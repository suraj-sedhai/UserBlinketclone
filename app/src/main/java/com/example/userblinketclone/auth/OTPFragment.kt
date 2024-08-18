package com.example.userblinketclone.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.userblinketclone.R
import com.example.userblinketclone.Utils
import com.example.userblinketclone.activity.UserMainActivity
import com.example.userblinketclone.databinding.FragmentOTPBinding
import com.example.userblinketclone.models.Users
import com.example.userblinketclone.viewModels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {
    private val viewModel : AuthViewModel by viewModels()

    private lateinit var binding: FragmentOTPBinding
    private var userNumber: String = ""
    private lateinit var editText: Array<com.google.android.material.textfield.TextInputEditText>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(inflater, container, false)
        getUserNumber()
        customizingEnteringOTP()
        onBackBtnClick()
        sendOTP()
        onLoginBtnClick()
        return binding.root
    }

    private fun onLoginBtnClick() {
        binding.login.setOnClickListener {
            Utils.showDialog(requireContext(),"sinning you")
            val otp=editText.joinToString("") { it.text.toString() }

            if (otp.length<editText.size){
                Utils.showToast(requireContext(),"Plesae enter the correct otp")
            }
            else{
                editText.forEach{it.text?.clear()}
                verifyOTP(otp)

            }
        }
    }

    private fun verifyOTP(otp: String) {
        viewModel.signInWithPhoneAuthCredential(otp,userNumber)

        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collect{
                if (it == true){

                    Utils.hideDialog()
                    Utils.showToast(requireContext(),"you are successfully signed in")
                    startActivity(Intent(requireContext(), UserMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP")

        viewModel.apply{
            sendOTP(userNumber,requireActivity())
            lifecycleScope.launch{
                otpSent.collect(){
                    if (it == true){
                        Utils.hideDialog()
                        Utils.showToast(requireContext(),"the OTP is sent to ypur device")

                    }
                }

            }


        }
    }

    private fun onBackBtnClick() {
        binding.tbOTPFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }

    private fun customizingEnteringOTP() {
        editText = arrayOf(
            binding.etOTP1,
            binding.etOTP2,
            binding.etOTP3,
            binding.etOTP4,
            binding.etOTP5,
            binding.etOTP6
        )
        for (i in editText.indices) {
            editText[i].addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length == 1) {
                        if (i < editText.size - 1) {
                            editText[i + 1].requestFocus()
                        }
                    } else if (p0?.length == 0) {
                        if (i > 0) {
                            editText[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()
        binding.tvNumber.text = userNumber
    }
}
