package com.example.melon.ui.editProfile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.melon.databinding.FragmentEditProfileBinding
import com.example.melon.models.EditProfileModel
import com.example.melon.models.User
import com.example.melon.viewmodels.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.melon.utils.Constants

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    private val viewModel: EditProfileViewModel by viewModels()

    @Inject
    lateinit var userResponse: User

    private lateinit var gender: String
    private lateinit var birthDay: String
    private lateinit var name: String
    private lateinit var editProfileModel: EditProfileModel
    private var isPrivate = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        viewModel.loadUserData()
        editProfileModel = EditProfileModel()
        gender = ""
        birthDay = ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editProfileFragmentToolbarBackButton.setOnClickListener {
                findNavController().popBackStack()
            }

            //change password check button
            editProfileFragmentChangePasswordBox.setOnClickListener {
                if(editProfileFragmentChangePasswordBox.isChecked){
                    editProfileFragmentOldPasswordEdt.isEnabled = true
                    editProfileFragmentNewPasswordEdt.isEnabled = true
                    editProfileFragmentOldPasswordEdt.alpha = 1f
                    editProfileFragmentNewPasswordEdt.alpha = 1f
                }else{
                    editProfileFragmentOldPasswordEdt.text?.clear()
                    editProfileFragmentNewPasswordEdt.text?.clear()
                    editProfileFragmentOldPasswordEdt.isEnabled = false
                    editProfileFragmentNewPasswordEdt.isEnabled = false
                    editProfileFragmentOldPasswordEdt.alpha = 0.5f
                    editProfileFragmentNewPasswordEdt.alpha = 0.5f
                }
            }

            //switch button handling
            editProfileFragmentAccountPrivacySwitch.setOnCheckedChangeListener { _, _ ->

                isPrivate = editProfileFragmentAccountPrivacySwitch.isChecked
                if(isPrivate)
                    editProfileFragmentAccountPrivacyText.text = "Private"
                else
                    editProfileFragmentAccountPrivacyText.text = "Public"

            }

            viewModel.userDataResponse.observe(viewLifecycleOwner){
                if(it.isSuccessful){
                    //get the response data to check with user input later
                    val response = it.body()?.user!!
                    userResponse.email = response.email
                    userResponse.gender = response.gender
                    userResponse.birthday = response.birthday
                    userResponse.username = response.username
                    userResponse.name = response.name

                    //put user data in views
                    editProfileFragmentEmailEdt.setText(response.email)
                    editProfileFragmentBirthdayEdt.setText(response.birthday)
                    editProfileFragmentUsernameEdt.setText(response.username)
                    editProfileFragmentBioEdt.setText(response.bio)
                    editProfileFragmentNameEdt.setText(response.name)
                    editProfileFragmentAccountPrivacySwitch.isChecked = response.private
                    isPrivate = response.private
                    if(isPrivate)
                        editProfileFragmentAccountPrivacyText.text = "Private"
                    else
                        editProfileFragmentAccountPrivacyText.text = "Public"
                }else{
                    Toast.makeText(requireContext(), it.body()?.success.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            //handling the birthday edittext with date picker
            editProfileFragmentBirthdayEdt.setOnClickListener {
                val picker = DatePickerDialog(requireContext())
                picker.setOnDateSetListener { datePicker, _, _, _ ->
                    val temp = datePicker.year.toString() + " / " + (datePicker.month+1).toString() + " / " + datePicker.dayOfMonth.toString()
                    editProfileFragmentBirthdayEdt.setText(temp)
                    birthDay = temp
                }
                picker.show()
            }

            //loading progressbar handle
            viewModel.userDataLoading.observe(viewLifecycleOwner){
                if(it){
                    editProfileFragmentLoadingProgressbar.visibility = View.VISIBLE
                    editProfileFragmentScrollLayout.visibility = View.GONE
                    editProfileFragmentToolbarCheckButton.isEnabled = false
                }else{
                    editProfileFragmentLoadingProgressbar.visibility = View.GONE
                    editProfileFragmentScrollLayout.visibility = View.VISIBLE
                    editProfileFragmentToolbarCheckButton.isEnabled = true
                }
            }

            //check button for when user is done
            editProfileFragmentToolbarCheckButton.setOnClickListener {
                editProfileModel.bio = editProfileFragmentBioEdt.text.toString()
                editProfileModel.private = isPrivate

                if(editProfileFragmentUsernameEdt.text.toString() != userResponse.username)
                    editProfileModel.username = editProfileFragmentUsernameEdt.text.toString()
                else editProfileModel.username = ""

                if(editProfileFragmentNameEdt.text.toString() != userResponse.name)
                    editProfileModel.name = editProfileFragmentNameEdt.text.toString()
                else editProfileModel.name = ""

                if(editProfileFragmentEmailEdt.text.toString() != userResponse.email)
                    editProfileModel.email = editProfileFragmentEmailEdt.text.toString()
                else editProfileModel.email = ""

                if(editProfileFragmentBirthdayEdt.text.toString() != userResponse.birthday)
                    editProfileModel.birthday = editProfileFragmentBirthdayEdt.text.toString()
                else editProfileModel.birthday = ""

                if(editProfileFragmentChangePasswordBox.isChecked)
                {
                    editProfileModel.oldPassword = editProfileFragmentOldPasswordEdt.text.toString()
                    editProfileModel.newPassword = editProfileFragmentNewPasswordEdt.text.toString()
                }

                viewModel.doEditUserProfile(editProfileModel)
            }

            viewModel.editLoading.observe(viewLifecycleOwner){
                if(it){
                    editProfileFragmentEditingProgressbar.visibility = View.VISIBLE
                    editProfileFragmentToolbarCheckButton.visibility = View.GONE
                    editProfileFragmentToolbarBackButton.isEnabled = false
                }else{
                    editProfileFragmentEditingProgressbar.visibility = View.GONE
                    editProfileFragmentToolbarCheckButton.visibility = View.VISIBLE
                    editProfileFragmentToolbarBackButton.isEnabled = true
                }
            }

            viewModel.editProfileResponse.observe(viewLifecycleOwner){
                if(it.isSuccessful){
                    if(it.body()!!.success)
                        findNavController().popBackStack()

                    Toast.makeText(requireContext(), it.body()!!.message, Toast.LENGTH_SHORT).show()
                }else
                    Toast.makeText(requireContext(), it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}