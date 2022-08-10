package com.garibyan.armen.tbc_classwork_5.screens

sealed class ScreenState<out T>{
    data class Success<T>(val data: T): ScreenState<T>()
    data class Error(val error: String): ScreenState<Nothing>()
    object Loading: ScreenState<Nothing>()
}