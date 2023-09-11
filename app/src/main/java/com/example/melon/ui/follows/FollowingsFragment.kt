package com.example.melon.ui.follows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.databinding.FollowsViewPagerItemBinding
import com.example.melon.databinding.FragmentFollowingsBinding
import com.example.melon.databinding.FragmentFollowsBinding
import com.example.melon.models.FollowModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.FollowsAdapter
import com.example.melon.ui.adapters.FollowsViewPagerAdapter
import com.example.melon.ui.adapters.NotificationAdapter
import com.example.melon.utils.Constants
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FollowingsFragment : Fragment() {

    private lateinit var binding: FollowsViewPagerItemBinding

    private lateinit var adapter: FollowsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FollowsViewPagerItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    fun setAdapter(adapter: FollowsAdapter){
        this.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            loadAdapter()


            followsViewPagerItemSearchEdt.setOnClickListener {
                adapter.setData(emptyList())
                loadAdapter()
            }


            adapter.setOnItemCLickListener { followModel, s ->
                when(s){
                    "follow" -> {

                    }

                    "unfollow" -> {

                    }
                }
            }

        }
    }

    private fun loadAdapter(){
        binding.apply {
            followsViewPagerItemRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            followsViewPagerItemRecycler.adapter = adapter
        }
    }

}