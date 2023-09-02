package com.example.melon.ui.showPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.databinding.FragmentShowPostBinding
import com.example.melon.models.LikeCommentModel
import com.example.melon.models.Post
import com.example.melon.models.RecyclerViewState
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.ShowPostsAdapter
import com.example.melon.utils.Constants
import com.example.melon.viewmodels.ShowPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowPostFragment : Fragment() {

    private lateinit var binding: FragmentShowPostBinding

    @Inject
    lateinit var adapter: ShowPostsAdapter

    private val args : ShowPostFragmentArgs by navArgs()

    val viewModel: ShowPostViewModel by viewModels()

    private var posts: List<Post> = emptyList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowPostBinding.inflate(layoutInflater, container, false)

        if(MainActivity.appPagePosition == Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT || MainActivity.appPagePosition == Constants.GO_TO_MY_USER_PROFILE_FRAGMENT)
            viewModel.leftPosition.postValue(RecyclerViewState(args.position, 0))

        if(MainActivity.appPagePosition == Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT)
            viewModel.loadPostsWithId(args.userId)
        else if(MainActivity.appPagePosition == Constants.GO_TO_MY_USER_PROFILE_FRAGMENT)
            viewModel.loadPostsWithToken()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            showPostFragmentToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }


            // viewModel to get userdata from server
            viewModel.allPostsResponseList.observe(viewLifecycleOwner){
                posts = it.posts

                viewModel.leftPosition.observe(viewLifecycleOwner){ state ->
                    loadRecycler(posts, state.position, state.offset)
                }
            }

//            // viewModel to get response of liking a post
//            viewModel.likePostResponse.observe(viewLifecycleOwner){
//                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//            }

            //refreshing
            showPostFragmentSwipeRefresh.setOnRefreshListener {
                viewModel.leftPosition.observe(viewLifecycleOwner){ state ->
                    loadRecycler(posts, state.position, state.offset)
                }
                showPostFragmentSwipeRefresh.isRefreshing = false
            }

            // adapter on click listeners
            adapter.setOnItemCLickListener { postModel, s ->
                when(s){
                    Constants.GO_TO_COMMENTS_FRAGMENT -> {
                        val layoutManager = showPostFragmentRecycler.layoutManager as LinearLayoutManager
                        val position = layoutManager.findFirstVisibleItemPosition()
                        val firstVisibleView = layoutManager.findViewByPosition(position)
                        val offset = firstVisibleView?.top ?: 0
                        viewModel.leftPosition.postValue(RecyclerViewState(position, offset))
                        findNavController().navigate(ShowPostFragmentDirections.actionShowPostFragmentToCommentsFragment(postModel._id, args.userId, postModel.comments.toTypedArray()))
                    }

                    Constants.DO_LIKE_BUTTON -> {
                        viewModel.likePost(LikeCommentModel(postModel._id, args.userId))
                    }
                }
            }
        }
    }

    private fun loadRecycler(list: List<Post>, position: Int, offset: Int){
        binding.apply {
            adapter.setValues(args.username, args.userId)
            adapter.setData(list.reversed())
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            layoutManager.scrollToPositionWithOffset(position, offset)
            showPostFragmentRecycler.layoutManager = layoutManager
            showPostFragmentRecycler.adapter = adapter
        }
    }
}