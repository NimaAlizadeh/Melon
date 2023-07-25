package com.example.melon.ui.firstFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.melon.databinding.FragmentFirstBinding
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.loginUser.LoginFragment
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.example.melon.viewmodels.FirstFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    @Inject
    lateinit var userData: StoreUserData

    private val viewModel: FirstFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.appPagePosition = Constants.FRAGMENT_FIRST

        binding.apply {
            firstFragmentLoginButton.setOnClickListener {
                findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToLoginFragment())
            }

            firstFragmentSignUpButton.setOnClickListener {
                findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToSignupFragment())
            }

            lifecycle.coroutineScope.launch {
                userData.getFollowings().collect{
                    if(it.isNotEmpty()){
                        MainActivity.followingIdList = ArrayList(it.toList())
                    }
                }
            }

            //check user token to go to next fragment
            lifecycle.coroutineScope.launch {
                userData.getUserToken().collect{
                    withContext(Dispatchers.Main){
                        if(it.isNotEmpty()  && it != "out"){
                            Constants.USER_TOKEN = it
                            findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToHomeFragment())
                        }
                    }
                }
            }
        }
    }
}