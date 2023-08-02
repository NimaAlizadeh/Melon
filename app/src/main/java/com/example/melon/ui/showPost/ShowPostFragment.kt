package com.example.melon.ui.showPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.databinding.FragmentShowPostBinding
import com.example.melon.models.Post
import com.example.melon.models.UserX
import com.example.melon.ui.adapters.ShowPostsAdapter
import com.example.melon.ui.confirmAddPost.ConfirmAddPostFragmentArgs
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

    private var posts: Array<Post> = emptyArray()
    private lateinit var userX: UserX

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShowPostBinding.inflate(layoutInflater, container, false)
        viewModel.loadUserData(Constants.USER_TOKEN, args.userId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            showPostFragmentToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }



            showPostFragmentSwipeRefresh.setOnRefreshListener {
                loadRecycler(posts, args.position)
                showPostFragmentSwipeRefresh.isRefreshing = false
            }



            // viewModel to get userdata from server
            viewModel.userResponse.observe(viewLifecycleOwner){
                userX = it.user
                posts = it.user.posts.toTypedArray()
                loadRecycler(it.user.posts.toTypedArray(), args.position)
            }

            // adapter on click listeners
            adapter.setOnItemCLickListener { postModel, s ->
                when(s){
                    Constants.GO_TO_COMMENTS_FRAGMENT -> {
                        findNavController().navigate(ShowPostFragmentDirections.actionShowPostFragmentToCommentsFragment())
                    }

                    Constants.DO_LIKE_BUTTON -> {
                        // do what ever you want in this to like

                    }
                }
            }
        }
    }

    private fun loadRecycler(list: Array<Post>, position: Int){
        binding.apply {
            adapter.setValues(userX.username, userX.id)
            adapter.setData(list)
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            layoutManager.scrollToPosition(position)
            showPostFragmentRecycler.layoutManager = layoutManager
            showPostFragmentRecycler.adapter = adapter
        }
    }
}