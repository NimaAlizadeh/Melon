package com.example.melon.ui.confirmAddPost

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.melon.R
import com.example.melon.databinding.FragmentConfirmAddPostBinding
import com.example.melon.ui.adapters.ConfirmAddPostAdapter
import com.example.melon.viewmodels.AddPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ConfirmAddPostFragment : Fragment() {

    private lateinit var binding: FragmentConfirmAddPostBinding
    private val viewModel: AddPostViewModel by viewModels()

    private var path = arrayListOf<String>()

    private val pagerSnapHelper: PagerSnapHelper by lazy { PagerSnapHelper() }

    private val args : ConfirmAddPostFragmentArgs by navArgs()

    @Inject
    lateinit var adapter: ConfirmAddPostAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConfirmAddPostBinding.inflate(layoutInflater, container, false)
        args.path.map {
            path.add(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            //back button on click listener
            confirmAddPostFragmentToolbarBackButton.setOnClickListener {
                findNavController().popBackStack()
            }

            //set posts on picture's recycler
            adapter.differ.submitList(path)
            confirmAddPostFragmentPicsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            confirmAddPostFragmentPicsRecycler.adapter = adapter

            //indicator for recycler
            pagerSnapHelper.attachToRecyclerView(confirmAddPostFragmentPicsRecycler)
            confirmAddPostFragmentPicsIndicator.attachToRecyclerView(confirmAddPostFragmentPicsRecycler, pagerSnapHelper)


            confirmAddPostFragmentToolbarCheckButton.setOnClickListener {
                val images: ArrayList<MultipartBody.Part> = arrayListOf()
                path.forEach {
                    val originalFile = File(it)
                    val fileUri = Uri.parse(it)
                    val filePart: RequestBody = RequestBody.create(requireActivity().contentResolver.getType(fileUri)?.toMediaTypeOrNull(), originalFile)

                    val file: MultipartBody.Part = MultipartBody.Part.createFormData("images", originalFile.name, filePart)
                    images.add(file)
                }

                val date = Date()
                val day = DateFormat.format("dd", date) as String
                val monthName = DateFormat.format("MMMM", date) as String
                val year = DateFormat.format("yyyy", date) as String


                val descriptionPart = RequestBody.create(MultipartBody.FORM, confirmAddPostFragmentCaptionEdt.text.toString())
                val timePart = RequestBody.create(MultipartBody.FORM, "$year $monthName $day")



                confirmAddPostFragmentToolbarBackButton.isEnabled = false
                confirmAddPostFragmentCaptionEdt.isEnabled = false
                viewModel.doAddPost(timePart, descriptionPart, images)
                Toast.makeText(requireContext(), "Posting...", Toast.LENGTH_SHORT).show()
            }

            viewModel.addPostResponse.observe(viewLifecycleOwner){
                if(it.code() == 200){
                    Toast.makeText(requireContext(), it.body()?.message, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(ConfirmAddPostFragmentDirections.actionConfirmAddPostFragmentToHomeFragment())
                }
                else if (it.code() == 413){
                    Toast.makeText(requireContext(), "Your Post must be under 8Mb", Toast.LENGTH_SHORT).show()
                }
                confirmAddPostFragmentToolbarBackButton.isEnabled = true
                confirmAddPostFragmentCaptionEdt.isEnabled = true
            }
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.progress_dialog_layout)
            dialog.setCancelable(false)

            //loading progressbar handling
            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    confirmAddPostFragmentToolbarCheckButton.visibility = View.INVISIBLE
                    confirmAddPostFragmentToolbarPostingProgressbar.visibility = View.VISIBLE

                    dialog.show()
                }else{
                    confirmAddPostFragmentToolbarCheckButton.visibility = View.VISIBLE
                    confirmAddPostFragmentToolbarPostingProgressbar.visibility = View.INVISIBLE

                    dialog.dismiss()
                }
            }
        }
    }
}