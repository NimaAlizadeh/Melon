package com.example.melon.ui.follows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.melon.databinding.FragmentFollowsBinding
import com.example.melon.models.FollowModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.NotificationAdapter
import com.example.melon.ui.adapters.ViewPagerAdapter
import com.example.melon.utils.Constants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FollowsFragment : Fragment() {

    private lateinit var binding: FragmentFollowsBinding

    private val args by navArgs<FollowsFragmentArgs>()

    @Inject
    lateinit var adapter0: NotificationAdapter

    @Inject
    lateinit var adapter1: NotificationAdapter

    private lateinit var dataSet: List<List<FollowModel>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val fragments = listOf(
                FollowersFragment().apply { setAdapter(adapter0) },
                FollowingsFragment().apply { setAdapter(adapter1) },
            )

            dataSet = listOf(
                args.followers.toList(),
                args.followings.toList(),
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

        }
    }

    private fun initAdapter(position: Int){

        val dataForThisPage = dataSet[position]

        if(dataForThisPage.isNotEmpty())
            binding.followsViewPagerItemNotFoundText.visibility = View.GONE

        if(position == 0)
            adapter0.setData(dataForThisPage)
        else if(position == 1)
            adapter1.setData(dataForThisPage)

    }

}