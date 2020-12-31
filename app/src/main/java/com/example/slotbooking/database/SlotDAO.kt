package com.example.slotbooking.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.slotbooking.database.SlotDetails

@Dao
interface SlotDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSlotDetails(slotDetails: SlotDetails)

    @Query("SELECT * FROM table_slot_book")
     fun readAllUserData() : LiveData<List<SlotDetails>>

     @Update
     suspend fun updateUser(slotDetails: SlotDetails)

     /*@Delete
     suspend fun deleteUserDetails(userDetails: UserDetails)

     @Query("DELETE FROM table_slot_book")
     fun deleteAllData(){}*/
}