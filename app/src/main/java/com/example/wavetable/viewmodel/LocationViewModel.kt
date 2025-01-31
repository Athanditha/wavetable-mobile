package com.example.wavetable.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class LocationViewModel : ViewModel() {
    private val _address = MutableStateFlow<String?>(null)
    val address: StateFlow<String?> = _address

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context) {
        val fusedLocationClient: FusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(context)

        viewModelScope.launch {
            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).addOnSuccessListener { location: Location? ->
                    location?.let {
                        getAddressFromLocation(context, it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {
                _address.value = "Location permission not granted"
            }
        }
    }

    private fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            
            addresses?.firstOrNull()?.let { address ->
                val addressParts = listOfNotNull(
                    address.getAddressLine(0),
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
                _address.value = addressParts.joinToString(", ")
            }
        } catch (e: Exception) {
            _address.value = "Could not determine address"
        }
    }
} 