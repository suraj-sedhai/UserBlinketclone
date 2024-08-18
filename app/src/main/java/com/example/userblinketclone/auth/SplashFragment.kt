package com.example.userblinketclone.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import android.os.Build
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.userblinketclone.R
import com.example.userblinketclone.activity.UserMainActivity
import com.example.userblinketclone.databinding.FragmentSplashBinding
import com.example.userblinketclone.viewModels.AuthViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val viewModel : AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        setStatusBarColor(R.color.color_primary)

        Handler(Looper.getMainLooper()).postDelayed({

            lifecycleScope.launch {
                viewModel.isACurrentUser.collect{
                    if (it == true){
                        startActivity(Intent(requireContext(), UserMainActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        },3000)

        return binding.root
    }

    fun Fragment.setStatusBarColor(colorResId: Int) {
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = it.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(requireContext(), colorResId)
            }
        }
    }
}