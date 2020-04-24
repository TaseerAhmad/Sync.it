package com.vastalisy.syncit.feature_authentication

import com.firebase.ui.auth.AuthUI

object AccountAuthProvider {

    fun getProviders(): ArrayList<AuthUI.IdpConfig> {
        return arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
    }
}