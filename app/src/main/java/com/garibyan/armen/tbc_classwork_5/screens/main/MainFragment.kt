package com.garibyan.armen.tbc_classwork_5.screens.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.garibyan.armen.tbc_classwork_5.R
import com.garibyan.armen.tbc_classwork_5.databinding.FragmentMainBinding
import com.garibyan.armen.tbc_classwork_5.nodel.network.ViewItem
import com.garibyan.armen.tbc_classwork_5.screens.ScreenState
import com.garibyan.armen.tbc_classwork_5.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private var binding: FragmentMainBinding? = null
    private val viewItemAdapter: ViewItemAdapter by lazy { ViewItemAdapter() }
    private var checkedItem = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickListeners()
        collectors()
    }

    private fun onClickListeners(){
        viewItemAdapter.onChooserItemClick = {
            when (it) {
                "Birthday" -> datePickerDialog()
                "Gender" -> genderPickerDialog()
            }
        }

        binding!!.btnRegister.setOnClickListener {
            checkForEmptyFields()
        }
    }

    private fun collectors(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest {
                    when (it) {
                        is ScreenState.Loading -> loadingViewState()
                        is ScreenState.Error -> errorViewState()
                        is ScreenState.Success -> successViewState(it.data)
                    }
                }
            }
        }
    }

    private fun datePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, myear, mmonth, mdayOfMonth ->
                updateDate("$mdayOfMonth/$mmonth/$myear")
            },
            year, month, day
        )
            .show()
    }

    private fun genderPickerDialog() {
        val genders = arrayOf("Male", "Female")
        AlertDialog.Builder(requireContext())
            .setTitle("Gender Picker")
            .setSingleChoiceItems(genders, checkedItem) { dialog, which ->
                checkedItem = which
                updateGender(genders[which])
                dialog.dismiss()
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun updateGender(value: String) {
        for (i in 0 until viewItemAdapter.currentList.size) {
            when (val viewHolder =
                binding!!.recyclerView.findViewHolderForAdapterPosition(i)) {
                is ViewItemAdapter.ChooserViewHolder -> {
                    when (viewItemAdapter.currentList[i].hint) {
                        "Gender" -> (viewHolder.itemView as TextView).text = value
                    }
                }
            }
        }
    }

    private fun updateDate(value: String) {
        for (i in 0 until viewItemAdapter.currentList.size) {
            when (val viewHolder =
                binding!!.recyclerView.findViewHolderForAdapterPosition(i)) {
                is ViewItemAdapter.ChooserViewHolder -> {
                    when (viewItemAdapter.currentList[i].hint) {
                        "Birthday" -> (viewHolder.itemView as TextView).text = value
                    }
                }
            }
        }
    }

    private fun checkForEmptyFields() {
        for (i in 0 until viewItemAdapter.currentList.size) {
            when (val viewHolder = binding!!.recyclerView.findViewHolderForAdapterPosition(i)) {
                is ViewItemAdapter.InputViewHolder -> {
                    if ((viewHolder.itemView as AppCompatEditText).text?.isEmpty()!! && viewItemAdapter.currentList[i].required as Boolean) {
                        Toast.makeText(
                            requireContext(),
                            "${viewItemAdapter.currentList[i].hint} is empty!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun successViewState(list: List<ViewItem>) = with(binding!!) {
        binding!!.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = viewItemAdapter
            visibility = View.VISIBLE
            viewItemAdapter.submitList(list)
            recyclerView.findViewHolderForAdapterPosition(1)?.itemView?.performClick()
        }
        binding!!.btnRegister.visibility = View.VISIBLE
        View.GONE.also {
            tvState.visibility = it
            progressBar.visibility = it
        }
    }

    private fun loadingViewState() = with(binding!!) {
        recyclerView.visibility = View.GONE
        btnRegister.visibility = View.GONE
        tvState.text = requireContext().getString(R.string.loading)
        View.VISIBLE.also {
            progressBar.visibility = it
            tvState.visibility = it
        }
    }

    private fun errorViewState() = with(binding!!) {
        View.GONE.also {
            recyclerView.visibility = it
            progressBar.visibility = it
            btnRegister.visibility = it
        }
        tvState.visibility = View.VISIBLE
        tvState.text = requireContext().getString(R.string.error)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}