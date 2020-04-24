package com.vastalisy.syncit.feature_authentication

import com.google.firebase.auth.FirebaseUser

sealed class User {
    object NotAvailable : User()
    class Available(val firebaseUser: FirebaseUser) : User()
}