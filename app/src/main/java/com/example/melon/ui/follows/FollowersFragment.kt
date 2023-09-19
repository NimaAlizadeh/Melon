package com.example.melon.ui.follows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.databinding.FollowsViewPagerItemBinding
import com.example.melon.ui.adapters.FollowsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : Fragment() {

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


        }
    }

    private fun loadAdapter(){
        binding.apply {
            followsViewPagerItemRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            followsViewPagerItemRecycler.adapter = adapter
        }
    }

}