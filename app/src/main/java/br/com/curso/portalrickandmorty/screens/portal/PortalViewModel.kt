package br.com.curso.portalrickandmorty.screens.portal

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.curso.portalrickandmorty.location.LocationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import android.util.Log

class PortalViewModel(private val locationManager: LocationManager) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

    fun startTracking() {
        if (_isTracking.value) {
            Log.d("PortalViewModel", "Already tracking")
            return
        }
        
        Log.d("PortalViewModel", "Starting tracking job")
        viewModelScope.launch {
            _isTracking.value = true
            try {
                locationManager.getLocationUpdates().collectLatest {
                    Log.d("PortalViewModel", "Received location in ViewModel: $it")
                    _location.value = it
                }
            } catch (e: Exception) {
                Log.e("PortalViewModel", "Error in location flow", e)
                _isTracking.value = false
            }
        }
    }

    fun stopTracking() {
        _isTracking.value = false
        // In this simple implementation, stopping tracking is handled by the Flow lifecycle
        // when the ViewModel is cleared or we can manage a Job if needed.
    }
}