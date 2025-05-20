package com.desarrollo.myapp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desarrollo.myapp.repository.LocalRepository
import com.desarrollo.myapp.repository.OrdersRepository
import kotlinx.coroutines.launch

class HomeTenantViewModel : ViewModel() {
    private val localRepository = LocalRepository()
    private val ordersRepository = OrdersRepository()

    private val _hasLocals = mutableStateOf<Boolean?>(null)
    val hasLocals: State<Boolean?> = _hasLocals

    private val _hasOrders = mutableStateOf<Boolean?>(null)
    val hasOrders: State<Boolean?> = _hasOrders

    private val _locals = mutableStateOf<List<Map<String, Any>>>(emptyList())
    val locals: State<List<Map<String, Any>>> = _locals

    private val _orders = mutableStateOf<List<Map<String, Any>>>(emptyList())
    val orders: State<List<Map<String, Any>>> = _orders

    fun checkIfUserHasLocals(userId: String) {
        viewModelScope.launch {
            try {
                val locals = localRepository.getLocalsByOwner(userId)
                _hasLocals.value = locals.isNotEmpty()
                _locals.value = locals

                if (locals.isNotEmpty()) {
                    val localId = locals.first()["id"] as? String
                    localId?.let {
                        loadOrdersForLocal(it)
                    }
                } else {
                    _hasOrders.value = false
                    _orders.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("HomeTenantVM", "Error checking user locals", e)
                _hasLocals.value = false
                _locals.value = emptyList()
                _hasOrders.value = false
                _orders.value = emptyList()
            }
        }
    }

    private fun loadOrdersForLocal(localID: String) {
        viewModelScope.launch {
            try {
                val ordersList = ordersRepository.getOrdersByLocal(localID)
                _orders.value = ordersList
                _hasOrders.value = ordersList.isNotEmpty()
            } catch (e: Exception) {
                Log.e("HomeTenantVM", "Error loading orders", e)
                _hasOrders.value = false
                _orders.value = emptyList()
            }
        }
    }

    fun checkIfLocalHasOrders(localID: String) {
        // Ya no es necesario si cargas en checkIfUserHasLocals, pero si quieres dejarlo, OK
        viewModelScope.launch {
            try {
                val orders = ordersRepository.getOrdersByLocal(localID)
                _hasOrders.value = orders.isNotEmpty()
            } catch (e: Exception) {
                Log.e("HomeTenantVM", "Error checking orders", e)
                _hasOrders.value = false
            }
        }
    }
}
