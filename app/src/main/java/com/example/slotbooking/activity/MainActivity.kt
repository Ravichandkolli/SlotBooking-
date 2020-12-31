package com.example.slotbooking.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.colorSpace
import androidx.core.graphics.green
import androidx.core.graphics.luminance
import androidx.core.graphics.red
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.slotbooking.R
import com.example.slotbooking.databinding.ActivityMainBinding
import com.example.slotbooking.viewmodel.MainViewModel
import java.util.*
import kotlin.time.days
import kotlin.time.hours


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        );
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        binding.eveningSlot.setText("Availability Slots 5")
        binding.morningSlot.setText("Availability Slots 5")

        val today = Calendar.getInstance()
        val now = today.timeInMillis
        binding.calender.minDate = now


        binding.calender.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            var monthNum =month+1
            Toast.makeText(applicationContext, "$dayOfMonth/$monthNum/$year", Toast.LENGTH_SHORT).show()
            binding.eveningSlotButton.setBackgroundColor(Color.LTGRAY)
            binding.morningSlotButton.setBackgroundColor(Color.LTGRAY)
            viewModel.selectedDate = "$dayOfMonth/$monthNum/$year"
            binding.eveningSlot.setText("Availability Slots 4")
            binding.morningSlot.setText("Availability Slots 4")
           // binding.calender.date.colorSpace.id.days.
        })


        binding.morningSlotButton.setOnClickListener(
            View.OnClickListener {
                binding.morningSlotButton.setBackgroundColor(Color.GREEN)
                binding.eveningSlotButton.setBackgroundColor(Color.LTGRAY)
                viewModel.eveningslot = "NA"
                viewModel.moringslot = "8AM - 2PM"
            })

        binding.eveningSlotButton.setOnClickListener(
            View.OnClickListener {
                binding.eveningSlotButton.setBackgroundColor(Color.GREEN)
                binding.morningSlotButton.setBackgroundColor(Color.LTGRAY)
                viewModel.eveningslot = "2PM - 8PM"
                viewModel.moringslot = "NA"
            })
         viewModel.readalldata.observe(this,{slot ->
            println("Last few transactions======="+slot)
        })

        binding.bookSlot.setOnClickListener(View.OnClickListener {
            viewModel.checkValidation()
            if (viewModel.success.get()){
                Toast.makeText(applicationContext, "successfully booking added", Toast.LENGTH_SHORT).show()
            }
        })



    }
}