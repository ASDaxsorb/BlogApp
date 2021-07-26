package com.axellsolis.blogapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.axellsolis.blogapp.R
import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.data.remote.LoginDataSource
import com.axellsolis.blogapp.databinding.FragmentRegisterBinding
import com.axellsolis.blogapp.domain.auth.LoginRepositoryImplementation
import com.axellsolis.blogapp.presentation.RegisterViewModel
import com.axellsolis.blogapp.presentation.RegisterViewModelFactory
import com.axellsolis.blogapp.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class RegisterFragment : Fragment() {

    companion object {
        private const val TAG = "RegisterFragment"
    }

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(
            LoginRepositoryImplementation(
                LoginDataSource()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            hideKeyboard()
            signUp()
        }

    }

    private fun signUp() {
        val email = binding.etEmail
        val username = binding.etUserName
        val password = binding.etPassword
        val confirmPassword = binding.etConfirmPassword

        if (validateEmptyFields(email, username, password, confirmPassword)) {
            if (
                validatePasswords(
                    password.text?.trim().toString(),
                    confirmPassword.text?.trim().toString()
                )
            ) {
                createUser(
                    email.text.toString().trim(),
                    username.text.toString().trim(),
                    password.text.toString().trim()
                )
            }
        } else {
            Snackbar.make(requireView(), "Please set all empty the fields", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun validateEmptyFields(vararg fields: TextInputEditText): Boolean {
        var isValidFields = true
        for (field in fields) {
            if (field.isEmpty()) {
                isValidFields = false
            }
        }
        return isValidFields
    }

    private fun validatePasswords(password: String, confirmPassword: String): Boolean {
        return if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Password does not match"
            binding.etPassword.error = "Password does not match"
            Snackbar.make(requireView(), "Password does not match", Snackbar.LENGTH_SHORT)
                .show()
            false
        } else {
            binding.etConfirmPassword.error = null
            binding.etPassword.error = null
            true
        }
    }

    private fun createUser(email: String, username: String, password: String) {
        viewModel.signUp(email, username, password).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.show()
                }
                is Resource.Success -> {
                    findNavController().navigate(R.id.setupProfileFragment)
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Error: ${it.exception}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progressBar.gone()

        }
    }

}

