package com.desarrollo.myapp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desarrollo.myapp.repository.LocalRepository
import com.desarrollo.myapp.repository.OrdersRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class HomeTenantViewModel : ViewModel() {
    private val localRepository = LocalRepository()

    private val _hasLocals = mutableStateOf<Boolean?>(null)
    val hasLocals: State<Boolean?> = _hasLocals

    private val ordersRepository = OrdersRepository()

    private val _hasOrders = mutableStateOf<Boolean?>(null)
    val hasOrders: State<Boolean?> = _hasOrders

    private val _locals = mutableStateOf<List<Map<String, Any>>>(emptyList())
    val locals: State<List<Map<String, Any>>> = _locals

    fun checkIfUserHasLocals(userId: String) {
        viewModelScope.launch {
            try {
                val locals = localRepository.getLocalsByOwner(userId)
                _hasLocals.value = locals.isNotEmpty()
                _locals.value = locals
            } catch (e: Exception) {
                Log.e("HomeTenantVM", "Error checking user locals", e)
                _hasLocals.value = false
                _locals.value = emptyList()
            }
        }
    }

    fun checkIfLocalHasOrders(localID: String) {
        viewModelScope.launch {
            try {
                val orders = ordersRepository.getOrdersByLocal(localID)
                _hasOrders.value = orders.isNotEmpty()
            } catch (e: Exception) {
                Log.e("HomeTenantVM", "Error checking user locals", e)
                _hasOrders.value = false
            }
        }
    }
}
