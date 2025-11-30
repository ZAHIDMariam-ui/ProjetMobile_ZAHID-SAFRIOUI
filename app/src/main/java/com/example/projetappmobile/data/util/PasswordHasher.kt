package com.example.projetappmobile.data.util

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {

    /**
     * Hache un mot de passe avec BCrypt
     */
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    /**
     * Vérifie si un mot de passe correspond au hash
     */
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }

    /**
     * Génère un token de réinitialisation sécurisé
     */
    fun generateResetToken(): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..32)
            .map { allowedChars.random() }
            .joinToString("")
    }
}