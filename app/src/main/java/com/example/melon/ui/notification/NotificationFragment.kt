package com.example.melon.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.R
import com.example.melon.databinding.FragmentNotificationBinding
import com.example.melon.models.AcceptOrRejectModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.NotificationAdapter
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.example.melon.utils.getFollowIds
import com.example.melon.viewmodels.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment(){

    private lateinit var binding: FragmentNotificationBinding

    @Inject
    lateinit var adapter: NotificationAdapter

    private val viewModel: NotificationViewModel by viewModels()

    @Inject
    lateinit var userData: StoreUserData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        viewModel.loadRequests()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.appPagePosition = Constants.FRAGMENT_NOTIFICATION

        binding.apply {

            notificationFragmentToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            viewModel.requestsResponse.observe(viewLifecycleOwner){

//                if(it.followerRequests != null && it.followingRequests != null){
                    val tempFollowerRequested = it.followerRequests.getFollowIds()
                    val tempFollowingRequested = it.followingRequests.getFollowIds()

                    lifecycle.coroutineScope.launch {
                        userData.setFollowersRequestedCollection(tempFollowerRequested.toSet())
                        userData.setFollowingRequestedCollection(tempFollowingRequested.toSet())
                    }
                    MainActivity.followersRequestedIdList = tempFollowerRequested
                    MainActivity.followingsRequestedIdList = tempFollowingRequested
//                }


                if(it.followerRequests.isEmpty())
                    notificationFragmentNoNotificationText.visibility = View.VISIBLE
                else
                    notificationFragmentNoNotificationText.visibility = View.INVISIBLE

                adapter.setData(it.followerRequests)
                notificationFragmentRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                notificationFragmentRecycler.adapter = adapter
            }

            notificationFragmentSwipeRefresh.setOnRefreshListener {
                viewModel.loadRequests()
                notificationFragmentSwipeRefresh.isRefreshing = false
            }

            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    notificationFragmentProgressbar.visibility = View.VISIBLE
                    notificationFragmentSwipeRefresh.visibility = View.INVISIBLE
                }else{
                    notificationFragmentProgressbar.visibility = View.INVISIBLE
                    notificationFragmentSwipeRefresh.visibility = View.VISIBLE
                }
            }

            //set on clicks for confirm and delete
            adapter.setOnItemCLickListener { followModel, s ->
                when(s){
                    "confirm" -> {
                        viewModel.acceptFollowRequest(AcceptOrRejectModel(followModel.id))
                    }

                    "delete" -> {
                        viewModel.rejectFollowRequest(AcceptOrRejectModel(followModel.id))
                    }
                }
            }

            viewModel.acceptResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                viewModel.loadRequests()
            }

            viewModel.rejectResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                viewModel.loadRequests()
            }


        }
    }
}