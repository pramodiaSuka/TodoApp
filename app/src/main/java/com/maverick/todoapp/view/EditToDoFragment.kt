package com.maverick.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.maverick.todoapp.R
import com.maverick.todoapp.databinding.FragmentCreateToDoBinding
import com.maverick.todoapp.viewmodel.DetailToDoViewModel

class EditToDoFragment : Fragment() {
    private lateinit var binding: FragmentCreateToDoBinding
    private lateinit var viewModel: DetailToDoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailToDoViewModel::class.java)

        binding.txtToDoTitle.text = "Edit ToDo"
        binding.btnAdd.text = "Save Changes"

        val uuid = EditToDoFragmentArgs.fromBundle(requireArguments()).uuid
        viewModel.fetch(uuid)

        binding.btnAdd.setOnClickListener {
            val radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
            viewModel.update(binding.txtTitle.text.toString(), binding.txtNotes.text.toString(), radio.tag.toString().toInt(), uuid)
            Toast.makeText(view.context, "Todo Updated", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(it).popBackStack()
        }

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            binding.txtTitle.setText(it.title)
            binding.txtNotes.setText(it.notes)
            when(it.priority){
                1 -> binding.radioLow.isChecked = true
                2 -> binding.radioMedium.isChecked = true
                else -> binding.radioHigh.isChecked = true
            }
        })
    }
}