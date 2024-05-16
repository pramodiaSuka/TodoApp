package com.maverick.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.maverick.todoapp.model.Todo
import com.maverick.todoapp.model.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailToDoViewModel(application: Application):AndroidViewModel(application), CoroutineScope {
    private val job = Job()

    fun addTodo(list:List<Todo>){
        //Funsi launch untuk membuat thread terpisah
        launch {
            val db = TodoDatabase.buildDatabase(
                getApplication()
            )
            db.todoDao().insertAll(*list.toTypedArray())
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}