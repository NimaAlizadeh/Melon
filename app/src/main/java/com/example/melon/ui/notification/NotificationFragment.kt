package com.example.melon.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.R
import com.example.melon.databinding.FragmentNotificationBinding
import com.example.melon.models.AcceptOrRejectModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.NotificationAdapter
import com.example.melon.utils.Constants
import com.example.melon.viewmodels.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment(){

    private lateinit var binding: FragmentNotificationBinding

    @Inject
    lateinit var adapter: NotificationAdapter

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        viewModel.loadRequests(Constants.USER_TOKEN)
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
                if(it.requests.isEmpty())
                    notificationFragmentNoNotificationText.visibility = View.VISIBLE
                else
                    notificationFragmentNoNotificationText.visibility = View.INVISIBLE
                adapter.setData(it.requests)
                notificationFragmentRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                notificationFragmentRecycler.adapter = adapter
            }

            notificationFragmentSwipeRefresh.setOnRefreshListener {
                viewModel.loadRequests(Constants.USER_TOKEN)
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
                        viewModel.acceptFollowRequest(Constants.USER_TOKEN, AcceptOrRejectModel(followModel.id))
                    }

                    "delete" -> {
                        viewModel.rejectFollowRequest(Constants.USER_TOKEN, AcceptOrRejectModel(followModel.id))
                    }
                }
            }

            viewModel.acceptResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follower request accepted successfully"){
                    viewModel.loadRequests(Constants.USER_TOKEN)
                }
            }

            viewModel.rejectResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follower request rejected successfully"){
                    viewModel.loadRequests(Constants.USER_TOKEN)
                }
            }

        }
    }
}