package com.example.gatherpoint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gatherpoint.R
import com.example.gatherpoint.databinding.FragmentRegistrationBinding
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.viewmodel.LoginViewModel

class RegistrationFragment : Fragment() {

    private val viewModel: LoginViewModel by navGraphViewModels(R.id.nav_graph)

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            viewModel.register(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString()
            )
        }

        viewModel.registrationState.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status is Resource.Loading
            if (status is Resource.Success) {
                if (status.data == null) return@observe
                Toast.makeText(requireContext(), "Account created", Toast.LENGTH_SHORT).show()
                navigateToLoginScreen()
            }
            if (status is Resource.Error) {
                Toast.makeText(requireContext(), "Cannot create an account", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun navigateToLoginScreen() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}