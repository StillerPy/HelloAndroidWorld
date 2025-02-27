package ru.netology.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentNewPostBinding
import ru.netology.viewmodel.PostListViewModel


class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostListViewModel by activityViewModels()
        //binding.postContentInput.setText(content)
        binding.undoButton.setOnClickListener {
            //binding.postContentInput.setText(content)
        }
        binding.okButton.setOnClickListener {
            val text = binding.postContentInput.text.toString()
            if (text.isNotBlank()) {
                val viewModel: PostListViewModel by activityViewModels()
                viewModel.saveContent(text)
            }
            findNavController().navigateUp()
        }
        return binding.root
    }
}
