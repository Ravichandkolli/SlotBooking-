package com.example.slotbooking.viewmodel

import android.app.Application
import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.slotbooking.database.AppDataBase
import com.example.slotbooking.database.SlotDataRepository
import com.example.slotbooking.database.SlotDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var maxMornSlot: Int = 5
    var maxEvenSlot: Int = 5
    var moringslot : String = "NA"
    var eveningslot : String = "NA"
    var selectedDate : String = ""
    var success : ObservableBoolean = ObservableBoolean(false)

    val slotDataRepository : SlotDataRepository
    val readalldata : LiveData<List<SlotDetails>>
    init {
        val slotDAO  = AppDataBase.getDataBase(application).slotDao()
        slotDataRepository = SlotDataRepository(slotDAO)
        readalldata = slotDataRepository.readAllData
        println("check view model is work are not " + readalldata.value)
    }

    fun checkValidation() {
        if (!selectedDate.isEmpty() && moringslot != "NA" || eveningslot != "NA"){
            println("booking validation is working"+selectedDate +" "+moringslot +" "+eveningslot)
            val slotDetails = SlotDetails(0,selectedDate, moringslot, eveningslot);
            viewModelScope.launch(Dispatchers.IO) {
                slotDataRepository.addSlot(slotDetails)
                success.set(true)
            }
        }else{
            println("booking validation is not working")
        }
    }



}