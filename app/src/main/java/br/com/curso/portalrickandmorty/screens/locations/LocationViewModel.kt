package br.com.curso.portalrickandmorty.screens.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.curso.portalrickandmorty.data.local.dao.LocationDao
import br.com.curso.portalrickandmorty.domain.model.Location
import br.com.curso.portalrickandmorty.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocationViewModel(private val dao: LocationDao) : ViewModel() {

    private val repository = LocationRepository(dao)

    val locations: StateFlow<List<Location>> =
        repository.getAllLocations().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location.asStateFlow()

    init {
        loadLocations()
    }

    fun loadLocations() {
        // ...
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.syncLocations()
            } catch (exception: Exception) {
                if (locations.value.isEmpty()) {
                    _error.value = exception.message ?: "Erro ao carregar locais."
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLocationById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // I need to add getById to LocationRepository
                _location.value = repository.getLocationById(id)
            } catch (exception: Exception) {
                _error.value = exception.message ?: "Erro ao carregar detalhes."
            } finally {
                _isLoading.value = false
            }
        }
    }
}