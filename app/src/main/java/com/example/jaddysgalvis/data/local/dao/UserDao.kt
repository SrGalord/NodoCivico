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

    @Query(
        "SELECT * FROM users WHERE email = :email AND password = :password"
    )
    suspend fun loginUser(
        email: String,
        password: String
    ): UserEntity?
}