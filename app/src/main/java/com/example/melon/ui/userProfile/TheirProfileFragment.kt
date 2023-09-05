package com.example.melon.ui.userProfile

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.melon.R
import com.example.melon.databinding.FragmentProfileBinding
import com.example.melon.databinding.FragmentTheirProfileBinding
import com.example.melon.models.*
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.ProfilePostsAdapter
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.example.melon.utils.getFollowIds
import com.example.melon.utils.getIds
import com.example.melon.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TheirProfileFragment : Fragment(){

    private lateinit var binding: FragmentTheirProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var adapter: ProfilePostsAdapter

    @Inject
    lateinit var dataStore: StoreUserData

    private var userId = ""
    private var followUserId = ""
    private var theirUserId = ""
    private var userName = ""
    private var isPrivate = false
    private var isFromFollow = false
    private var followersList: Array<FollowModel> = emptyArray()
    private var followingsList: Array<FollowModel> = emptyArray()

    private val args by navArgs<TheirProfileFragmentArgs>()

    private var isAvatarLoaded = false

    @Inject
    lateinit var userData: StoreUserData

    private var posts: Array<Post> = emptyArray()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentTheirProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        if(!isAvatarLoaded)
            binding.theirUserProfileProgressbar.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            loadDataWhenIsTheirProfile(args.searchingUserID)

            //refreshing the page
            theirProfileFragmentSwipeRefresh.setOnRefreshListener {
                theirUserProfileProgressbar.visibility = View.VISIBLE

                loadDataWhenIsTheirProfile(args.searchingUserID)

                theirProfileFragmentSwipeRefresh.isRefreshing = false
            }

            //back button onclick
            theirUserProfileBackButton.setOnClickListener {
                findNavController().popBackStack()
            }

            //follow button on click listener
            theirUserProfileFollowButton.setOnClickListener {
                viewModel.addFollower(AddFollowerModel(followUserId))
                theirUserProfileUnFollowButton.visibility = View.INVISIBLE
                theirUserProfileFollowButton.visibility = View.INVISIBLE
                theirUserProfileRequestedButton.visibility = View.INVISIBLE
            }

            // unfollow button on click listener
            theirUserProfileUnFollowButton.setOnClickListener {
                viewModel.unFollow(UnFollowModel(followUserId))
                theirUserProfileUnFollowButton.visibility = View.INVISIBLE
                theirUserProfileFollowButton.visibility = View.INVISIBLE
                theirUserProfileRequestedButton.visibility = View.INVISIBLE
            }

            theirUserProfileRequestedButton.setOnClickListener {
                viewModel.cancelRequest(RemoveFollowerModel(followUserId))
                theirUserProfileUnFollowButton.visibility = View.INVISIBLE
                theirUserProfileFollowButton.visibility = View.INVISIBLE
                theirUserProfileRequestedButton.visibility = View.INVISIBLE
            }

            theirUserProfileFollowersNumberText.setOnClickListener {
                findNavController().navigate(TheirProfileFragmentDirections.actionTheirProfileFragmentToFollowsFragment(followersList, followingsList, userName))
                MainActivity.appPagePosition = Constants.FRAGMENT_FOLLOWERS
            }

            theirUserProfileFollowingNumberText.setOnClickListener {
                findNavController().navigate(TheirProfileFragmentDirections.actionTheirProfileFragmentToFollowsFragment(followersList, followingsList, userName))
                MainActivity.appPagePosition = Constants.FRAGMENT_FOLLOWINGS
            }

            theirUserProfileFollowersTextView.setOnClickListener {
                findNavController().navigate(TheirProfileFragmentDirections.actionTheirProfileFragmentToFollowsFragment(followersList, followingsList, userName))
                MainActivity.appPagePosition = Constants.FRAGMENT_FOLLOWERS
            }

            theirUserProfileFollowingTextView.setOnClickListener {
                findNavController().navigate(TheirProfileFragmentDirections.actionTheirProfileFragmentToFollowsFragment(followersList, followingsList, userName))
                MainActivity.appPagePosition = Constants.FRAGMENT_FOLLOWINGS
            }

            //response of trying to follow some one
            viewModel.followResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follow request sent successfully"){
                    if(isPrivate){
                        MainActivity.followingsRequestedIdList.add(userId)
                        lifecycle.coroutineScope.launch {
                            dataStore.setFollowingRequestedCollection(MainActivity.followingsRequestedIdList.toSet())
                        }
                    }

                }else if(it.message == "User followed successfully"){
                    val tempFollowings = it.data.followings.getFollowIds()
                    val tempFollowers = it.data.followers.getFollowIds()

                    MainActivity.followingsIdList = tempFollowings
                    MainActivity.followersIdList = tempFollowers

                    lifecycle.coroutineScope.launch {
                        userData.setFollowingsCollection(tempFollowings.toSet())
                        userData.setFollowersCollection(tempFollowers.toSet())
                    }
                } else{
                    theirUserProfileFollowButton.visibility = View.VISIBLE
                }

                isFromFollow = true
                viewModel.followLoading.postValue(true)
                viewModel.loadUserDataWithId(userId)


            }

            // response of unFollowing some one
            viewModel.unFollowResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follower unfollowed successfully"){

//                    if(it.data.followers != null && it.data.followings != null){
                    val tempFollowings = it.data.followings.getFollowIds()
                    val tempFollowers = it.data.followers.getFollowIds()

                    MainActivity.followingsIdList = tempFollowings
                    MainActivity.followersIdList = tempFollowers

                    lifecycle.coroutineScope.launch {
                        userData.setFollowingsCollection(tempFollowings.toSet())
                        userData.setFollowersCollection(tempFollowers.toSet())
                    }
//                    }

                    isFromFollow = true
                    viewModel.followLoading.postValue(true)
                    viewModel.loadUserDataWithId(userId)
                }else{
                    theirUserProfileUnFollowButton.visibility = View.VISIBLE
                }

            }

            // response of cancelling the request of following some one
            viewModel.cancelRequestResponse.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.message == "Follow request cancelled successfully"){

//                    if(it.data.followerRequests != null && it.data.followingRequests != null){
                    val tempFollowerRequested = it.data.followerRequests.getFollowIds()
                    val tempFollowingRequested = it.data.followingRequests.getFollowIds()

                    lifecycle.coroutineScope.launch {
                        userData.setFollowersRequestedCollection(tempFollowerRequested.toSet())
                        userData.setFollowingRequestedCollection(tempFollowingRequested.toSet())
                    }
                    MainActivity.followersRequestedIdList = tempFollowerRequested
                    MainActivity.followingsRequestedIdList = tempFollowingRequested
//                    }

                    isFromFollow = true
                    viewModel.followLoading.postValue(true)
                    viewModel.loadUserDataWithId(userId)
                }else{
                    theirUserProfileRequestedButton.visibility = View.GONE
                }

            }

            viewModel.followLoading.observe(viewLifecycleOwner){
                if(it)
                    theirFollowProgressbar.visibility = View.VISIBLE
                else
                    theirFollowProgressbar.visibility = View.INVISIBLE
            }


            viewModel.allPostsResponseList.observe(viewLifecycleOwner){

                if(it.success){
                    theirUserProfilePostsNumberText.text = it.posts.size.toString()
                    if(it != null)
                    {
                        loadDataToRecycler(it.posts)
                        posts = it.posts.toTypedArray()
                    }
                    else
                    {
                        loadDataToRecycler(emptyList())
                    }
                }
            }

            viewModel.userDataResponseWithId.observe(viewLifecycleOwner){

                isFromFollow = false

                if(it.success){

                    followUserId = it.user.id
                    theirUserId = it.user.id
                    userId = it.user.id
                    isPrivate = it.user.private
                    userName = it.user.username


                    followersList = emptyArray()
                    followingsList = emptyArray()
                    followersList = it.user.followers.toTypedArray()
                    followingsList = it.user.followings.toTypedArray()

                    loadDataIntoViews(bio = it.user.bio, username = it.user.username, followers = it.user.followersCount.toString(), followings = it.user.followingsCount.toString())

                    loadProfileAvatar(userId)

                    if(!it.user.private || (it.user.private && MainActivity.followingsIdList.contains(userId))){

                        viewModel.loadPostsWithId(userId)
                        theirUserProfileNoPostImage.setBackgroundResource(R.drawable.baseline_camera_24)
                        theirUserProfileNoPostText.text = "No Posts Yet"

                    }else{
                        theirUserProfileNoPostImage.setBackgroundResource(R.drawable.baseline_lock_24)
                        theirUserProfileNoPostText.text = "This Account is private"
                    }

                    if(MainActivity.followingsIdList.contains(theirUserId)){
                        theirUserProfileUnFollowButton.visibility = View.VISIBLE
                        theirUserProfileFollowButton.visibility = View.INVISIBLE
                        theirUserProfileRequestedButton.visibility = View.INVISIBLE
                    }
                    else if(MainActivity.followingsRequestedIdList.contains(theirUserId)){
                        theirUserProfileUnFollowButton.visibility = View.INVISIBLE
                        theirUserProfileFollowButton.visibility = View.INVISIBLE
                        theirUserProfileRequestedButton.visibility = View.VISIBLE
                    }
                    else{
                        theirUserProfileFollowButton.visibility = View.VISIBLE
                        theirUserProfileUnFollowButton.visibility = View.INVISIBLE
                        theirUserProfileRequestedButton.visibility = View.INVISIBLE
                    }
                }

                viewModel.followLoading.postValue(false)
            }


            viewModel.loading.observe(viewLifecycleOwner){
                if(!isFromFollow){
                    if(it){
                        theirProfileFragmentLoadingProgressbar.visibility = View.VISIBLE
                        theirProfileFragmentWholeLayout.visibility = View.GONE
                    }
                    else
                    {
                        theirProfileFragmentLoadingProgressbar.visibility = View.GONE
                        theirProfileFragmentWholeLayout.visibility = View.VISIBLE
                    }
                }
            }

            viewModel.postloading.observe(viewLifecycleOwner){
                if(it){
                    theirUserProfileLoadPostsProgressbar.visibility = View.VISIBLE
                    theirUserProfilePostsRecycler.visibility = View.INVISIBLE
                    theirUserProfileNoPostImage.visibility = View.INVISIBLE
                    theirUserProfileNoPostText.visibility = View.INVISIBLE
                }
                else
                    theirUserProfileLoadPostsProgressbar.visibility = View.INVISIBLE
            }

            // on click listeners from adapter
            adapter.setOnItemCLickListener { position, s ->
                if(s == "onClick"){
                    findNavController().navigate(TheirProfileFragmentDirections.actionTheirProfileFragmentToShowPostFragment(position, userId, userName))
                }
            }
        }
    }

    private fun loadDataWhenIsTheirProfile(userId: String){
        binding.apply {
            viewModel.loadUserDataWithId(userId)
        }
    }

    private fun loadDataIntoViews(bio: String, username: String, followers: String, followings: String){
        binding.apply {

            if(bio.isEmpty())
                theirUserProfileBio.visibility = View.GONE
            else
                theirUserProfileBio.visibility = View.VISIBLE

            theirUserProfileBio.text = bio
            theirUserProfileUserName.text = username
            theirUserProfileFollowersNumberText.text = followers
            theirUserProfileFollowingNumberText.text = followings
        }
    }

    private fun loadDataToRecycler(it: List<Post>){

        binding.apply {

            if(it.reversed().isNotEmpty()){

                adapter.differ.submitList(it.reversed())
                theirUserProfilePostsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                theirUserProfilePostsRecycler.setHasFixedSize(true)
                theirUserProfilePostsRecycler.adapter = adapter

                theirUserProfileNoPostText.visibility = View.GONE
                theirUserProfileNoPostImage.visibility = View.GONE
                theirUserProfilePostsRecycler.visibility = View.VISIBLE
                theirUserProfilePostsNumberText.text = it.reversed().size.toString()

            }
            else{

                theirUserProfileNoPostText.visibility = View.VISIBLE
                theirUserProfileNoPostImage.visibility = View.VISIBLE
                theirUserProfileNoPostImage.setBackgroundResource(R.drawable.baseline_camera_24)
                theirUserProfileNoPostText.text = "No Posts Yet"
                theirUserProfilePostsRecycler.visibility = View.INVISIBLE

            }
        }
    }

    private fun loadProfileAvatar(userId: String) {
        //load user profile avatar with glide
        val headers = Headers {
            val headersMap = HashMap<String, String>()
            headersMap["auth-token"] = Constants.USER_TOKEN
            headersMap
        }
        val glideUrl = GlideUrl(Constants.BASE_URL + "auth/avatars/" + userId, headers)

        Glide.with(requireContext())
            .load(glideUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    binding.theirUserProfileProgressbar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    binding.theirUserProfileProgressbar.visibility = View.GONE
                    isAvatarLoaded = true
                    return false
                }

            })
            .timeout(60000)
            .placeholder(R.drawable.solid_white)
            .error(R.drawable.user_png)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.theirUserProfileImage)
    }
}