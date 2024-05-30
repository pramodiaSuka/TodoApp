package com.maverick.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: Todo)

    @Query("SELECT * FROM todo WHERE is_done = 0 ORDER BY priority DESC")
    fun selectAllTodo(): List<Todo>

    @Query("SELECT * FROM todo WHERE uuid = :id")
    fun selectTodo(id:Int): Todo

    @Query("UPDATE todo SET title=:title, notes=:notes, priority=:priority WHERE uuid = :id")
    fun update(title:String, notes:String, priority:Int, id:Int)

    @Query("UPDATE todo SET is_done=:is_done WHERE uuid = :id")
    fun softDelete(is_done:Int, id:Int)

    @Update
    fun updateToDo(todo:Todo)
    @Delete
    fun deleteTodo(todo:Todo)
}