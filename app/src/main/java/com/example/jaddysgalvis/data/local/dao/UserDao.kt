package com.example.jaddysgalvis.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jaddysgalvis.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: UserEntity)

    // 🔥 LOGIN CON EMAIL O NOMBRE
    @Query("""
        SELECT * FROM users 
        WHERE (email = :input OR name = :input) 
        AND password = :password
    """)
    suspend fun loginUser(
        input: String,
        password: String
    ): UserEntity?

    @Query("SELECT * FROM users WHERE role = 'ADMIN' LIMIT 1")
    suspend fun getAdmin(): UserEntity?


    // 🔥 BUSCAR USUARIO POR ID (perfil, etc)
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?
}