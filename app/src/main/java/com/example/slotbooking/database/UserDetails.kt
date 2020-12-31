package com.example.slotbooking.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserDetails(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val name: String,
    val age : String,
    val area : String
)
