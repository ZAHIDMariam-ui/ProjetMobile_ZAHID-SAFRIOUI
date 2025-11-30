package com.example.projetappmobile.service


import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.delay

/**
 * Service simulé pour l'envoi d'emails
 * En production, remplacez par un vrai service d'email ou un appel API
 */
object EmailService {

    suspend fun sendPasswordResetEmail(context: Context, email: String, resetToken: String) {
        // Simulation d'un délai d'envoi d'email
        delay(2000)

        // En production, vous enverriez un vrai email ici
        // Pour l'instant, on affiche juste un Toast avec le token (pour le debug)

        // ⚠️ En production, NE montrez PAS le token à l'utilisateur !
        // Ici c'est juste pour la démo
        val message = """
            Email de réinitialisation envoyé à $email
            Token de test : $resetToken
            (En production, ce token serait envoyé par email)
        """.trimIndent()

        // Stocker le token localement pour les tests
        storeResetToken(context, resetToken)
    }

    private fun storeResetToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences("reset_tokens", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("last_reset_token", token)
            apply()
        }
    }

    fun getLastResetToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("reset_tokens", Context.MODE_PRIVATE)
        return sharedPref.getString("last_reset_token", null)
    }
}