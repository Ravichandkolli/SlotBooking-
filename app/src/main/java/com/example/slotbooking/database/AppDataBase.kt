package com.example.slotbooking.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.slotbooking.database.SlotDAO
import com.example.slotbooking.database.SlotDetails

@Database(entities = [SlotDetails::class],version = 1,exportSchema = false)
abstract class AppDataBase : RoomDatabase() {


    abstract fun slotDao() : SlotDAO

    companion object {
         @Volatile
        private var DBInstance : AppDataBase?= null

        fun getDataBase(context: Context): AppDataBase{
            val temInstance = DBInstance
            if(temInstance != null){
                return temInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "slot_details"
                ).build()
                DBInstance = instance
                return instance
            }
        }
    }

}