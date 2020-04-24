package com.vastalisy.syncit.feature_authentication.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.vastalisy.syncit.R
import com.vastalisy.syncit.databinding.UserAccountBinding
import com.vastalisy.syncit.feature_authentication.AccountAuthRequest
import com.vastalisy.syncit.feature_authentication.AccountAuthResult
import com.vastalisy.syncit.feature_authentication.User
import com.vastalisy.syncit.viewmodel.AuthViewModel
import com.vastalisy.syncit.viewmodel.factory.AuthViewModelFactory

class UserAccountFragment : Fragment(),
    AccountAuthResult {
    private var _binding: UserAccountBinding? = null
    private lateinit var accountAuthRequest: AccountAuthRequest
    private val userAccountBinding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel


    override fun onAuthFail(resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_CANCELED -> makeToast("Login cancelled")
            else -> makeToast("Login failed")
        }
    }

    private fun makeToast(msg: String) {
        Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    override fun onAuthSuccess(user: User) {
        enableStateSignedIn()

        when (val userInfo = authViewModel.currentUser()) {
            is User.Available -> {
                enableStateSignedIn()
                displayUserAccountInfo(userInfo.firebaseUser)
            }
            is User.NotAvailable -> {
                enableStateSignedOut()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = AuthViewModelFactory(requireActivity().application)
        authViewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]

        accountAuthRequest = requireContext() as AccountAuthRequest
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserAccountBinding.inflate(inflater, container, false)
        return userAccountBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (val user = authViewModel.currentUser()) {
            is User.Available -> {
                enableStateSignedIn()
                displayUserAccountInfo(user.firebaseUser)
            }
            is User.NotAvailable -> {
                enableStateSignedOut()
            }
        }

        userAccountBinding.signInButton.setOnClickListener {
            accountAuthRequest.onAuthRequest()
        }

        userAccountBinding.signOutButton.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                enableStateSignedOut()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun displayUserAccountInfo(firebaseUser: FirebaseUser) {
        val profile = firebaseUser.providerData.first()

        var displayName = profile.displayName
        if (displayName.isNullOrEmpty())
            displayName = "N/A"

        userAccountBinding.userName.text = displayName
        userAccountBinding.userMail.text = profile.email
        Picasso.get()
            .load(profile.photoUrl)
            .fit()
            .centerCrop()
            .into(userAccountBinding.userImage)
    }

    private fun enableStateSignedIn() {
        userAccountBinding.stateSignedOut.isVisible = false
        userAccountBinding.stateSignedIn.isVisible = true
    }

    private fun enableStateSignedOut() {
        userAccountBinding.stateSignedIn.isVisible = false
        userAccountBinding.stateSignedOut.isVisible = true
    }
}