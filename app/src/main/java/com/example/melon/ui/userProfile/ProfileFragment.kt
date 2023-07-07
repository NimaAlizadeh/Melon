package com.example.melon.ui.userProfile

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        ProfileHamburgerFragment().setOnCallBackClickListener(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.userProfileProgressbar.visibility = View.VISIBLE
        loadProfileAvatar()
    }

    private fun loadProfileAvatar()
    {
        //load user profile avatar with glide
        val headers = Headers {
            val headersMap = HashMap<String, String>()
            headersMap["auth-token"] = Constants.USER_TOKEN
            headersMap
        }
        val glideUrl = GlideUrl(Constants.BASE_URL + "auth/avatars", headers)

        Glide.with(requireContext())
            .load(glideUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    binding.userProfileProgressbar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    binding.userProfileProgressbar.visibility = View.GONE
                    return false
                }

            })
            .placeholder(R.drawable.solid_white)
            .error(R.drawable.baseline_person_24)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.userProfileImage)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            if(MainActivity.appPagePosition != Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT)
                MainActivity.appPagePosition = Constants.GO_TO_MY_USER_PROFILE_FRAGMENT

            if(MainActivity.appPagePosition == Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT){
                userProfileHamburgerMenuButton.visibility = View.GONE
                userProfileFollowButton.visibility = View.VISIBLE
            }
            else if(MainActivity.appPagePosition == Constants.GO_TO_MY_USER_PROFILE_FRAGMENT){
                viewModel.loadPosts(Constants.USER_TOKEN)
                userProfileHamburgerMenuButton.visibility = View.VISIBLE
                userProfileFollowButton.visibility = View.GONE
            }

            profileFragmentSwipeRefresh.setOnRefreshListener {
                viewModel.loadPosts(Constants.USER_TOKEN)
                loadProfileAvatar()
                profileFragmentSwipeRefresh.isRefreshing = false
            }

            userProfileImage.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangeAvatarFragment())
            }


            //back button onclick
            userProfileBackButton.setOnClickListener {
                findNavController().popBackStack()
            }

            userProfileHamburgerMenuButton.setOnClickListener {
                ProfileHamburgerFragment().show(parentFragmentManager, ProfileHamburgerFragment().tag)
            }


            viewModel.allPostsResponseList.observe(viewLifecycleOwner){
                userProfileNoPostText.visibility = View.GONE
                userProfileNoPostImage.visibility = View.GONE

                adapter.differ.submitList(it)
                userProfilePostsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                userProfilePostsRecycler.setHasFixedSize(true)
                userProfilePostsRecycler.adapter = adapter
            }

            viewModel.userDataResponse.observe(viewLifecycleOwner){
                if(it.success){
                    if(it.user.bio == "")
                        userProfileBio.visibility = View.GONE
                    else
                        userProfileBio.visibility = View.VISIBLE

                    userProfileBio.text = it.user.bio
                    userProfileUserName.text = it.user.username
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


//            //add follower onClick
//            profileUserFollowCheck.setOnClickListener {
//                profileUserFollowCheck.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.check))
//            }
        }
    }

    override fun onClickLogOut() {
        lifecycle.coroutineScope.launch {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.progress_dialog_layout)
            dialog.show()
            dataStore.setUserToken("out")
            delay(2000)
            dialog.dismiss()
            withContext(Dispatchers.Main){
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFirstFragment())
            }
        }
    }

    override fun onClickSettings() {

    }

    override fun onClickEditProfile() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
    }
}