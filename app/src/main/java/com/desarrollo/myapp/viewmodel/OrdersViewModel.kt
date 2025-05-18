package com.desarrollo.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desarrollo.myapp.repository.OrdersRepository
import com.google.firestore.v1.StructuredQuery.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
}


