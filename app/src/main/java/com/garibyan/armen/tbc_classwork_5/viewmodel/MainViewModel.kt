package com.garibyan.armen.tbc_classwork_5.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_classwork_5.nodel.ApiClient
import com.garibyan.armen.tbc_classwork_5.nodel.network.ViewItem
import com.garibyan.armen.tbc_classwork_5.nodel.repository.Repository
import com.garibyan.armen.tbc_classwork_5.screens.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
    = Repository(ApiClient.apiService)
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ScreenState<List<ViewItem>>>(ScreenState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            _stateFlow.value = ScreenState.Loading
            val itemList = mutableListOf<ViewItem>()
            repository.getData().collectLatest { response ->
                if(response.isSuccessful && response.body()!!.isNotEmpty()){
                    response.body()!!.forEach {
                            itemList.addAll(it)
                        }
                    _stateFlow.value = ScreenState.Success(itemList)
                }else{
                    _stateFlow.value = ScreenState.Error("error")
                }
            }
        }
    }
}