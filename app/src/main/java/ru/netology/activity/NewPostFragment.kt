package ru.netology.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentNewPostBinding
import ru.netology.util.StringSaver
import ru.netology.viewmodel.PostListViewModel
import ru.netology.viewmodel.emptyPost


class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostListViewModel by activityViewModels()
        val draftStorage = StringSaver(requireContext(), "draft")
        val originalText = arguments?.getString("post_content") ?: draftStorage.get()
        binding.postContentInput.setText(originalText)
        binding.undoButton.setOnClickListener {
            binding.postContentInput.setText(originalText)
        }
        binding.okButton.setOnClickListener {
            val text = binding.postContentInput.text.toString()
            if (text.isNotBlank()) {
                viewModel.saveContent(text)
            }
            draftStorage.del()
            findNavController().navigateUp()
        }
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this,
                object: OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        println("Back!")
                        val text = binding.postContentInput.text.toString()
                        if (text.isNotBlank()) {
                            draftStorage.put(text)
                        }
                        viewModel.edit(emptyPost)
                        findNavController().navigateUp()
                    }
                })
        return binding.root
    }


}
