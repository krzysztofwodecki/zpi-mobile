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
import com.example.gatherpoint.databinding.FragmentLoginBinding
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.utils.Prefs
import com.example.gatherpoint.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by navGraphViewModels(R.id.nav_graph)

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noAccountLabel.setOnClickListener {
            navigateToRegistrationScreen()
        }
        binding.loginBtn.setOnClickListener {
            viewModel.login(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString(),
                Prefs(requireActivity())
            )
        }

        viewModel.loginState.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status is Resource.Loading
            if(status is Resource.Success) {
                if (status.data == null) return@observe
                navigateToDashboardScreen()
            }
            if(status is Resource.Error) {
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                viewModel.clearLoginStatus()
            }
        }
    }

    private fun navigateToRegistrationScreen() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        findNavController().navigate(action)
    }

    private fun navigateToDashboardScreen() {
        val action = LoginFragmentDirections.actionLoginFragmentToDashboardFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}