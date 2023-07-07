package com.example.melon.ui.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.melon.R
import com.example.melon.databinding.ActivityMainBinding
import com.example.melon.ui.profileHamburger.ProfileHamburgerFragment
import com.example.melon.ui.userProfile.ProfileFragment
import com.example.melon.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    companion object{
        var appPagePosition = Constants.MAIN_ACTIVITY
        private lateinit var callBack: OnPermissionCallBackListener
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.apply {
            mainBottomNavigation.selectedItemId = R.id.homeFragment
//            ProfileFragment().onCreateAnimation()
            //set bottomNavigation to be active in special fragments
            val navController = findNavController(R.id.fragmentContainerView)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if(destination.id == R.id.profileFragment || destination.id == R.id.homeFragment ||
                        destination.id == R.id.searchFragment){
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


//            mainBottomNavigation.setOnItemSelectedListener { item ->
//                if (item.itemId == mainBottomNavigation.selectedItemId) {
//                    // Do nothing if the clicked item is already selected
//                    return@setOnItemSelectedListener false
//                }
//
//                // Handle navigation to the selected item
////                when (item.itemId) {
////                    R.id.menu_item1 -> {
////                        // Handle navigation to item 1
////                        // For example: navigate to a specific fragment
////                        navController.navigate(R.id.destination_fragment1)
////                    }
////                    R.id.menu_item2 -> {
////                        // Handle navigation to item 2
////                        // For example: navigate to a specific fragment
////                        navController.navigate(R.id.destination_fragment2)
////                    }
////                    // Add more cases for other menu items
////                }
//
//                true // Return true to indicate that the selection has been handled
//            }
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
}