package com.axellsolis.blogapp.ui.auth

import android.app.Activity
import android.app.AlertDialog
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
import com.axellsolis.blogapp.R
import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.data.remote.LoginDataSource
import com.axellsolis.blogapp.databinding.FragmentSetupProfileBinding
import com.axellsolis.blogapp.domain.auth.LoginRepositoryImplementation
import com.axellsolis.blogapp.presentation.RegisterViewModel
import com.axellsolis.blogapp.presentation.RegisterViewModelFactory
import com.axellsolis.blogapp.utils.gone
import com.axellsolis.blogapp.utils.show

class SetupProfileFragment : Fragment() {

    private lateinit var binding: FragmentSetupProfileBinding
    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(
            LoginRepositoryImplementation(
                LoginDataSource()
            )
        )
    }
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private var bitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCameraLauncher()

        binding.imgProfile.setOnClickListener {
            takePhoto()
        }

        binding.bntCreateProfile.setOnClickListener {
            createProfile()
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun setCameraLauncher() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    bitmap = result.data?.extras?.get("data") as Bitmap
                    binding.placeHolderPhoto.gone()
                    binding.imgProfile.setImageBitmap(bitmap)
                } else {
                    binding.placeHolderPhoto.show()
                }
            }
    }

    private fun createProfile() {
        val uploadAlert = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_creating_profile)).create()
        bitmap?.let { photo ->
            if (!binding.etUsername.text.isNullOrEmpty()) {
                viewModel.updateProfile(
                    imageBitmap = photo,
                    username = binding.etUsername.text.toString()
                ).observe(viewLifecycleOwner) {
                    when (it) {
                        is Resource.Loading -> {
                            uploadAlert.show()
                        }
                        is Resource.Success -> {
                            uploadAlert.dismiss()
                            val action = R.id.action_setupProfileFragment_to_homeFragment
                            findNavController().navigate(action)
                        }
                        is Resource.Failure -> {
                            uploadAlert.dismiss()
                        }
                    }
                }
            }
        }
    }

}