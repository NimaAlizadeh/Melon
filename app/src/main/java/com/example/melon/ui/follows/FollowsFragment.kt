package com.example.melon.ui.follows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.melon.databinding.FragmentFollowsBinding
import com.example.melon.models.*
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.FollowsAdapter
import com.example.melon.ui.adapters.NotificationAdapter
import com.example.melon.ui.adapters.ViewPagerAdapter
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.example.melon.utils.getFollowIds
import com.example.melon.viewmodels.FollowsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FollowsFragment : Fragment() {

    private lateinit var binding: FragmentFollowsBinding

    private val args by navArgs<FollowsFragmentArgs>()

    @Inject
    lateinit var adapter0: FollowsAdapter

    @Inject
    lateinit var adapter1: FollowsAdapter

    @Inject
    lateinit var dataStore: StoreUserData

    private val viewModel: FollowsViewModel by viewModels()

    private lateinit var dataSet: List<ArrayList<FollowModel>>

    private var userId = ""
    private var adapterPosition = 0
    private lateinit var followModel: FollowModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            followsFragmentUserName.text = args.userName

            val fragments = listOf(
                FollowersFragment().apply { setAdapter(adapter0) },
                FollowingsFragment().apply { setAdapter(adapter1) },
            )

            dataSet = arrayListOf(
                args.followers.toCollection(ArrayList()),
                args.followings.toCollection(ArrayList())
            )


            val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, fragments, viewLifecycleOwner.lifecycle)
            followsFragmentViewPager.adapter = viewPagerAdapter

            TabLayoutMediator(followsFragmentTabsLayout, followsFragmentViewPager) { tab, position ->
                if(position == 0)
                    tab.text = "Followers"
                else if(position == 1)
                    tab.text = "Followings"
            }.attach()

            followsFragmentTabsLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    followsFragmentViewPager.currentItem = tab!!.position
                    initAdapter(tab.position)
                }


                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })


            followsFragmentViewPager.post {

                if(MainActivity.appPagePosition == Constants.FRAGMENT_FOLLOWERS){
                    followsFragmentViewPager.currentItem = 0

                    initAdapter(0)
                }else if(MainActivity.appPagePosition == Constants.FRAGMENT_FOLLOWINGS){
                    followsFragmentViewPager.currentItem = 1

                    initAdapter(1)
                }

            }


            followsFragmentBackButton.setOnClickListener {
                findNavController().popBackStack()
            }

            val adapters = listOf(adapter0, adapter1)
            adapters.forEach {
                it.setOnItemCLickListener { adapterPosition , followModel, s ->

                    userId = followModel.id
                    this@FollowsFragment.adapterPosition = adapterPosition
                    this@FollowsFragment.followModel = followModel

                    when(s){
                        "Follow" -> {
                            viewModel.addFollower(AddFollowerModel(followModel.id))
                        }

                        "Remove" -> {
                            viewModel.removeFollower(RemoveFollowModel(followModel.id))
                        }

                        "Following" -> {
                            viewModel.unFollow(UnFollowModel(followModel.id))
                        }

                        "Requested" -> {
                            viewModel.cancelRequest(RemoveFollowerModel(followModel.id))
                        }

                        Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT -> {
                            findNavController().navigate(FollowsFragmentDirections.actionFollowsFragmentToTheirProfileFragment(followModel.id))
                        }
                    }
                }
            }


            // when click on following
            viewModel.unFollowResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follower unfollowed successfully")
                    fetchData(it)


                adapters.forEach {adapter ->
                    adapter.notifyItemChanged(adapterPosition)
                }

            }


            // when click on requested
            viewModel.cancelRequestResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follow request cancelled successfully")
                    fetchData(it)


                adapters.forEach {adapter ->
                    adapter.notifyItemChanged(adapterPosition)
                }

            }


            // when click on remove
            viewModel.removeResponse.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follower removed successfully")
                    fetchData(it)

                adapters.forEach {adapter ->
                    adapter.removeAt(adapterPosition, followModel)
                }
            }

            // when click on follow
            viewModel.followResponse.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follow request sent successfully"){
                    MainActivity.followingsRequestedIdList.add(userId)
                    lifecycle.coroutineScope.launch {
                        dataStore.setFollowingRequestedCollection(MainActivity.followingsRequestedIdList.toSet())
                    }

                }else if(it.message == "User followed successfully")
                    fetchData(it)


                adapters.forEach {adapter ->
                    adapter.notifyItemChanged(adapterPosition)
                }
            }

        }
    }

    private fun initAdapter(position: Int){

        val dataForThisPage = dataSet[position]

        if(dataForThisPage.isNotEmpty())
            binding.followsViewPagerItemNotFoundText.visibility = View.GONE
        else
            binding.followsViewPagerItemNotFoundText.visibility = View.VISIBLE

        adapter0.setPage(Constants.FRAGMENT_FOLLOWERS)
        adapter1.setPage(Constants.FRAGMENT_FOLLOWINGS)

        if(position == 0)
            adapter0.setData(dataForThisPage)
        else if(position == 1)
            adapter1.setData(dataForThisPage)

    }

    private fun fetchData(it: FollowResponse){
        if(it.data.followers != null && it.data.followings != null){
            val tempFollowings = it.data.followings.getFollowIds()
            val tempFollowers = it.data.followers.getFollowIds()

            MainActivity.followingsIdList = tempFollowings
            MainActivity.followersIdList = tempFollowers

            lifecycle.coroutineScope.launch {
                dataStore.setFollowingsCollection(tempFollowings.toSet())
                dataStore.setFollowersCollection(tempFollowers.toSet())
            }
        }

        if(it.data.followerRequests != null && it.data.followingRequests != null){
            val tempFollowerRequested = it.data.followerRequests.getFollowIds()
            val tempFollowingRequested = it.data.followingRequests.getFollowIds()

            lifecycle.coroutineScope.launch {
                dataStore.setFollowersRequestedCollection(tempFollowerRequested.toSet())
                dataStore.setFollowingRequestedCollection(tempFollowingRequested.toSet())
            }
            MainActivity.followersRequestedIdList = tempFollowerRequested
            MainActivity.followingsRequestedIdList = tempFollowingRequested
        }
    }
}