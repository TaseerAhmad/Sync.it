package com.vastalisy.syncit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.vastalisy.syncit.feature_authentication.User

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val fireAuth = FirebaseAuth.getInstance()

    fun currentUser(): User {
        val user = fireAuth.currentUser
        return user?.run { User.Available(this) } ?: User.NotAvailable
    }

}