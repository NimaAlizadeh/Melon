package com.example.melon.ui.signUpUser

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.melon.R
import com.example.melon.databinding.FragmentSignupBinding
import com.example.melon.models.UserModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.example.melon.viewmodels.SignupViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    private lateinit var gender: String
    private lateinit var birthDay: String

    private val viewModel: SignupViewModel by viewModels()

    @Inject
    lateinit var userData: StoreUserData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.appPagePosition = Constants.FRAGMENT_SIGNUP

        binding.apply {

            //choose a default value for radio buttons
            signupFragmentMaleRadio.isChecked = true
            gender = "male"

            //radio group listener
            genderInput.setOnCheckedChangeListener { _, i ->
                when(i){
                    R.id.signup_fragment_male_radio -> gender = "male"
                    R.id.signup_fragment_female_radio -> gender = "female"
                    R.id.signup_fragment_other_radio -> gender = "other"
                }
            }

            //handling the birthday edittext with date picker
            signupFragmentBirthdayEdt.setOnClickListener {
                val picker = DatePickerDialog(requireContext())
                picker.setOnDateSetListener { datePicker, _, _, _ ->
                    val temp = datePicker.year.toString() + " / " + (datePicker.month+1).toString() + " / " + datePicker.dayOfMonth.toString()
                    signupFragmentBirthdayEdt.setText(temp)
                    birthDay = temp
                }
                picker.show()
            }

            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    signupFragmentSignupButton.visibility = View.GONE
                    signupFragmentProgressBar.visibility = View.VISIBLE
                }else{
                    signupFragmentSignupButton.visibility = View.VISIBLE
                    signupFragmentProgressBar.visibility = View.GONE
                }
            }

            signupFragmentSignupButton.setOnClickListener {

                if(signupFragmentUsernameEdt.text!!.isEmpty() || signupFragmentEmailEdt.text!!.isEmpty() ||
                    signupFragmentPasswordEdt.text!!.isEmpty() || signupFragmentBirthdayEdt.text!!.isEmpty()){
                    val snackBar = Snackbar.make(view, "Fill in the blanks", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
                else{
                    val user = UserModel()
                    user.username = signupFragmentUsernameEdt.text.toString()
                    user.email = signupFragmentEmailEdt.text.toString()
                    user.password = signupFragmentPasswordEdt.text.toString()
                    user.birthday = birthDay
                    user.gender = gender

                    viewModel.signUpUser(user)
                }
            }

            viewModel.signUpResponse.observe(viewLifecycleOwner){
                val snackBar = Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT)
                snackBar.show()
                if(it.success){
                    findNavController().popBackStack()
                }
            }

        }
    }
}