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
                    viewModel.getUserData(it.authtoken)
                    Constants.USER_TOKEN = it.authtoken
                }
                else{
                    val snackBar = Snackbar.make(view, "Something went wrong, Please try again...", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
            }

            viewModel.userDataResponse.observe(viewLifecycleOwner){ response ->

                Log.d("ssssssssssssssssssssssssssssssssssssss   ", response.success.toString())

                if(response.success){
                    if(response.user.followings.isNotEmpty()){
                        val tempFollowingList = response.user.followings
                        val tempFollowingIdList = ArrayList<String>()
                        tempFollowingList.forEach {
                            tempFollowingIdList.add(it.id)
                        }

                        MainActivity.followingIdList = tempFollowingIdList
                        lifecycle.coroutineScope.launch {
                            userData.setFollowingSet(tempFollowingIdList.toSet())
                        }
                    }

                    lifecycle.coroutineScope.launch {
                        userData.setUserToken(Constants.USER_TOKEN)
                    }

                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
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