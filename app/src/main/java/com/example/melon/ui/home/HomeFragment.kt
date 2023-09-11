package com.example.melon.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.Engine.LoadStatus
import com.example.melon.databinding.FragmentHomeBinding
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.HomePostsAdapter
import com.example.melon.ui.adapters.LoadMoreAdapter
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
    lateinit var adapter: HomePostsAdapter

    @Inject
    lateinit var userData: StoreUserData


    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        MainActivity.appPagePosition = Constants.FRAGMENT_HOME
        lifecycle.coroutineScope.launch {
            userData.setUserToken(Constants.USER_TOKEN)
        }

        // get first data that the applications needs from data store
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
                if(it.isNotEmpty()){
                    MainActivity.followersRequestedIdList = ArrayList(it.toList())
                }
            }
        }
        lifecycle.coroutineScope.launch {
            userData.getFollowingRequestedCollection().collect{
                if(it.isNotEmpty()){
                    MainActivity.followingsRequestedIdList = ArrayList(it.toList())
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            homeFragmentToolbar.customToolbarNotificationsButton.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNotificationFragment())
            }


            lifecycle.coroutineScope.launch {
                viewModel.postsList.collect{
                    adapter.submitData(it)
                }
            }

            lifecycle.coroutineScope.launch {
                adapter.loadStateFlow.collect{
                    homeFragmentLoadDataProgressbar.isVisible = it.refresh is LoadState.Loading
                }
            }

            // init adapter
            homeFragmentPostRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            homeFragmentPostRecycler.adapter = adapter


            //refresh
            homeFragmentSwipeRefresh.setOnRefreshListener {
                adapter.refresh()
                homeFragmentSwipeRefresh.isRefreshing = false
            }

            homeFragmentPostRecycler.adapter = adapter.withLoadStateFooter(
                LoadMoreAdapter{adapter.retry()}
            )

        }
    }
}