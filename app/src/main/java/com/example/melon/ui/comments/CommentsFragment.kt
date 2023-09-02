package com.example.melon.ui.comments

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.databinding.FragmentCommentsBinding
import com.example.melon.models.CommentModel
import com.example.melon.models.LikeCommentModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.adapters.CommentsAdapter
import com.example.melon.utils.Constants
import com.example.melon.viewmodels.CommentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CommentsFragment : Fragment() {

    private lateinit var binding: FragmentCommentsBinding

    @Inject
    lateinit var adapter: CommentsAdapter

    private val viewModel: CommentsViewModel by viewModels()

    private val args : CommentsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCommentsBinding.inflate(layoutInflater, container, false)
        MainActivity.appPagePosition = Constants.FRAGMENT_COMMENTS
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            setRecycler(args.commentsList.toList())

            commentsFragmentToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            
            commentsFragmentAddCommentButton.setOnClickListener {
                if(commentsFragmentAddCommentEdt.text.isNotEmpty()) {
                    val comment = commentsFragmentAddCommentEdt.text.toString()
                    commentsFragmentAddCommentEdt.clearFocus()
                    commentsFragmentAddCommentEdt.text.clear()


                    val date = Date()
                    val day = DateFormat.format("dd", date) as String
                    val monthName = DateFormat.format("MMMM", date) as String
                    val year = DateFormat.format("yyyy", date) as String


                    viewModel.addComment(LikeCommentModel(args.postId, args.userId, comment, "$year $monthName $day"))
                }else
                    Toast.makeText(requireContext(), "Comment field is empty", Toast.LENGTH_SHORT).show()
                
            }

            viewModel.addCommentResponse.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                if(it.success)
                    setRecycler(it.data.comments)
            }

            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    commentsFragmentAddCommentProgressbar.visibility = View.VISIBLE
                    commentsFragmentAddCommentButton.visibility = View.INVISIBLE
                }else{
                    commentsFragmentAddCommentProgressbar.visibility = View.INVISIBLE
                    commentsFragmentAddCommentButton.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun setRecycler(list: List<CommentModel>)
    {
        if(list.isEmpty())
            binding.commentsFragmentNoCommentText.visibility = View.VISIBLE
        else
            binding.commentsFragmentNoCommentText.visibility = View.INVISIBLE

        adapter.setData(list.reversed())
        binding.commentsFragmentRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.commentsFragmentRecycler.adapter = adapter
    }
}