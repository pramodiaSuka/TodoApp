package com.maverick.todoapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maverick.todoapp.util.DB_NAME
import com.maverick.todoapp.util.MIGRATION_1_2
import com.maverick.todoapp.util.MIGRATION_2_3
import com.maverick.todoapp.util.MIGRATION_3_4

@Database(entities = arrayOf(Todo::class), version = 4)
abstract class TodoDatabase:RoomDatabase() {
    abstract fun todoDao(): TodoDao //Jika mempunyai 5 DAO, maka harus membuat 5 abstract

    companion object {
        //Konsep singleton, dimana suatu class hanya bisa mempunyai 1 object dalam satu waktu.
        //Ini untuk menghindari pembuatan 2 objek database dalam waktu yang bersamaan.
        //Agar proses kueri tidak ada conflict
        @Volatile private var instance: TodoDatabase ?= null
        private val LOCK = Any()

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                DB_NAME
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build()

        operator fun invoke(context: Context){
            if (instance != null){
                synchronized(LOCK){
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }
        }
    }
}