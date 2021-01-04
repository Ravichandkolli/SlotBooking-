package com.example.slotbooking.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.slotbooking.database.AppDataBase
import com.example.slotbooking.database.SlotDataRepository
import com.example.slotbooking.database.SlotDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var maxMornSlot: Int = 5
    var maxEvenSlot: Int = 5

    var morningSlotcount = ObservableField<String>()
    var eveningSlotcount = ObservableField<String>()
    var moringslot: ObservableField<String> = ObservableField("NA")
    var eveningslot: ObservableField<String> = ObservableField("NA")
    var selectedDate: ObservableField<String> = ObservableField("")
    var success: ObservableBoolean = ObservableBoolean(false)
    var eveningBtnColor  = ObservableField<Boolean>(false)
    var morningBtnColor = ObservableField<Boolean>(false)
    var checkMorning = ObservableField<Boolean>(false)
    var checkEvening = ObservableField<Boolean>(false)

    val slotDataRepository: SlotDataRepository
    val readalldata: LiveData<List<SlotDetails>>

    init {
        val slotDAO = AppDataBase.getDataBase(application).slotDao()
        slotDataRepository = SlotDataRepository(slotDAO)
        readalldata = slotDataRepository.readAllData
        println("check view model is work are not " + readalldata.value)

          eveningSlotcount.set("Availability Slots"+(maxEvenSlot))
          morningSlotcount.set("Availability Slots"+(maxMornSlot))

    }

    fun setMorningBtnColor() {
        if (readalldata.value?.size ?: 0 > 0) {
            for (i in 0..readalldata.value!!.size.minus(1)) {
                if (readalldata.value?.get(i)?.date == selectedDate.get().toString()) {
                    if (readalldata.value?.get(i)?.morning != "NA") {
                        moringslot.set("${readalldata.value?.get(i)?.morning}")
                        morningBtnColor.set(true)
                        checkMorning.set(true)
                        break
                    } else {
                        print("Slot was Not Booked in this date")
                        morningBtnColor.set(false)
                    }
                }else{
                    print("Slot was Not Booked in this date")
                    morningBtnColor.set(false)
                }
            }
        } else {
            morningBtnColor.set(false)
        }
    }

    fun setEveningBtnColor() {
        if (readalldata.value?.size ?: 0 > 0) {
            for (i in 0..readalldata.value!!.size.minus(1)) {
                if (readalldata.value?.get(i)?.date == selectedDate.get().toString()) {
                    if (readalldata.value?.get(i)?.evening != "NA") {
                        eveningslot.set("${readalldata.value?.get(i)?.evening}")
                        eveningBtnColor.set(true)
                        checkEvening.set(true)
                        break
                    } else {
                        print("Slot was Not Booked in this date")
                        eveningBtnColor.set(false)
                    }
                }else{
                    print("Slot was Not Booked in this date")
                    eveningBtnColor.set(false)
                }
            }
        } else {
            eveningBtnColor.set(false)
        }
    }
    fun checkValidation() {
        if (!selectedDate.get()!!.isEmpty() && moringslot.get() != "NA" || eveningslot.get() != "NA"){
            println("booking validation is working" + selectedDate.get() + " " + moringslot.get() + " " + eveningslot.get())
            success.set(true)
            val slotDetails = SlotDetails(
                0,
                selectedDate.get().toString(),
                moringslot.get().toString(),
                eveningslot.get().toString()
            )

            viewModelScope.launch(Dispatchers.IO) {
                slotDataRepository.addSlot(slotDetails)
            }
        } else {
            println("booking validation is not working")
        }
    }

    fun morningslotvalidation() {

        maxMornSlot = 5
        if (readalldata.value?.size ?: 0 > 0) {
            for (i in 0..readalldata.value!!.size.minus(1)) {

                if (readalldata.value?.get(i)?.morning != "NA") {
                    maxMornSlot--
                    morningSlotcount.set("Availability Slots"+(maxMornSlot))
                }/* else if (readalldata.value?.get(i)?.evening != "NA") {
                    print("Slot was Not Booked in this date")
                    eveningSlotcount.set("Availability Slots"+(maxEvenSlot-i))
                    break
                }*/
            }
        }else{
            morningSlotcount.set("Availability Slots"+(maxMornSlot))
        }
    }

    fun eveningslotvalidation() {
        maxMornSlot = 5
        if (readalldata.value?.size ?: 0 > 0) {
            for (i in 0..readalldata.value!!.size.minus(1)) {
              if (readalldata.value?.get(i)?.evening != "NA") {
                    print("Slot was Not Booked in this date")
                   maxEvenSlot--
                    eveningSlotcount.set("Availability Slots"+(maxEvenSlot))
                }
            }
        }else{
            eveningSlotcount.set("Availability Slots"+(maxEvenSlot))
        }
    }

}