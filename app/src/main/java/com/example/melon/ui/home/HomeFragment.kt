package com.example.melon.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.R
import com.example.melon.databinding.FragmentHomeBinding
import com.example.melon.models.UserX
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.HomePostsAdapter
import com.example.melon.utils.Constants
import com.example.melon.viewmodels.HomeViewModel
import com.example.melon.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var adapter: HomePostsAdapter

    private val viewModel: HomeViewModel by viewModels()

    companion object{
        private lateinit var onHomeFragmentListener: OnHomeFragmentListener
    }

    interface OnHomeFragmentListener{
        fun onHomeFragmentLoaded()
    }

    fun setOnHomeFragmentListener(listener: OnHomeFragmentListener){
        onHomeFragmentListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        viewModel.loadData(Constants.USER_TOKEN)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.appPagePosition = Constants.FRAGMENT_HOME

        binding.apply {

            //get user data and fill it in global variable to use in another place
            viewModel.userDataResponse.observe(viewLifecycleOwner){ response ->

                if(response.success){
                    if(response.user.followings.isNotEmpty()){
                        MainActivity.followingList = response.user.followings
                        MainActivity.followingList.forEach {
                            MainActivity.followingIdList.add(it.id)
                        }
                    }

                    if(response.user.followerRequests.isNotEmpty()){
                        MainActivity.followRequestList = response.user.followerRequests
                        MainActivity.followRequestList.forEach {
                            MainActivity.followRequestIdList.add(it.id)
                        }
                    }
                }

            }

            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    homeFragmentLoadDataProgressbar.visibility = View.VISIBLE
                    homeFragmentWholeLayout.visibility = View.INVISIBLE
                }else{
                    homeFragmentLoadDataProgressbar.visibility = View.INVISIBLE
                    homeFragmentWholeLayout.visibility = View.VISIBLE
                    onHomeFragmentListener.onHomeFragmentLoaded()
                }
            }

            //where to go when
            adapter.setOnItemCLickListener { _, s ->
                when(s){
                    Constants.GO_TO_COMMENTS_FRAGMENT -> {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCommentsFragment())
                    }

                    Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT-> {
                        MainActivity.appPagePosition = Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment(""))
                    }
                }
            }


            val list = ArrayList<Int>()
            list.add(R.drawable.ph)
            list.add(R.drawable.ph1)
            list.add(R.drawable.ph2)
            list.add(R.drawable.ph3)
            list.add(R.drawable.ph4)
            list.add(R.drawable.ph5)
            list.add(R.drawable.ph6)
            list.add(R.drawable.ph7)
            list.add(R.drawable.ph8)
            list.add(R.drawable.ph9)


            adapter.differ.submitList(list)
            homeFragmentPostRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            homeFragmentPostRecycler.adapter = adapter


            // when refreshing the fragment
            homeFragmentSwipeRefresh.setOnRefreshListener {
                adapter.differ.submitList(list)
                homeFragmentPostRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                homeFragmentPostRecycler.adapter = adapter
                homeFragmentSwipeRefresh.isRefreshing = false
            }
        }
    }
}