package com.example.slotbooking.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import com.example.slotbooking.R
import com.example.slotbooking.databinding.ActivityMainBinding
import com.example.slotbooking.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        );
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        val today = Calendar.getInstance()
        val now = today.timeInMillis
        binding.calender.minDate = now

        val date: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        println("Default Date is==${date}")
        viewModel.selectedDate.set(date)

        viewModel.readalldata.observe(this, { slot ->
            println("Last few transactions=======" + slot)
//            viewModel.maxMornSlot.set(5)
//            viewModel.maxEvenSlot.set(5)
            viewModel.setMorningBtnColor()
            viewModel.setEveningBtnColor()
            viewModel.morningslotvalidation()
            viewModel.eveningslotvalidation()
            if(viewModel.morningSlotcount.get() == 0){
                Toast.makeText(applicationContext, "All Morning Slots Booked", Toast.LENGTH_SHORT)
                    .show()
            }
            if(viewModel.eveningSlotcount.get() == 0){
                Toast.makeText(applicationContext, "All Evening Slots Booked", Toast.LENGTH_SHORT)
                    .show()
            }
        })





        binding.calender.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            var monthNum = month + 1
            Toast.makeText(applicationContext, "$dayOfMonth/$monthNum/$year", Toast.LENGTH_SHORT)
                .show()
            viewModel.selectedDate.set("$dayOfMonth/$monthNum/$year")
            viewModel.eveningslot.set("NA")
            viewModel.moringslot.set("NA")

            /* viewModel.morningslotvalidation()
             viewModel.eveningslotvalidation()*/

            viewModel.setMorningBtnColor()
            viewModel.setEveningBtnColor()

            println("Morning Btn Color is==${viewModel.morningBtnColor.get()}")
            println("Evening Btn Color is==${viewModel.eveningBtnColor.get()}")
            println("Selected Date is==${viewModel.selectedDate.get()}")
            println("Datebase List is==${viewModel.readalldata.value}")

            /* if (viewModel.checkEvening.get() == true){
                 binding.eveningSlotButton.isLongClickable = false
             }
              if (viewModel.checkMorning.get() == true){
                  binding.eveningSlotButton.isLongClickable = false
              }*/
            // binding.calender.date.colorSpace.id.days.
        })


        binding.morningSlotButton.setOnClickListener(
            View.OnClickListener {
                // viewModel.morningBtnColor.set(true)
                viewModel.moringslot.set("8AM - 2PM")
                viewModel.morningBtnColor.set(true)
                println("================Evening " + viewModel.eveningslot.get())
            })

        binding.eveningSlotButton.setOnClickListener(
            View.OnClickListener {
                viewModel.setEveningBtnColor()
                viewModel.eveningslot.set("2PM - 8PM")
                viewModel.eveningBtnColor.set(true)
                println("================Morning " + viewModel.moringslot.get())
            })


        binding.bookSlot.setOnClickListener(View.OnClickListener {
            viewModel.checkValidation()
            if (viewModel.success.get()) {
                viewModel.morningslotvalidation()
                viewModel.eveningslotvalidation()
                Toast.makeText(applicationContext, "successfully booking added", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Sorry already slot was booked",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


    }
}