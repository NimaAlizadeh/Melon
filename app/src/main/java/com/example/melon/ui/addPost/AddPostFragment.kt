package com.example.melon.ui.addPost

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.Glide
import com.example.melon.api.ApiServices
import com.example.melon.databinding.FragmentAddPostBinding
import com.example.melon.models.MultiSelectRecycler
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.AddPostAdapter
import com.example.melon.ui.profileHamburger.ProfileHamburgerFragment
import com.example.melon.utils.Constants
import com.example.melon.viewmodels.AddPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class AddPostFragment : Fragment(), MainActivity.OnPermissionCallBackListener, AddPostAdapter.PhotoListener {

    private lateinit var binding: FragmentAddPostBinding

    @Inject
    lateinit var adapter: AddPostAdapter

    private lateinit var imagesList: ArrayList<String>

    private lateinit var anim: AlphaAnimation
    private var path = ""

    @Inject
    lateinit var apiServices: ApiServices

    private lateinit var selectedImages: ArrayList<String>

    var isOnMultipleMode = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddPostBinding.inflate(layoutInflater, container, false)
        MainActivity.appPagePosition = Constants.FRAGMENT_ADD_POST
        MainActivity().setOnPermissionCallBackClickListener(this)
        adapter.setPhotoListener(this)


        //fade animation
        anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 700

        //initiate arrays
        selectedImages = ArrayList()
        imagesList = ArrayList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addPostSelectedCount.text = adapter.selectedItems.size.toString()

            //we are gonna make the mode to be false from the multiple mode button
//            addPostMultipleModeButton.setOnClickListener {
//                if(isOnMultipleMode){
//                    isOnMultipleMode = false
//                    addPostSelectedCount.text = "0"
//                    addPostSelectedCount.visibility = View.GONE
//                    adapter.selectedItems.clear()
//                }else{
//                    isOnMultipleMode = true
//                    addPostSelectedCount.text = "1"
//                    addPostSelectedCount.visibility = View.VISIBLE
//                }
//            }


            //refresh listener
            addPostRefresh.setOnRefreshListener {
                adapter.selectedItems.clear()
                loadImages()
                addPostRefresh.isRefreshing = false
                addPostSelectedCount.text = adapter.selectedItems.size.toString()
            }


            //onclick listeners
            addPostDiscardButton.setOnClickListener {
                findNavController().popBackStack()
            }

            //forward button clicked ---> get the files then do the api request
            addPostForwardButton.setOnClickListener {
                if (adapter.selectedItems.size >= 1)
                    findNavController().navigate(AddPostFragmentDirections.actionAddPostFragmentToConfirmAddPostFragment(adapter.selectedItems.toTypedArray()))
                else
                    Toast.makeText(requireContext(), "Please select an item", Toast.LENGTH_SHORT).show()
            }

            //check permission for user images
            if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                showPermissionDialog()
            }
            else{
                loadImages()
            }


        }
    }

    private fun showPermissionDialog(){
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle("Permission")
            .setMessage("Storage permission is necessary")
            .setPositiveButton("Ok") { _, _ ->
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 101)
                else
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101)
            }
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .create()
            .show()
    }

    @SuppressLint("Recycle")
    private fun loadImages(){
        imagesList.clear()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, "$orderBy DESC")
        val columnIndexData = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

//        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)
//        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
//
//        val cursor = requireContext().contentResolver.query(uri, projection, null, null, sortOrder)
//
//        cursor?.use {
//            val columnIndexId = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
////            val columnIndexDisplayName = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
//
//            while (it.moveToNext()) {
//                val imageId = it.getLong(columnIndexId)
////                val displayName = it.getString(columnIndexDisplayName)
//
//                val imageUri = Uri.withAppendedPath(uri, imageId.toString())
//                val imagePath = imageUri.toString()
//
//                // Display the image using the imagePath in your UI
//                // You can load the image into an ImageView, for example
//
//                imagesList.add(imagePath)
//            }
//        }

        while (cursor.moveToNext())
            imagesList.add(cursor.getString(columnIndexData))


        binding.apply {
            adapter.differ.submitList(imagesList)
            addPostFragmentRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
            addPostFragmentRecycler.setHasFixedSize(true)
            addPostFragmentRecycler.adapter = adapter

            if(adapter.selectedItems.size == 0) {
                Glide.with(requireContext()).load(imagesList[0]).into(addPostImageView)
                addPostImageView.startAnimation(anim)
                this@AddPostFragment.path = imagesList[0]
            }else{
                Glide.with(requireContext()).load(adapter.selectedItems[adapter.selectedItems.size -1]).into(addPostImageView)
                addPostImageView.startAnimation(anim)
                this@AddPostFragment.path = adapter.selectedItems[adapter.selectedItems.size -1]
            }
        }
    }

    override fun onPermissionGranted() {
        loadImages()
    }

    override fun onPermissionNotGranted() {

    }

    override fun onPhotoClicked(path: String, isSelected: Boolean) {
        binding.apply {
            if (!adapter.isMaximum || (adapter.selectedItems.size == 4 && isSelected))
            {
                Glide.with(requireContext()).load(path).into(addPostImageView)
                this@AddPostFragment.path = path
                addPostImageView.startAnimation(anim)
                if(adapter.selectedItems.size == 5)
                    adapter.isMaximum = true
            }
            addPostSelectedCount.text = adapter.selectedItems.size.toString()
        }
    }

    override fun onPhotoLongClicked(path: String, isSelected: Boolean) {
        binding.apply {

        }
    }

}