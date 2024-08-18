package com.example.userblinketclone.viewModels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.userblinketclone.models.Users
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit


class AuthViewModel : ViewModel(){



    private val _VerificationId = MutableStateFlow<String?>(null)
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var isSignedInSuccessfully= MutableStateFlow(false)

    private val _isACurrentUser = MutableStateFlow(false)
    val isACurrentUser = _isACurrentUser

    val currentUser = auth.currentUser

    init {
        currentUser?.let {
            _isACurrentUser.value = true
        }
    }


    private val _otpSent = MutableStateFlow(false)
    val otpSent = _otpSent

    fun sendOTP(phoneNumber: String, activity: Activity) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Handle verification completion
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // Handle verification failure
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _VerificationId.value = verificationId
                _otpSent.value = true
            }
        }


        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+7$phoneNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Corrected parameter name
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }



    fun signInWithPhoneAuthCredential(otp: String, userNumber: String) {
        val verificationId = _VerificationId.value
        if (verificationId.isNullOrEmpty()) {
            Log.e("AuthViewModel", "Verification ID is null or empty")
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val user = Users(
                            uid = currentUser.uid,
                            userPhoneNumber = userNumber,
                            userAddress = "Nepal"
                        )
                        saveUserToDatabase(user)
                    } else {
                        Log.e("AuthViewModel", "User is null after sign-in")
                    }
                } else {
                    Log.e("AuthViewModel", "Sign-in failed: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserToDatabase(user: Users) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(user.uid!!)
        databaseReference.setValue(user)
            .addOnSuccessListener {
                Log.d("AuthViewModel", "User data saved successfully")
                isSignedInSuccessfully.value = true

            }
            .addOnFailureListener { e ->
                Log.e("AuthViewModel", "Failed to save user data: ${e.message}")
            }
    }




}