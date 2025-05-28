package com.desarrollo.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desarrollo.myapp.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {
    private val ordersRepository = OrdersRepository()

    private val _orders = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val orders = _orders

    fun loadOrdersForUser(userId: String) {
        viewModelScope.launch {
            val ordersList = ordersRepository.getOrdersByUser(userId)
            _orders.value = ordersList
        }
    }

    private val _selectedOrder = MutableStateFlow<Map<String, Any>?>(null)
    val selectedOrder: StateFlow<Map<String, Any>?> = _selectedOrder

    fun loadOrderByRef(orderRef: String) {
        viewModelScope.launch {
            val order = ordersRepository.getOrderByRef(orderRef)
            _selectedOrder.value = order
        }
    }

    fun markOrderAsDelivered(orderRef: String, deliverPerson: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = ordersRepository.markOrderAsDelivered(orderRef, deliverPerson)
            onComplete(success)
        }
    }


}


