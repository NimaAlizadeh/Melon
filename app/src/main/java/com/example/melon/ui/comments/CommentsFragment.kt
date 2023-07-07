package com.example.melon.ui.comments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melon.R
import com.example.melon.databinding.FragmentCommentsBinding
import com.example.melon.models.CommentModel
import com.example.melon.ui.adapters.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommentsFragment : Fragment() {

    private lateinit var binding: FragmentCommentsBinding

    @Inject
    lateinit var adapter: CommentsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCommentsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            adapter.differ.submitList(listOf(CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph9)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga #hello kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph1)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph2)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph3)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga #hello kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph4)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph5)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph6)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph7)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga #hello kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph8)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph9)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph1)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph2)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga #hello kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph3)!!),
                CommentModel("NimaALizadeh", "4w", "aksjhda khdkwajh dkahw dkajwhd akwhjd auiwhda kwdhakwj dhadjh awkdjha dkahdj kajwh dakjhwgd akjwhdg ajkhwdg ajkwhdga kjdh", ContextCompat.getDrawable(requireContext(), R.drawable.ph4)!!)))

            setRecycler(adapter)


            commentsFragmentToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            
            commentsFragmentAddCommentButton.setOnClickListener {
                if(commentsFragmentAddCommentEdt.text.isNotEmpty()){
                    val comment = commentsFragmentAddCommentEdt.text
                    commentsFragmentAddCommentEdt.clearFocus()
                    commentsFragmentAddCommentEdt.text.clear()
                    setRecycler(adapter)

                    Toast.makeText(requireContext(), "Comment has been sent", Toast.LENGTH_SHORT).show()
                }else
                    Toast.makeText(requireContext(), "There is nothing to send", Toast.LENGTH_SHORT).show()
                
            }
        }


    }

    fun setRecycler(adapter: CommentsAdapter){
        binding.commentsFragmentRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.commentsFragmentRecycler.adapter = adapter
    }
}