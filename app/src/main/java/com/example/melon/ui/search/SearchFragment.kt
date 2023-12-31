package com.example.melon.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.R
import com.example.melon.databinding.FragmentSearchBinding
import com.example.melon.models.User
import com.example.melon.ui.showSinglePost.ShowSinglePostFragment
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.LoadMoreAdapter
import com.example.melon.ui.adapters.SearchPostsAdapter
import com.example.melon.ui.adapters.SearchUserAdapter
import com.example.melon.utils.Constants
import com.example.melon.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var userAdapter: SearchUserAdapter

    @Inject
    lateinit var postAdapter: SearchPostsAdapter

    private val viewModel: SearchViewModel by viewModels()

    companion object{
        var isListVisible = MutableLiveData<Boolean>()
        var edtClicked = MutableLiveData<Boolean>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        isListVisible.value = true
        edtClicked.value = false
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.apply {
            postAdapter.refresh()
            postAdapter.notifyDataSetChanged()

            searchFragmentExploreRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
            searchFragmentExploreRecycler.adapter = postAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.appPagePosition = Constants.FRAGMENT_SEARCH

        binding.apply {


            // post --------------------------------------------------------------------------------------------------------


            lifecycle.coroutineScope.launch {
                viewModel.searchPostsList.collect{
                    postAdapter.submitData(it)
                }
            }

            lifecycle.coroutineScope.launch {
                postAdapter.loadStateFlow.collect{
                    searchFragmentUserProgressbar.isVisible = it.refresh is LoadState.Loading
                    searchFragmentExploreRecycler.isVisible = it.refresh !is LoadState.Loading
                }
            }

            //init adapter
            searchFragmentExploreRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
            searchFragmentExploreRecycler.adapter = postAdapter

            //refresh
            searchFragmentSwipeRefresh.setOnRefreshListener {
                postAdapter.refresh()
                postAdapter.notifyDataSetChanged()

                searchFragmentExploreRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                searchFragmentExploreRecycler.adapter = postAdapter
                searchFragmentSwipeRefresh.isRefreshing = false
            }

            searchFragmentExploreRecycler.adapter = postAdapter.withLoadStateFooter(
                LoadMoreAdapter{postAdapter.retry()}
            )


            postAdapter.setOnItemCLickListener { post, s ->
                if(s == "onClick"){
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToShowSinglePostFragment(post))
                }
            }


            // user --------------------------------------------------------------------------------------------------------

            isListVisible.observe(viewLifecycleOwner){
                toggleListVisibility()
            }

            //I have to use both onClickListener and onFocusChangeListener to open the user search list
            searchFragmentSearchEdt.setOnClickListener {
                if(!edtClicked.value!!){
                    isListVisible.postValue(!isListVisible.value!!)
                    edtClicked.value = true
                }
            }
            searchFragmentSearchEdt.setOnFocusChangeListener { _, _ ->
                isListVisible.postValue(!isListVisible.value!!)
                edtClicked.value = true
            }

            //here we send userName that we are searching to server
            searchFragmentSearchEdt.addTextChangedListener {
                if(it.toString().isNotEmpty())
                    viewModel.loadUserSearch(it.toString())
                else
                    loadAdapter(emptyList())
            }

            //let's get user search data from server
            viewModel.userSearchLiveList.observe(viewLifecycleOwner){
                if(it.success){
                    if(it.users.isNotEmpty()){
                        loadAdapter(it.users)
                    }else{
                        loadAdapter(emptyList())
                        searchFragmentUserNotFoundText.visibility = View.VISIBLE
                    }

                }else{
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    searchFragmentUserProgressbar.visibility = View.VISIBLE
                    searchFragmentUserRecycler.visibility = View.INVISIBLE
                }else{
                    searchFragmentUserProgressbar.visibility = View.INVISIBLE
                    searchFragmentUserRecycler.visibility = View.VISIBLE
                }
            }

            userAdapter.setOnItemCLickListener { userX, s ->
                if(s == Constants.SEARCH_FRAGMENT_GO_TO_PROFILE_FRAGMENT){
                    MainActivity.appPagePosition = Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToTheirProfileFragment(userX.id))
                }
            }
        }
    }

    private fun toggleListVisibility(){
        binding.apply {
            if (isListVisible.value!!) {
                // Hide the list with out animation
                searchFragmentUserLayout.visibility = View.INVISIBLE
            } else {
                // Show the list with animation
                val slideDown: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
                searchFragmentUserLayout.startAnimation(slideDown)
                searchFragmentUserLayout.visibility = View.VISIBLE
            }
        }
    }

    fun onBackPressed() : Boolean{
        if (!isListVisible.value!!) {
            isListVisible.value = !isListVisible.value!!
            edtClicked.value = false
            return true
        }
        return false
    }

    private fun loadAdapter(list: List<User>){
        binding.apply {
            searchFragmentUserNotFoundText.visibility = View.GONE
            userAdapter.setData(list)
            searchFragmentUserRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            searchFragmentUserRecycler.setHasFixedSize(true)
            searchFragmentUserRecycler.adapter = userAdapter
        }
    }
}