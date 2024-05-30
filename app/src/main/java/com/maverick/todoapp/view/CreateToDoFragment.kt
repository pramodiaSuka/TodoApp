package com.maverick.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.maverick.todoapp.R
import com.maverick.todoapp.databinding.FragmentCreateToDoBinding
import com.maverick.todoapp.model.Todo
import com.maverick.todoapp.viewmodel.DetailToDoViewModel

class CreateToDoFragment : Fragment() {
    private lateinit var binding: FragmentCreateToDoBinding
    private lateinit var viewModel:DetailToDoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateToDoBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailToDoViewModel::class.java)

        binding.btnAdd.setOnClickListener {
            var radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
            var todo = Todo(
                binding.txtTitle.text.toString(),
                binding.txtNotes.text.toString(),
                radio.tag.toString().toInt(),
                0
            )

            val list = listOf(todo)

            viewModel.addTodo(list)
            Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()
        }
    }
}