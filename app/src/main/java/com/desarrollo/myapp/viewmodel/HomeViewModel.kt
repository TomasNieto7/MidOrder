package com.desarrollo.myapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desarrollo.myapp.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val repository = LocalRepository()

    // Cambia el tipo de _locals a List<Map<String, Any>> para contener toda la data
    private val _locals = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val locals: StateFlow<List<Map<String, Any>>> = _locals

    init {
        loadLocals()
    }

    internal fun loadLocals() {
        viewModelScope.launch {
            // Llamamos a getLocals y asignamos la respuesta a _locals
            _locals.value = repository.getLocals()
            Log.d("HomeSeller", "localsA: ${_locals.value}")
        }
    }
}


