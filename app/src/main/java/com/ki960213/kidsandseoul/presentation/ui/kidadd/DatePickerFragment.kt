package com.ki960213.kidsandseoul.presentation.ui.kidadd

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.Calendar

@AndroidEntryPoint
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel: KidAddViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = viewModel.selectedBirthDate.value
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )
        datePickerDialog.datePicker.minDate = Calendar.getInstance()
            .apply { add(Calendar.YEAR, -ADULT_AGE) }
            .timeInMillis
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.selectBirthDate(LocalDate.of(year, month + 1, dayOfMonth))
        dismiss()
    }

    companion object {

        private const val ADULT_AGE = 19
    }
}
