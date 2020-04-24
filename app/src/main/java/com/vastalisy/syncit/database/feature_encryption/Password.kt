package com.vastalisy.syncit.database.feature_encryption

import java.security.SecureRandom

object Password {
    private val secureRandom = SecureRandom()
    private val passwordCombination = arrayOf(20, 22, 24, 26)

    fun getRandomPassword(): String {
        fun randomPasswordSize() = secureRandom.nextInt(passwordCombination.size - 1)
        var pass = ""
        val dictionary = Dictionary.get()
        val dictionaryLength = dictionary.length
        for (charIndex in 0 until randomPasswordSize()) {
            pass += dictionary[secureRandom.nextInt(dictionaryLength)]
        }
        return pass
    }

    private object Dictionary {
        private const val NUMBER = "0123456789"
        private const val SPECIAL = "!@#$%^&*_=+/"
        private const val ALPHA_U = "abcdefghijklmnopkqrtuvwxyz"
        private const val ALPHA_C = "ABCDEFGHIJKLMNOPKQRTUVWXYZ"

        fun get() = ALPHA_C + ALPHA_U + NUMBER + SPECIAL
    }
}
