package com.maverick.todoapp.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
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

class CreateToDoFragment : Fragment(), TodoAddClickListener, RadioClickListener, DateClickListener, TimeClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: FragmentCreateToDoBinding
    private lateinit var viewModel:DetailToDoViewModel
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

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

        binding.todo = Todo("", "", 3, 0, 0)

        viewModel = ViewModelProvider(this).get(DetailToDoViewModel::class.java)
        binding.listener = this
        binding.radioListener = this
        binding.listenerDate = this
        binding.listenerTime = this


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
//        var radio = view?.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//        var todo = Todo(
//            binding.txtTitle.text.toString(),
//            binding.txtNotes.text.toString(),
//            radio?.tag.toString().toInt(),
//            0
//        )

        val c = Calendar.getInstance()
        c.set(year, month, day, hour, minute, 0)
        val today = Calendar.getInstance()
        val diff = (c.timeInMillis/1000L) - (today.timeInMillis/1000L)
        binding.todo!!.todo_date = (c.timeInMillis/1000L).toInt()

        val list = listOf(binding.todo!!)

        viewModel.addTodo(list)
        Toast.makeText(v.context, "Data added", Toast.LENGTH_LONG).show()

        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(diff, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo created",
                    "message" to "Stay Focus"
                )
            )
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }

    override fun OnDateClick(v: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        activity?.let { it1 -> DatePickerDialog(it1, this, year, month, day).show()}
    }

    override fun OnTimeClick(v: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Calendar.getInstance().let {
            it.set(year, month, dayOfMonth)
            binding.txtDate.setText(dayOfMonth.toString().padStart(2, '0',) + "-" + month.toString().padStart(2, '0') + "-" + year)
            this.year = year
            this.month = month
            this.day = dayOfMonth
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        binding.txtTime.setText(
            hourOfDay.toString().padStart(2, '0') + ":" + minute.toString().padStart(2, '0')
        )
        this.hour = hourOfDay
        this.minute = minute
    }
}