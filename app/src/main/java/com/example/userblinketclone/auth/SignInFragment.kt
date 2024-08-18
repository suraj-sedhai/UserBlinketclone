package com.example.userblinketclone.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userblinketclone.R
import com.example.userblinketclone.Utils
import com.example.userblinketclone.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        getUserNumber()
        onContinueButtonClick()
        return binding.root
    }



    private fun onContinueButtonClick() {
        binding.btnContinue.setOnClickListener {
            val mobileNumber = binding.mobileNumber.text.toString()
            if (mobileNumber.length != 10 || mobileNumber.isEmpty()) {
                Utils.showToast(requireContext(), "Please enter a valid mobile number")
            }
            else {
                val bundle = Bundle()
                bundle.putString("number", mobileNumber)
                findNavController().navigate(R.id.action_signInFragment_to_OTPFragment, bundle)
            }
        }
    }

    private fun getUserNumber() {
        binding.mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // No action needed before text changes
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val len = p0?.length
                if (len == 10) {
                    binding.btnContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.button_green
                        )
                    )
                } else {
                    binding.btnContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.button_background
                        )
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // No action needed after text changes
            }
        })
    }

}



