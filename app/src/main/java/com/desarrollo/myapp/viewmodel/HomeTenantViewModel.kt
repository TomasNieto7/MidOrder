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

    private val _filteredOrders = mutableStateOf<List<Map<String, Any>>>(emptyList())
    val filteredOrders: State<List<Map<String, Any>>> = _filteredOrders

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applySearchFilter()
    }

    fun checkIfUserHasLocals(userId: String) {
        viewModelScope.launch {
            try {
                val locals = localRepository.getLocalsByOwner(userId)
                _hasLocals.value = locals.isNotEmpty()
                _locals.value = locals
                val allOrders = mutableListOf<Map<String, Any>>()

                for (local in locals) {
                    val localId = local["id"] as? String ?: continue
                    val orders = ordersRepository.getOrdersByLocal(localId)
                    val localName = local["localName"] as? String ?: "Desconocido"
                    orders.forEach {
                        val copy = it.toMutableMap()
                        copy["localName"] = localName
                        allOrders.add(copy)
                    }
                }

                _orders.value = allOrders
                _filteredOrders.value = allOrders
                _hasOrders.value = allOrders.isNotEmpty()

            } catch (e: Exception) {
                Log.e("HomeTenantVM", "Error loading locals/orders", e)
                _locals.value = emptyList()
                _orders.value = emptyList()
                _filteredOrders.value = emptyList()
                _hasOrders.value = false
            }
        }
    }

    private fun applySearchFilter() {
        val query = _searchQuery.value.trim().lowercase()
        _filteredOrders.value = if (query.isEmpty()) {
            _orders.value
        } else {
            _orders.value.filter {
                val orderId = (it["orderId"] as? String)?.lowercase() ?: ""
                val localName = (it["localName"] as? String)?.lowercase() ?: ""
                orderId.contains(query) || localName.contains(query)
            }
        }
    }
}
