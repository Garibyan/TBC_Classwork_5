package com.garibyan.armen.tbc_classwork_5.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private var binding: FragmentMainBinding? = null
    private val viewItemAdapter: ViewItemAdapter by lazy { ViewItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.stateFlow.collectLatest {
                    when(it){
                        is ScreenState.Loading ->loadingViewState()
                        is ScreenState.Error -> errorViewState()
                        is ScreenState.Success -> {
                            successViewState(it.data)
                        }
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
        }
        View.GONE.also {
            tvState.visibility = it
            progressBar.visibility = it
        }
    }

    private fun loadingViewState() = with(binding!!) {
        recyclerView.visibility = View.GONE
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
        }
        tvState.visibility = View.VISIBLE
        tvState.text = requireContext().getString(R.string.error)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}