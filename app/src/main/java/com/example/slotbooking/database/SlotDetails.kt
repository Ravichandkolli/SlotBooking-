package com.example.slotbooking.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_slot_book")
data class SlotDetails(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val date: String,
    val morning : String,
    val evening  : String
)
