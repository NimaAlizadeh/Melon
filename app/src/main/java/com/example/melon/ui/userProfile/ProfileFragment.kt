package com.example.melon.ui.userProfile

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
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
import com.example.melon.models.AddFollowerModel
import com.example.melon.models.Post
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.ProfilePostsAdapter
import com.example.melon.ui.profileHamburger.ProfileHamburgerFragment
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.example.melon.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() , ProfileHamburgerFragment.OnCallBackListener{

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var adapter: ProfilePostsAdapter

    @Inject
    lateinit var dataStore: StoreUserData

    private var userId = ""
    private var followUserId = ""
    private var theirUserId = ""

    private val args by navArgs<ProfileFragmentArgs>()

    private var isAvatarLoaded = false

    @Inject
    lateinit var userData: StoreUserData

    private var posts: Array<Post> = emptyArray()

    companion object{
        private lateinit var onProfileFragmentListener: OnProfileFragmentListener
    }

    interface OnProfileFragmentListener{
        fun onProfileFragmentLoaded()
    }

    fun setOnProfileFragmentListener(listener: OnProfileFragmentListener){
        onProfileFragmentListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        ProfileHamburgerFragment().setOnCallBackClickListener(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(!isAvatarLoaded)
            binding.userProfileProgressbar.visibility = View.VISIBLE
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            if(MainActivity.appPagePosition != Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT)
                MainActivity.appPagePosition = Constants.GO_TO_MY_USER_PROFILE_FRAGMENT



            if(MainActivity.appPagePosition == Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT){
                loadDataWhenIsTheirProfile(args.searchingUserID)
                onProfileFragmentListener.onProfileFragmentLoaded()
            }
            else if(MainActivity.appPagePosition == Constants.GO_TO_MY_USER_PROFILE_FRAGMENT){
                userProfileFollowButton.visibility = View.GONE
                userProfileUnFollowButton.visibility = View.GONE
                followProgressbar.visibility = View.GONE
                loadDataWhenIsMyProfile()
            }

            //refreshing the page
            profileFragmentSwipeRefresh.setOnRefreshListener {
                userProfileProgressbar.visibility = View.VISIBLE

                if(MainActivity.appPagePosition == Constants.GO_TO_MY_USER_PROFILE_FRAGMENT){
                    loadDataWhenIsMyProfile()
                }else if(MainActivity.appPagePosition == Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT)
                    loadDataWhenIsTheirProfile(args.searchingUserID)

                profileFragmentSwipeRefresh.isRefreshing = false
            }

            userProfileImage.setOnClickListener {
                if(MainActivity.appPagePosition == Constants.GO_TO_MY_USER_PROFILE_FRAGMENT)
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangeAvatarFragment())
            }

            //back button onclick
            userProfileBackButton.setOnClickListener {
                findNavController().popBackStack()
            }

            //follow button on click listener
            userProfileFollowButton.setOnClickListener {
                viewModel.addFollower(Constants.USER_TOKEN, AddFollowerModel(followUserId))
                userProfileUnFollowButton.visibility = View.INVISIBLE
                userProfileFollowButton.visibility = View.INVISIBLE
            }

            // unfollow button on click listener
            userProfileUnFollowButton.setOnClickListener {

            }

            userProfileHamburgerMenuButton.setOnClickListener {
                ProfileHamburgerFragment().show(parentFragmentManager, ProfileHamburgerFragment().tag)
            }

            //response of trying to follow some one
            viewModel.followResponse.observe(viewLifecycleOwner){
                if(it.equals("Follow request sent successfully") || it.equals("User followed successfully")){
                    userProfileUnFollowButton.visibility = View.VISIBLE
                    userProfileFollowersText.text = (userProfileFollowersText.text.toString().toInt() + 1).toString()

                    MainActivity.followingIdList.add(theirUserId)

                    lifecycle.coroutineScope.launch {
                        userData.setFollowingSet(MainActivity.followingIdList.toSet())
                    }
                }else{
                    userProfileFollowButton.visibility = View.VISIBLE
                }
            }

            viewModel.followLoading.observe(viewLifecycleOwner){
                if(it)
                    followProgressbar.visibility = View.VISIBLE
                else
                    followProgressbar.visibility = View.INVISIBLE
            }


            viewModel.allPostsResponseList.observe(viewLifecycleOwner){
                if(MainActivity.appPagePosition == Constants.GO_TO_MY_USER_PROFILE_FRAGMENT){
                    if(it != null)
                    {
                        loadDataToRecycler(it.posts)
                        posts = it.posts.toTypedArray()
                        Toast.makeText(requireContext(), "it's not null", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        loadDataToRecycler(emptyList())
                        Toast.makeText(requireContext(), "it's null", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            viewModel.userDataResponse.observe(viewLifecycleOwner){
                if(it.success){

//                    MainActivity.followRequestList = it.user.followerRequests
                    MainActivity.followingList = it.user.followings

                    MainActivity.followingIdList.clear()
                    MainActivity.followingList.forEach { model ->
                        MainActivity.followingIdList.add(model.id)
                    }

                    lifecycle.coroutineScope.launch {
                        userData.setFollowingSet(MainActivity.followingIdList.toSet())
                    }
//                    MainActivity.followRequestIdList.clear()
//                    MainActivity.followRequestList.forEach {model ->
//                        MainActivity.followRequestIdList.add(model.id)
//                    }

                    userId = it.user._id
                    loadDataIntoViews(it.user.bio, it.user.username, it.user.followers.size.toString(), it.user.followings.size.toString())
                    loadProfileAvatar(userId)
                }
            }

            viewModel.userDataResponseWithId.observe(viewLifecycleOwner){
                if(it.success){

                    followUserId = it.user.id
                    theirUserId = it.user.id
                    userId = it.user.id

                    loadDataIntoViews(bio = it.user.bio, username = it.user.username, followers = it.user.followers.toString(), followings = it.user.followings.toString())

                    loadProfileAvatar(it.user.id)

                    if(!it.user.private){
                        loadDataToRecycler(it.user.posts)
                        userProfileNoPostImage.setBackgroundResource(R.drawable.baseline_camera_24)
                        userProfileNoPostText.text = "No Posts Yet"
                        loadDataIntoViews(bio = it.user.bio, username = it.user.username, followers = it.user.followers.toString(), followings = it.user.followings.toString())

                    }else{
                        loadDataIntoViews(bio = it.user.bio, username = it.user.username, followers = it.user.followers.toString(), followings = it.user.followings.toString())

                        userProfilePostsText.text = it.user.posts.size.toString()
                        userProfileNoPostImage.setBackgroundResource(R.drawable.baseline_lock_24)
                        userProfileNoPostText.text = "This Account is private"
                    }

                    if(MainActivity.followingIdList.contains(theirUserId)){
                        userProfileUnFollowButton.visibility = View.VISIBLE
                        userProfileFollowButton.visibility = View.INVISIBLE
                    }
                    else{
                        userProfileFollowButton.visibility = View.VISIBLE
                        userProfileUnFollowButton.visibility = View.INVISIBLE
                    }
                }
            }


            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    profileFragmentLoadingProgressbar.visibility = View.VISIBLE
                    profileFragmentWholeLayout.visibility = View.GONE
                }
                else
                {
                    profileFragmentLoadingProgressbar.visibility = View.GONE
                    profileFragmentWholeLayout.visibility = View.VISIBLE
                }
            }


            // on click listeners from adapter
            adapter.setOnItemCLickListener { position, s ->
                if(s == "onClick"){
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToShowPostFragment(position, userId))
                }
            }
        }
    }

    override fun onClickLogOut() {
        lifecycle.coroutineScope.launch {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.progress_dialog_layout)
            dialog.setCancelable(false)
            dialog.show()
            dataStore.setUserToken("out")
            delay(2000)
            dialog.dismiss()
            withContext(Dispatchers.Main){
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFirstFragment())
            }
        }
    }

    private fun loadDataWhenIsTheirProfile(userId: String){
        binding.apply {

            viewModel.loadUserDataWithId(userId, Constants.USER_TOKEN)
            userProfileHamburgerMenuButton.visibility = View.GONE
            userProfileFollowButton.visibility = View.VISIBLE
        }
    }

    private fun loadDataWhenIsMyProfile(){
        binding.apply {
            viewModel.loadPosts(Constants.USER_TOKEN)

            userProfileHamburgerMenuButton.visibility = View.VISIBLE
            userProfileFollowButton.visibility = View.GONE
        }
    }

    private fun loadDataIntoViews(bio: String, username: String, followers: String, followings: String){
        binding.apply {

            if(bio.isEmpty())
                userProfileBio.visibility = View.GONE
            else
                userProfileBio.visibility = View.VISIBLE

            userProfileBio.text = bio
            userProfileUserName.text = username
            userProfileFollowersText.text = followers
            userProfileFollowingText.text = followings
        }
    }

    private fun loadDataToRecycler(it: List<Post>){

        binding.apply {

            if(it.isNotEmpty()){
                userProfileNoPostText.visibility = View.GONE
                userProfileNoPostImage.visibility = View.GONE
                userProfilePostsRecycler.visibility = View.VISIBLE
                userProfilePostsText.text = it.size.toString()

                adapter.differ.submitList(it)
                userProfilePostsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                userProfilePostsRecycler.setHasFixedSize(true)
                userProfilePostsRecycler.adapter = adapter
            }
            else{
                userProfileNoPostText.visibility = View.VISIBLE
                userProfileNoPostImage.visibility = View.VISIBLE
                userProfileNoPostImage.setBackgroundResource(R.drawable.baseline_camera_24)
                userProfileNoPostText.text = "No Posts Yet"
                userProfilePostsRecycler.visibility = View.INVISIBLE
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
                    binding.userProfileProgressbar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    binding.userProfileProgressbar.visibility = View.GONE
                    isAvatarLoaded = true
                    return false
                }

            })
            .timeout(60000)
            .placeholder(R.drawable.solid_white)
            .error(R.drawable.baseline_person_24)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.userProfileImage)
    }

    override fun onClickSettings() {


    }

    override fun onClickEditProfile() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
    }
}