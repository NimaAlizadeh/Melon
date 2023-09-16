package com.example.melon.ui.showSinglePost

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.melon.R
import com.example.melon.databinding.FragmentShowSinglePostBinding
import com.example.melon.models.LikeCommentModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.showSinglePost.ShowSinglePostFragmentArgs
import com.example.melon.ui.showSinglePost.ShowSinglePostFragmentDirections
import com.example.melon.ui.adapters.ShowPostsItemAdapter
import com.example.melon.viewmodels.ShowSinglePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowSinglePostFragment : Fragment() {

    lateinit var binding: FragmentShowSinglePostBinding

    private val args : ShowSinglePostFragmentArgs by navArgs()

    @Inject
    lateinit var adapter: ShowPostsItemAdapter

    private val pagerSnapHelper: PagerSnapHelper by lazy { PagerSnapHelper() }

    private val viewModel: ShowSinglePostViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentShowSinglePostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            showSinglePostFragmentToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            //indicator for recycler
            pagerSnapHelper.attachToRecyclerView(showSinglePostFragmentPicsRecycler)
            showSinglePostFragmentPicsIndicator.attachToRecyclerView(showSinglePostFragmentPicsRecycler, pagerSnapHelper)


            val post = args.postModel
            showSinglePostFragmentLikesText.text = post.likesCount.toString()
            showSinglePostFragmentTimeText.text = post.time
            if(post.description.isNotEmpty())
            {
                showSinglePostFragmentDescriptionText.text = post.description
                showSinglePostFragmentDescriptionText.visibility = View.VISIBLE
            }else
                showSinglePostFragmentDescriptionText.visibility = View.GONE


            // init adapter
            adapter.setValues(post.username, post.user)
            adapter.setData(post.images)
            showSinglePostFragmentPicsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            showSinglePostFragmentPicsRecycler.adapter = adapter

            // adapter on click listener
            adapter.setOnItemCLickListener { s, s2 ->
                if(s2 == "userNameOnClick"){
                    findNavController().navigate(
                        ShowSinglePostFragmentDirections.actionShowSinglePostFragmentToTheirProfileFragment(
                            s
                        )
                    )
                }
            }

            // is it liked?
            var isLiked = if(post.likes.contains(MainActivity.myUserID)){
                showSinglePostFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_24))
                true
            } else {
                showSinglePostFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_border_24))
                false
            }


            // like on click listener
            showSinglePostFragmentPostLikeButton.setOnClickListener {
                viewModel.likePost(LikeCommentModel(post._id, post.user))

                if(isLiked){
                    isLiked = false
                    showSinglePostFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_border_24))
                    showSinglePostFragmentLikesText.text = ((showSinglePostFragmentLikesText.text).toString().toInt() - 1).toString()
                }
                else{
                    isLiked = true
                    showSinglePostFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_24))
                    showSinglePostFragmentLikesText.text = ((showSinglePostFragmentLikesText.text).toString().toInt() + 1).toString()
                }
            }



            showSinglePostFragmentPostCommentButton.setOnClickListener {
                findNavController().navigate(
                    ShowSinglePostFragmentDirections.actionShowSinglePostFragmentToCommentsFragment(
                        post._id,
                        post.user,
                        post.comments.toTypedArray()
                    )
                )
            }
        }


    }

}