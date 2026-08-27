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

class PortalViewModel(private val locationManager: LocationManager) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

    fun startTracking() {
        if (_isTracking.value) return
        
        viewModelScope.launch {
            _isTracking.value = true
            locationManager.getLocationUpdates().collectLatest {
                _location.value = it
            }
        }
    }

    fun stopTracking() {
        _isTracking.value = false
        // In this simple implementation, stopping tracking is handled by the Flow lifecycle
        // when the ViewModel is cleared or we can manage a Job if needed.
    }
}