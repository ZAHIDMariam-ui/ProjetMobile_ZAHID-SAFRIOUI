package com.example.projetappmobile.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.projetappmobile.data.entity.User
import com.example.projetappmobile.data.repository.UserRepository
import com.example.projetappmobile.service.EmailService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _registrationStatus = MutableStateFlow<Long?>(null)
    val registrationStatus: StateFlow<Long?> = _registrationStatus

    private val _loginStatus = MutableStateFlow<User?>(null)
    val loginStatus: StateFlow<User?> = _loginStatus

    private val _resetPasswordStatus = MutableStateFlow<Boolean?>(null)
    val resetPasswordStatus: StateFlow<Boolean?> = _resetPasswordStatus

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    suspend fun register(user: User) {
        try {
            if (!userRepository.isEmailExists(user.email)) {
                val userId = userRepository.registerUser(user)
                _registrationStatus.value = userId
            } else {
                _errorMessage.value = "Email déjà utilisé"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Échec de l'inscription : ${e.message}"
        }
    }

    suspend fun login(email: String, password: String) {
        try {
            val user = userRepository.loginUser(email, password)
            _loginStatus.value = user
            if (user == null) {
                _errorMessage.value = "Email ou mot de passe incorrect"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Échec de la connexion : ${e.message}"
        }
    }

    suspend fun initiatePasswordReset(email: String, context: Context) {
        try {
            val success = userRepository.initiatePasswordReset(email)
            if (success) {
                // Récupérer l'utilisateur pour obtenir le token
                val user = userRepository.getUserByEmail(email)
                user?.resetToken?.let { token ->
                    // Envoyer l'email de réinitialisation
                    EmailService.sendPasswordResetEmail(context, email, token)
                    _resetPasswordStatus.value = true
                }
            } else {
                _errorMessage.value = "Aucun compte trouvé avec cet email"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Échec de la réinitialisation : ${e.message}"
        }
    }

    suspend fun resetPassword(token: String, newPassword: String) {
        try {
            val success = userRepository.resetPassword(token, newPassword)
            _resetPasswordStatus.value = success
            if (!success) {
                _errorMessage.value = "Token invalide ou expiré"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Échec de la réinitialisation : ${e.message}"
        }
    }

    suspend fun validateResetToken(token: String): Boolean {
        return userRepository.validateResetToken(token)
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearResetStatus() {
        _resetPasswordStatus.value = null
    }
}
