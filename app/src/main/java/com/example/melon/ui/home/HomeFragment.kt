package com.example.melon.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.melon.databinding.FragmentHomeBinding
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.ShowPostsAdapter
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.example.melon.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var adapter: ShowPostsAdapter

    @Inject
    lateinit var userData: StoreUserData


    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        MainActivity.appPagePosition = Constants.FRAGMENT_HOME
        lifecycle.coroutineScope.launch {
            userData.setUserToken(Constants.USER_TOKEN)
        }
//        viewModel.loadData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

//            viewModel.loading.observe(viewLifecycleOwner){
//                if(it){
//                    homeFragmentLoadDataProgressbar.visibility = View.VISIBLE
//                    homeFragmentWholeLayout.visibility = View.INVISIBLE
//                }else{
//                    homeFragmentLoadDataProgressbar.visibility = View.INVISIBLE
//                    homeFragmentWholeLayout.visibility = View.VISIBLE
//                    onHomeFragmentListener.onHomeFragmentLoaded()
//                }
//            }

            homeFragmentToolbar.customToolbarNotificationsButton.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNotificationFragment())
            }


            lifecycle.coroutineScope.launch {
                userData.getFollowingsCollection().collect{
                    if(it.isNotEmpty()){
                        MainActivity.followingsIdList = ArrayList(it.toList())
                    }
                }
            }

            lifecycle.coroutineScope.launch {
                userData.getFollowersCollection().collect{
                    if(it.isNotEmpty()){
                        MainActivity.followersIdList = ArrayList(it.toList())
                    }
                }
            }

            lifecycle.coroutineScope.launch {
                userData.getFollowersRequestedCollection().collect{
                    Log.d("ssssssssss1", it.toList().toString())
                    if(it.isNotEmpty()){
                        MainActivity.followersRequestedIdList = ArrayList(it.toList())
                    }
                }
            }

            lifecycle.coroutineScope.launch {
                userData.getFollowingRequestedCollection().collect{
                    Log.d("ssssssssss2", it.toList().toString())
                    if(it.isNotEmpty()){
                        MainActivity.followingsRequestedIdList = ArrayList(it.toList())
                    }
                }
            }

//            //where to go when
//            adapter.setOnItemCLickListener { _, s ->
//                when(s){
//                    Constants.GO_TO_COMMENTS_FRAGMENT -> {
//                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCommentsFragment())
//                    }
//
//                    Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT-> {
//                        MainActivity.appPagePosition = Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT
//                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment(""))
//                    }
//                }
//            }

//
//            val list = ArrayList<Int>()
//            list.add(R.drawable.ph)
//            list.add(R.drawable.ph1)
//            list.add(R.drawable.ph2)
//            list.add(R.drawable.ph3)
//            list.add(R.drawable.ph4)
//            list.add(R.drawable.ph5)
//            list.add(R.drawable.ph6)
//            list.add(R.drawable.ph7)
//            list.add(R.drawable.ph8)
//            list.add(R.drawable.ph9)

//
//            adapter.differ.submitList(list)
//            homeFragmentPostRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            homeFragmentPostRecycler.adapter = adapter
//
//
//            // when refreshing the fragment
//            homeFragmentSwipeRefresh.setOnRefreshListener {
//                adapter.differ.submitList(list)
//                homeFragmentPostRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//                homeFragmentPostRecycler.adapter = adapter
//                homeFragmentSwipeRefresh.isRefreshing = false
//            }
        }
    }
}