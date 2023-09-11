package com.example.melon.ui.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.melon.R
import com.example.melon.databinding.ActivityMainBinding
import com.example.melon.ui.search.SearchFragment
import com.example.melon.ui.userProfile.ProfileFragment
import com.example.melon.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ProfileFragment.OnProfileFragmentListener
{
    private lateinit var binding: ActivityMainBinding

    companion object{
        var appPagePosition = Constants.MAIN_ACTIVITY
        var profilePosition = Constants.MAIN_ACTIVITY
        private lateinit var callBack: OnPermissionCallBackListener

        var followingsIdList = ArrayList<String>()
        var followersIdList = ArrayList<String>()
        var followingsRequestedIdList = ArrayList<String>()
        var followersRequestedIdList = ArrayList<String>()

        var myUserID = ""
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ProfileFragment().setOnProfileFragmentListener(this)


        binding.apply {
            mainBottomNavigation.selectedItemId = R.id.homeFragment
            //set bottomNavigation to be active in special fragments
            val navController = findNavController(R.id.fragmentContainerView)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if(destination.id == R.id.profileFragment || destination.id == R.id.searchFragment || destination.id == R.id.homeFragment ||
                        destination.id == R.id.theirProfileFragment){
                    mainBottomNavigation.visibility = View.VISIBLE
                    mainSimpleView1.visibility = View.VISIBLE
                }
                else{
                    mainBottomNavigation.visibility = View.GONE
                    mainSimpleView1.visibility = View.GONE
                }
            }
            mainBottomNavigation.setupWithNavController(navController)
            mainBottomNavigation.setOnItemReselectedListener {  }
        }
    }

    override fun onProfileFragmentLoaded() {
        binding.apply {
            mainBottomNavigation.visibility = View.GONE
            mainSimpleView1.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 101){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callBack.onPermissionGranted()
            }else{
                callBack.onPermissionNotGranted()
            }
        }
    }

    interface OnPermissionCallBackListener{
        fun onPermissionGranted()
        fun onPermissionNotGranted()
    }

    fun setOnPermissionCallBackClickListener(tempCallBack: OnPermissionCallBackListener) {
        callBack = tempCallBack
    }


    override fun onBackPressed() {
        val searchFragment = SearchFragment()

        if(appPagePosition == Constants.FRAGMENT_SEARCH && searchFragment.onBackPressed()){
            return
        }

        super.onBackPressed()
    }

}