package com.clockingcloud.utils

import android.util.Base64
import java.security.MessageDigest

object SecurityUtils {

    fun encodePassword(password: String): String {
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hash, Base64.DEFAULT)
    }

    fun verifyPassword(input: String, storedBase64: String): Boolean {
        return encodePassword(input).trim() == storedBase64.trim()
    }
}
