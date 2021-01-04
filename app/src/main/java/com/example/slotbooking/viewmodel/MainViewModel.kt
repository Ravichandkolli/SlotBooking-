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

    var maxMornSlot: ObservableField<String> = ObservableField("5")
    var maxEvenSlot: ObservableField<String> = ObservableField("5")


    var morningSlotcount: ObservableField<Int> = ObservableField(5)
    var eveningSlotcount: ObservableField<Int> = ObservableField(5)
    var moringslot: ObservableField<String> = ObservableField("NA")
    var eveningslot: ObservableField<String> = ObservableField("NA")
    var selectedDate: ObservableField<String> = ObservableField("")
    var success: ObservableBoolean = ObservableBoolean(false)
    var eveningBtnColor = ObservableField<Boolean>(false)
    var morningBtnColor = ObservableField<Boolean>(false)
    var checkMorning = ObservableField<Boolean>(false)
    var checkEvening = ObservableField<Boolean>(false)
    var mapOfMorning = mutableMapOf<String, String>()
    var mapOfEvening = mutableMapOf<String, String>()

    val slotDataRepository: SlotDataRepository
    val readalldata: LiveData<List<SlotDetails>>

    init {
        val slotDAO = AppDataBase.getDataBase(application).slotDao()
        slotDataRepository = SlotDataRepository(slotDAO)
        readalldata = slotDataRepository.readAllData
        println("check view model is work are not " + readalldata.value)

        morningSlotcount.set(morningSlotcount.get())
        eveningSlotcount.set(eveningSlotcount.get())

    }

    fun setMorningBtnColor() {

        if (selectedDate.get()?.length ?: 0 > 1) {

            var dayChanged = selectedDate.get()?.split("/")?.get(0)
            var monthChanged = selectedDate.get()?.split("/")?.get(1)
            var yearChanged = selectedDate.get()?.split("/")?.get(2)
            if (dayChanged?.length == 1 && monthChanged?.length == 1) {
                selectedDate.set("${"0$dayChanged"}${"/"}${"0$monthChanged"}${"/"}${yearChanged}")
            } else if (dayChanged?.length == 1) {
                selectedDate.set("${"0$dayChanged"}${"/"}${"$monthChanged"}${"/"}${yearChanged}")
            } else if (monthChanged?.length == 1) {
                selectedDate.set("${"$dayChanged"}${"/"}${"0$monthChanged"}${"/"}${yearChanged}")
            } else {
                selectedDate.set("${"$dayChanged"}${"/"}${"$monthChanged"}${"/"}${yearChanged}")
            }
        }
        if (readalldata.value?.size ?: 0 > 0) {
            for (i in 0..readalldata.value!!.size.minus(1)) {
                if (readalldata.value?.get(i)?.date.toString() == selectedDate.get().toString()) {
                    if (readalldata.value?.get(i)?.morning != "NA") {
                        moringslot.set("${readalldata.value?.get(i)?.morning}")
                        morningBtnColor.set(true)
                        checkMorning.set(true)
                        break
                    } else {
                        println("Slot was Not Booked in this date")
                        morningBtnColor.set(false)
                    }
                } else {
                    println("Slot was Not Booked in this date")
                    morningBtnColor.set(false)
                }
            }
        } else {
            morningBtnColor.set(false)
        }
    }

    fun setEveningBtnColor() {
        if (selectedDate.get()?.length ?: 0 > 1) {

            var dayChanged = selectedDate.get()?.split("/")?.get(0)
            var monthChanged = selectedDate.get()?.split("/")?.get(1)
            var yearChanged = selectedDate.get()?.split("/")?.get(2)
            if (dayChanged?.length == 1 && monthChanged?.length == 1) {
                selectedDate.set("${"0$dayChanged"}${"/"}${"0$monthChanged"}${"/"}${yearChanged}")
            } else if (dayChanged?.length == 1) {
                selectedDate.set("${"0$dayChanged"}${"/"}${"$monthChanged"}${"/"}${yearChanged}")
            } else if (monthChanged?.length == 1) {
                selectedDate.set("${"$dayChanged"}${"/"}${"0$monthChanged"}${"/"}${yearChanged}")
            } else {
                selectedDate.set("${"$dayChanged"}${"/"}${"$monthChanged"}${"/"}${yearChanged}")
            }
        }

        if (readalldata.value?.size ?: 0 > 0) {
            for (i in 0..readalldata.value!!.size.minus(1)) {
                if (readalldata.value?.get(i)?.date == selectedDate.get().toString()) {
                    if (readalldata.value?.get(i)?.evening != "NA") {
                        eveningslot.set("${readalldata.value?.get(i)?.evening}")
                        eveningBtnColor.set(true)
                        checkEvening.set(true)
                        break
                    } else {
                        println("Slot was Not Booked in this date")
                        eveningBtnColor.set(false)
                    }
                } else {
                    println("Slot was Not Booked in this date")
                    eveningBtnColor.set(false)
                }
            }
        } else {
            eveningBtnColor.set(false)
        }
    }

    fun checkValidation() {
        if (!selectedDate.get()!!
                .isEmpty() && moringslot.get() != "NA" || eveningslot.get() != "NA"
        ) {
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

//    7th, 12th, 19th, 26th, 28th


    fun morningslotvalidation() {
        if (readalldata.value?.size ?: 0 > 0) {
            for (i in 0..readalldata.value!!.size.minus(1)) {
                if (readalldata.value?.get(i)?.date != null) {
                    if (readalldata.value?.get(i)?.morning != "NA") {
                        if (mapOfMorning.isEmpty()) {
                            mapOfMorning.put(
                                readalldata.value?.get(i)?.date!!,
                                readalldata.value?.get(i)?.morning!!
                            )
                        } else {
                            for ((key, value) in mapOfMorning) {
                                if (mapOfMorning.get(key) != readalldata.value!!.get(i).date && mapOfMorning.get(value) != readalldata.value!!.get(i).morning) {
                                    mapOfMorning.put(
                                        readalldata.value?.get(i)?.date!!,
                                        readalldata.value?.get(i)?.morning!!)
                                    break
                                }
                            }
                        }
                    }
                }
            }
            println("Morning List is ${mapOfMorning}")
            if (morningSlotcount.get()?.toInt() ?: 0 > 0) {
                morningSlotcount.set(maxMornSlot.get()?.toInt()?.minus(mapOfMorning.size))
                morningSlotcount.set(morningSlotcount.get())
            }

        } else {
            if (morningSlotcount.get()?.toInt() ?: 0 > 0) {
                morningSlotcount.set(maxMornSlot.get()?.toInt()?.minus(mapOfMorning.size))
                morningSlotcount.set(morningSlotcount.get())
            }
        }
    }

    fun eveningslotvalidation() {
        if (readalldata.value?.size ?: 0 > 0) {
            for (i in 0..readalldata.value!!.size.minus(1)) {
                if (readalldata.value?.get(i)?.date != null) {
                    if (readalldata.value?.get(i)?.evening != "NA") {
                        if (mapOfEvening.isEmpty()) {
                            mapOfEvening.put(
                                readalldata.value?.get(i)?.date!!,
                                readalldata.value?.get(i)?.evening!!
                            )
                        } else {
                            for ((key, value) in mapOfMorning) {
                                if (mapOfEvening.get(key) != readalldata.value!!.get(i).date && mapOfMorning.get(value) != readalldata.value!!.get(i).evening
                                ) {
                                    mapOfEvening.put(
                                        readalldata.value?.get(i)?.date!!,
                                        readalldata.value?.get(i)?.evening!!
                                    )
                                    break
                                }
                            }
                        }
                    }
                }
            }
            println("Evening List is ${mapOfEvening}")
            if (eveningSlotcount.get()?.toInt() ?: 0 > 0) {
                eveningSlotcount.set(maxEvenSlot.get()?.toInt()?.minus(mapOfEvening.size))
                eveningSlotcount.set(eveningSlotcount.get())
            }
        } else {
            if (eveningSlotcount.get()?.toInt() ?: 0 > 0) {
                eveningSlotcount.set(maxEvenSlot.get()?.toInt()?.minus(mapOfEvening.size))
                eveningSlotcount.set(eveningSlotcount.get())
            }
        }
    }

}