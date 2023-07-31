package com.example.melon.ui.follows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.melon.R
import com.example.melon.databinding.FragmentFollowsBinding
import com.example.melon.databinding.FragmentNotificationBinding
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants


class FollowsFragment : Fragment() {

    private lateinit var binding: FragmentFollowsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.appPagePosition = Constants.FRAGMENT_FOLLOWS
    }

}