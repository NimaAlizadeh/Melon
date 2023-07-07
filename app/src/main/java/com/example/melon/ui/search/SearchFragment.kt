package com.example.melon.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.melon.R
import com.example.melon.databinding.FragmentSearchBinding
import com.example.melon.ui.adapters.ProfilePostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var adapter: ProfilePostsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
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
//            list.add(R.drawable.ph3)
//            list.add(R.drawable.ph4)
//            list.add(R.drawable.ph5)
//            list.add(R.drawable.ph6)
//            list.add(R.drawable.ph7)
//            list.add(R.drawable.ph8)
//            list.add(R.drawable.ph1)
//            list.add(R.drawable.ph2)
//            list.add(R.drawable.ph3)
//            list.add(R.drawable.ph4)
//            list.add(R.drawable.ph5)
//            list.add(R.drawable.ph6)
//            list.add(R.drawable.ph4)
//            list.add(R.drawable.ph5)
//            list.add(R.drawable.ph6)
//            list.add(R.drawable.ph7)
//            list.add(R.drawable.ph8)
//            list.add(R.drawable.ph9)
//            list.add(R.drawable.ph3)
//            list.add(R.drawable.ph4)
//            list.add(R.drawable.ph5)
//            list.add(R.drawable.ph6)
//            list.add(R.drawable.ph7)


//            adapter.differ.submitList(list)
//            searchFragmentRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
//            searchFragmentRecycler.setHasFixedSize(true)
//            searchFragmentRecycler.adapter = adapter

            // when refreshing the fragment
            searchFragmentSwipeRefresh.setOnRefreshListener {
//                adapter.differ.submitList(list)
//                searchFragmentRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
//                searchFragmentRecycler.setHasFixedSize(true)
//                searchFragmentRecycler.adapter = adapter

                searchFragmentSwipeRefresh.isRefreshing = false
            }
        }
    }

}