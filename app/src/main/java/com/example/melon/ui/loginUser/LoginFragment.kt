package com.example.melon.ui.loginUser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.melon.databinding.FragmentLoginBinding
import com.example.melon.models.LoginModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.profileHamburger.ProfileHamburgerFragment
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.example.melon.utils.getFollowIds
import com.example.melon.utils.getIds
import com.example.melon.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var userData: StoreUserData


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.appPagePosition = Constants.FRAGMENT_LOGIN

        binding.apply {

            //login button listener
            loginFragmentLoginButton.setOnClickListener {

                if(loginFragmentPasswordEdt.text!!.isEmpty() || loginFragmentUsernameEdt.text!!.isEmpty()){
                    val snackBar = Snackbar.make(view, "Fill the blanks", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
                else{
                    val loginModel = LoginModel()
                    loginModel.password = loginFragmentPasswordEdt.text.toString()
                    loginModel.username_or_email = loginFragmentUsernameEdt.text.toString()

                    viewModel.loginUser(loginModel)
                }
            }

            viewModel.loginResponse.observe(viewLifecycleOwner){
                //go to home page
                if(it.success){
                    viewModel.getUserData()
                    Constants.USER_TOKEN = it.authtoken
                }
                else{
                    val snackBar = Snackbar.make(view, "Something went wrong, Please try again...", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
            }

            viewModel.userDataResponse.observe(viewLifecycleOwner){ response ->

                if(response.success){
                    MainActivity.myUserID = response.user.id


                    val tempFollowings = response.user.followings.getIds()
                    val tempFollowers = response.user.followers.getIds()

                    MainActivity.followingsIdList = tempFollowings
                    MainActivity.followersIdList = tempFollowers

                    lifecycle.coroutineScope.launch {
                        userData.setFollowingsCollection(tempFollowings.toSet())
                        userData.setFollowersCollection(tempFollowers.toSet())
                        userData.setUserId(response.user.id)
                    }
                }

                viewModel.getRequestedData()
            }

            viewModel.userRequestedData.observe(viewLifecycleOwner){

//                if(it.followerRequests != null && it.followingRequests != null){
                    val tempFollowerRequested = it.followerRequests.getIds()
                    val tempFollowingRequested = it.followingRequests.getIds()
                    lifecycle.coroutineScope.launch {
                        userData.setFollowersRequestedCollection(tempFollowerRequested.toSet())
                        userData.setFollowingRequestedCollection(tempFollowingRequested.toSet())
                    }

                    MainActivity.followersRequestedIdList = tempFollowerRequested
                    MainActivity.followingsRequestedIdList = tempFollowingRequested
//                }



                // we have to get all data we need and then lead the user inside the application
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }

            //loading progressbar handling
            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    loginFragmentLoginButton.visibility = View.GONE
                    loginFragmentProgressBar.visibility = View.VISIBLE
                }else{
                    loginFragmentLoginButton.visibility = View.VISIBLE
                    loginFragmentProgressBar.visibility = View.GONE
                }
            }
        }
    }
}