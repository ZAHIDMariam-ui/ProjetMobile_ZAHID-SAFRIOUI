package com.example.projetappmobile.data.repository

import com.example.projetappmobile.data.dao.UserDao
import com.example.projetappmobile.data.entity.User
import com.example.projetappmobile.data.util.PasswordHasher

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Long {
        // Hacher le mot de passe avant stockage
        val hashedPassword = PasswordHasher.hashPassword(user.password)
        val userWithHashedPassword = user.copy(password = hashedPassword)
        return userDao.insertUser(userWithHashedPassword)
    }

    suspend fun loginUser(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        return if (user != null && PasswordHasher.verifyPassword(password, user.password)) {
            user
        } else {
            null
        }
    }

    suspend fun isEmailExists(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }


    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    // Nouvelle méthode pour la réinitialisation du mot de passe
    suspend fun initiatePasswordReset(email: String): Boolean {
        val user = userDao.getUserByEmail(email) ?: return false

        // Générer un token de réinitialisation valide 1 heure
        val resetToken = PasswordHasher.generateResetToken()
        val tokenExpiry = System.currentTimeMillis() + (60 * 60 * 1000) // 1 heure

        val updatedUser = user.copy(
            resetToken = resetToken,
            resetTokenExpiry = tokenExpiry
        )

        userDao.updateUser(updatedUser)
        return true
    }

    suspend fun resetPassword(token: String, newPassword: String): Boolean {
        val user = userDao.getUserByResetToken(token) ?: return false

        // Vérifier si le token n'a pas expiré
        if (user.resetTokenExpiry != null && user.resetTokenExpiry < System.currentTimeMillis()) {
            return false
        }

        // Hacher le nouveau mot de passe
        val hashedPassword = PasswordHasher.hashPassword(newPassword)
        val updatedUser = user.copy(
            password = hashedPassword,
            resetToken = null,
            resetTokenExpiry = null
        )

        userDao.updateUser(updatedUser)
        return true
    }

    suspend fun validateResetToken(token: String): Boolean {
        val user = userDao.getUserByResetToken(token) ?: return false
        return user.resetTokenExpiry != null && user.resetTokenExpiry >= System.currentTimeMillis()
    }
}