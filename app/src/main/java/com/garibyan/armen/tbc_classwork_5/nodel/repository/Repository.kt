package com.garibyan.armen.tbc_classwork_5.nodel.repository

import com.garibyan.armen.tbc_classwork_5.nodel.ApiService
import com.garibyan.armen.tbc_classwork_5.nodel.network.ViewItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class Repository(private val apiService: ApiService) {
    suspend fun getData(): Flow<Response<List<List<ViewItem>>>> {
        return flow {
            emit(apiService.getData())
        }.flowOn(Dispatchers.IO)
    }
}