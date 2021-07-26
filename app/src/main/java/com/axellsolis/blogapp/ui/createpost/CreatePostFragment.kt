package com.axellsolis.blogapp.ui.createpost

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.data.remote.PostDataSource
import com.axellsolis.blogapp.databinding.FragmentCreatePostBinding
import com.axellsolis.blogapp.domain.posts.PostRepositoryImplementation
import com.axellsolis.blogapp.presentation.CreatePostViewModel
import com.axellsolis.blogapp.presentation.CreatePostViewModelFactory
import com.axellsolis.blogapp.utils.showToast

class CreatePostFragment : Fragment() {

    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private val viewModel: CreatePostViewModel by viewModels {
        CreatePostViewModelFactory(
            PostRepositoryImplementation(
                PostDataSource()
            )
        )
    }
    private var postPicture: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        setCameraLauncher()
        takePicture()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgPreview.setOnClickListener {
            takePicture()
        }

        binding.btnUploadPost.setOnClickListener {
            uploadPost()
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun setCameraLauncher() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    postPicture = result.data?.extras?.get("data") as Bitmap
                    binding.imgPreview.setImageBitmap(postPicture)
                }
            }
    }

    private fun uploadPost() {
        postPicture?.let { picture ->
            viewModel.uploadPost(
                picture,
                binding.etPostDescription.text.toString()
            ).observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        requireContext().showToast("Uploading post ...")
                    }
                    is Resource.Success -> {
                        requireContext().showToast("Upload Success!")
                        findNavController().popBackStack()
                    }
                    is Resource.Failure -> {
                        requireContext().showToast("Upload Error")
                    }
                }
            }
        }
    }
}