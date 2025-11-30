package com.example.projetappmobile.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetappmobile.data.dao.UserDao
import com.example.projetappmobile.data.entity.User
import com.example.projetappmobile.ui.screens.cart.CartItem
import com.example.projetappmobile.ui.screens.home.Product
import com.example.projetappmobile.ui.screens.profile.OrderItem

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "raja_club_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}