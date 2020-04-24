package com.vastalisy.syncit.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.vastalisy.syncit.RC_SIGN_IN
import com.vastalisy.syncit.USER_ACCOUNT
import com.vastalisy.syncit.databinding.AppSettingBinding
import com.vastalisy.syncit.feature_authentication.AccountAuthProvider
import com.vastalisy.syncit.feature_authentication.AccountAuthRequest
import com.vastalisy.syncit.feature_authentication.AccountAuthResult
import com.vastalisy.syncit.feature_authentication.User
import com.vastalisy.syncit.feature_authentication.view.UserAccountFragment
import com.vastalisy.syncit.feature_authentication.viewmodel.SettingsViewModel
import com.vastalisy.syncit.viewmodel.AuthViewModel
import com.vastalisy.syncit.viewmodel.factory.AuthViewModelFactory

class AppSettingActivity : AppCompatActivity(),
    AccountAuthRequest {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var authResult: AccountAuthResult
    private lateinit var appSettingBinding: AppSettingBinding

    override fun onAuthRequest() { //Perform auth request for the fragment
        val providers = AccountAuthProvider.getProviders()
        val authInstance = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        startActivityForResult(authInstance, RC_SIGN_IN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appSettingBinding = AppSettingBinding.inflate(layoutInflater)
        setContentView(appSettingBinding.root)

        val factory = AuthViewModelFactory(application)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]


        showUserAccountInfo()
    }

    private fun showUserAccountInfo(): UserAccountFragment {
        val userAccount = UserAccountFragment()
        authResult = userAccount
        supportFragmentManager.beginTransaction()
            .replace(appSettingBinding.accountFragContainer.id, userAccount, USER_ACCOUNT)
            .commit()
        return userAccount
    }

    private fun getSettingViewModel(): SettingsViewModel {
        return ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                authResult.onAuthSuccess(authViewModel.currentUser())
            } else {
                authResult.onAuthFail(resultCode)
            }
        }
    }

}