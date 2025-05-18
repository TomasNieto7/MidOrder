package com.desarrollo.myapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desarrollo.myapp.repository.LocalRepository
import com.desarrollo.myapp.repository.OrdersRepository
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

    private val _savedLocalIds = MutableStateFlow<Set<String>>(emptySet())
    val savedLocalIds: StateFlow<Set<String>> = _savedLocalIds

    fun loadSavedLocals(userId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getSavedLocals(userId)
                _savedLocalIds.value = result.toSet()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al cargar guardados", e)
            }
        }
    }

    fun toggleLocalSave(userId: String, localId: String) {
        viewModelScope.launch {
            val isSaved = _savedLocalIds.value.contains(localId)
            try {
                if (isSaved) {
                    repository.removeLocalFromUser(userId, localId)
                } else {
                    repository.saveLocalForUser(userId, localId)
                }
                loadSavedLocals(userId) // refresca
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error guardando/eliminando", e)
            }
        }
    }


    internal fun loadLocals() {
        viewModelScope.launch {
            // Llamamos a getLocals y asignamos la respuesta a _locals
            _locals.value = repository.getLocals()
            Log.d("HomeSeller", "localsA: ${_locals.value}")
        }
    }

    private val ordersRepository = OrdersRepository()

    fun createOrder(
        localId: String,
        senderId: String,
        recipientName: String,
        packageSize: String,
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            val orderId = ordersRepository.createOrderAndUpdateCapacity(
                localId = localId,
                senderId = senderId,
                recipientName = recipientName,
                packageSize = packageSize
            )
            if (orderId != null) onSuccess(orderId) else onFailure()
        }
    }
}


