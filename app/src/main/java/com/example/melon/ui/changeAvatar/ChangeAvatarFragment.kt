package com.example.melon.ui.changeAvatar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.melon.R
import com.example.melon.databinding.FragmentChangeAvatarBinding
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.AddPostAdapter
import com.example.melon.utils.Constants
import com.example.melon.viewmodels.ChangeAvatarViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ChangeAvatarFragment : Fragment(), MainActivity.OnPermissionCallBackListener, AddPostAdapter.PhotoListener {

    private lateinit var binding: FragmentChangeAvatarBinding

    @Inject
    lateinit var adapter: AddPostAdapter

    private lateinit var imagesList: ArrayList<String>

    private lateinit var anim: AlphaAnimation
    private var path = ""

    private lateinit var selectedImages: ArrayList<String>

    private val viewModel: ChangeAvatarViewModel by viewModels ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChangeAvatarBinding.inflate(layoutInflater, container, false)
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
            //init variables
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.progress_dialog_layout)
            dialog.setCancelable(false)


            //refresh listener
            changeAvatarRefresh.setOnRefreshListener {
                loadImages()
                changeAvatarRefresh.isRefreshing = false
            }

            //onclick listeners
            changeAvatarDiscardButton.setOnClickListener {
                findNavController().popBackStack()
            }

            //forward button clicked ---> get the files then do the api request
            changeAvatarCheckButton.setOnClickListener {
                val originalFile = File(path)
                val fileUri = Uri.parse(path)
                val filePart: RequestBody = RequestBody.create(requireActivity().contentResolver.getType(fileUri)
                    ?.toMediaTypeOrNull(), originalFile)

                val file: MultipartBody.Part = MultipartBody.Part.createFormData("avatar", originalFile.name, filePart)

                viewModel.doChangeAvatar(Constants.USER_TOKEN, file)
                changeAvatarCheckButton.isEnabled = false
                Toast.makeText(requireContext(), "Changing avatar...", Toast.LENGTH_SHORT).show()
                dialog.show()
            }

            viewModel.changeAvatarResponse.observe(viewLifecycleOwner){
                if(it.isSuccessful){
                    Toast.makeText(requireContext(), it.body()?.message, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }else{
                    Toast.makeText(requireContext(), it.message(), Toast.LENGTH_SHORT).show()
                }
                changeAvatarCheckButton.isEnabled = true
                dialog.dismiss()
            }

            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    changeAvatarToolbarPostingProgressbar.visibility = View.VISIBLE
                    changeAvatarCheckButton.visibility = View.GONE
                }else{
                    changeAvatarToolbarPostingProgressbar.visibility = View.GONE
                    changeAvatarCheckButton.visibility = View.VISIBLE
                }
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

        while (cursor.moveToNext())
            imagesList.add(cursor.getString(columnIndexData))


        binding.apply {
            adapter.differ.submitList(imagesList)
            changeAvatarFragmentRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
            changeAvatarFragmentRecycler.setHasFixedSize(true)
            changeAvatarFragmentRecycler.adapter = adapter

            Glide.with(requireContext()).load(imagesList[0]).into(changeAvatarImageView)
            changeAvatarImageView.startAnimation(anim)
            this@ChangeAvatarFragment.path = imagesList[0]
        }
    }

    override fun onPermissionGranted() {
        loadImages()
    }

    override fun onPermissionNotGranted() {

    }

    override fun onPhotoClicked(path: String, isSelected: Boolean) {
        binding.apply {
            Glide.with(requireContext()).load(path).into(changeAvatarImageView)
            this@ChangeAvatarFragment.path = path
            changeAvatarImageView.startAnimation(anim)

        }
    }

    override fun onPhotoLongClicked(path: String, isSelected: Boolean) {

    }
}