package com.maverick.todoapp.view

import android.view.View
import android.widget.CompoundButton
import com.maverick.todoapp.model.Todo

interface TodoCheckedChangeListener{
    fun onCheckedChanged(cb: CompoundButton, isChecked: Boolean, obj: Todo)
}

interface TodoEditClick{
    fun onTodoEditClick(v: View)
}

interface RadioClickListener{
    fun onRadioClick(v:View)
}

interface TodoAddClickListener{
    fun onTodoAddClick(v:View)
}

