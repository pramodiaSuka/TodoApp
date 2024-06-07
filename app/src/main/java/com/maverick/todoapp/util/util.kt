package com.maverick.todoapp.util

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.maverick.todoapp.model.TodoDatabase

val DB_NAME = "newtododb"

val MIGRATION_1_2 = object : Migration(1,2){
    //is_done field use INTEGER instead of boolean because sqlite doesn't have a separate Boolean storage class (sqlite doesn't support boolean).
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE todo ADD COLUMN priority INTEGER DEFAULT 3 not null"
        )
    }
}

val MIGRATION_2_3 = object : Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE todo ADD COLUMN is_done INTEGER DEFAULT 0 not null"
        )
    }
}

fun buildDb(context: Context):TodoDatabase{
    val db = TodoDatabase.buildDatabase(context)
    return db
}

class TodoWorker(context: Context, params:WorkerParameters):Worker(context,params){
    override fun doWork(): Result {
        NotificationHelper(applicationContext).createNotification(
            inputData.getString("title").toString(),
            inputData.getString("message").toString()
        )
        return Result.success()
    }

}

