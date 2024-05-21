package com.maverick.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maverick.todoapp.model.Todo
import com.maverick.todoapp.model.TodoDatabase
import com.maverick.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailToDoViewModel(application: Application):AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    val todoLD = MutableLiveData<Todo>()

    fun addTodo(list:List<Todo>){
        //Funsi launch untuk membuat thread terpisah
        launch {
            val db = buildDb(getApplication())

            db.todoDao().insertAll(*list.toTypedArray())
        }
    }

    fun fetch(uuid:Int){
        launch {
            val db = buildDb(getApplication())
            todoLD.postValue(db.todoDao().selectTodo(uuid))
        }
    }

    fun update(title:String, notes:String, priority:Int, uuid:Int){
        launch {
            val db = buildDb(getApplication())
            db.todoDao().update(title, notes, priority, uuid)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}