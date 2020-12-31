package com.example.slotbooking.database

import androidx.lifecycle.LiveData

class SlotDataRepository(private val slotDAO: SlotDAO) {

    val readAllData : LiveData<List<SlotDetails>> =slotDAO.readAllUserData()

    suspend fun addSlot(slotDetails : SlotDetails){
        slotDAO.addSlotDetails(slotDetails)
    }

    suspend fun updateUserDetails(slotDetails: SlotDetails){
        slotDAO.updateUser(slotDetails)
    }

  /*  suspend fun deleteUserDetails(userdetails: UserDetails){
        slotDAO.deleteUserDetails(userdetails)
    }

    suspend fun deleteAllData(){
        slotDAO.deleteAllData()

    }*/


}