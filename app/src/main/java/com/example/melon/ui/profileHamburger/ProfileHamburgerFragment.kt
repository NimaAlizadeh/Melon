package com.example.melon.ui.profileHamburger

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.melon.databinding.FragmentProfileHamburgerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileHamburgerFragment : BottomSheetDialogFragment(){

    private lateinit var binding : FragmentProfileHamburgerBinding

    companion object{
        private lateinit var callBack: OnCallBackListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileHamburgerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //log out layout handling
            hamburgerMenuLogoutLayout.setOnClickListener {

                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes") { _, _ ->
                        callBack.onClickLogOut()
                    }
                    .setNegativeButton("No", null)
                    .setCancelable(false)
                    .create()
                    .show()

                this@ProfileHamburgerFragment.dismiss()
            }

            //open settings page
//            hamburgerMenuSettingsLayout.setOnClickListener {
//                callBack.onClickSettings()
//                this@ProfileHamburgerFragment.dismiss()
//            }

            //open edit profile page
            hamburgerMenuEditProfileLayout.setOnClickListener {
                callBack.onClickEditProfile()
                this@ProfileHamburgerFragment.dismiss()
            }
        }
    }

    interface OnCallBackListener{
        fun onClickLogOut()
        fun onClickSettings()
        fun onClickEditProfile()
    }

    fun setOnCallBackClickListener(tempCallBack: OnCallBackListener) {
        callBack = tempCallBack
    }
}