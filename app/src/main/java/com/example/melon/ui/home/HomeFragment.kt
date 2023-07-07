package com.example.melon.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.R
import com.example.melon.databinding.FragmentHomeBinding
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.HomePostsAdapter
import com.example.melon.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var adapter: HomePostsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.appPagePosition = Constants.FRAGMENT_HOME

        binding.apply {

            //where to go when
            adapter.setOnItemCLickListener { _, s ->
                when(s){
                    Constants.GO_TO_COMMENTS_FRAGMENT -> {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCommentsFragment())
                    }

                    Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT-> {
                        MainActivity.appPagePosition = Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
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