package com.vastalisy.syncit.feature_authentication

interface AccountAuthResult {
    fun onAuthFail(resultCode: Int)
    fun onAuthSuccess(user: User)
}