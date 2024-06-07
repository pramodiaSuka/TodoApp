package com.maverick.todoapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.maverick.todoapp.R
import com.maverick.todoapp.databinding.FragmentCreateToDoBinding
import com.maverick.todoapp.model.Todo
import com.maverick.todoapp.util.NotificationHelper
import com.maverick.todoapp.util.NotificationHelper.Companion.REQUEST_NOTIF
import com.maverick.todoapp.util.TodoWorker
import com.maverick.todoapp.viewmodel.DetailToDoViewModel
import java.util.concurrent.TimeUnit

class CreateToDoFragment : Fragment(), TodoAddClickListener, RadioClickListener {
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

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_NOTIF)
        }

        binding.todo = Todo("", "", 3, 0)

        viewModel = ViewModelProvider(this).get(DetailToDoViewModel::class.java)
        binding.listener = this
        binding.radioListener = this


//        binding.btnAdd.setOnClickListener {
////            val notif = NotificationHelper(view.context)
////            notif.createNotification("To do Created", "A new to do has been created! Stay focus!")
//
//            val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
//                .setInitialDelay(10, TimeUnit.SECONDS)
//                .setInputData(
//                    workDataOf(
//                        "title" to "Todo created",
//                        "message" to "Stay Focus"
//                    )
//                )
//                .build()
//            WorkManager.getInstance(requireContext()).enqueue(workRequest)
//
//            var radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//            var todo = Todo(
//                binding.txtTitle.text.toString(),
//                binding.txtNotes.text.toString(),
//                radio.tag.toString().toInt(),
//                0
//            )
//
//            val list = listOf(todo)
//
//            viewModel.addTodo(list)
//            Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
//
//
//
//
//            Navigation.findNavController(it).popBackStack()
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIF) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                NotificationHelper(requireContext())
                    .createNotification("Todo Created", "A new todo has been created! Stay focus!")
            }
        }
    }

    override fun onTodoAddClick(v: View) {
        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo created",
                    "message" to "Stay Focus"
                )
            )
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)

//        var radio = view?.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//        var todo = Todo(
//            binding.txtTitle.text.toString(),
//            binding.txtNotes.text.toString(),
//            radio?.tag.toString().toInt(),
//            0
//        )

        val list = listOf(binding.todo!!)

        viewModel.addTodo(list)
        Toast.makeText(v.context, "Data added", Toast.LENGTH_LONG).show()




        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }
}