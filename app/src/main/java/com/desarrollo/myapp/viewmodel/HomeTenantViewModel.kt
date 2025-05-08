package com.desarrollo.myapp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desarrollo.myapp.repository.LocalRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class HomeTenantViewModel : ViewModel() {
    private val localRepository = LocalRepository()

    private val _hasLocals = mutableStateOf<Boolean?>(null)
    val hasLocals: State<Boolean?> = _hasLocals

    fun checkIfUserHasLocals(userId: String) {
        viewModelScope.launch {
            try {
                val ownerRef = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)

                val locals = localRepository.getLocalsByOwner(ownerRef) // <-- tu función aquí
                _hasLocals.value = locals.isNotEmpty()
            } catch (e: Exception) {
                Log.e("HomeTenantVM", "Error checking user locals", e)
                _hasLocals.value = false
            }
        }
    }
}
