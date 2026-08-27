package br.com.curso.portalrickandmorty.screens.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.curso.portalrickandmorty.data.local.dao.EpisodeDao
import br.com.curso.portalrickandmorty.domain.model.Episode
import br.com.curso.portalrickandmorty.repository.EpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EpisodeViewModel(private val dao: EpisodeDao) : ViewModel() {

    private val repository = EpisodeRepository(dao)

    val episodes: StateFlow<List<Episode>> =
        repository.getAllEpisodes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _episode = MutableStateFlow<Episode?>(null)
    val episode: StateFlow<Episode?> = _episode.asStateFlow()

    init {
        loadEpisodes()
    }

    fun loadEpisodes() {
        // ...
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.syncEpisodes()
            } catch (exception: Exception) {
                if (episodes.value.isEmpty()) {
                    _error.value = exception.message ?: "Erro ao carregar episódios."
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadEpisodeById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _episode.value = repository.getEpisodeById(id)
            } catch (exception: Exception) {
                _error.value = exception.message ?: "Erro ao carregar detalhes."
            } finally {
                _isLoading.value = false
            }
        }
    }
}