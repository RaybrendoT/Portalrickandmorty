package br.com.curso.portalrickandmorty.screens.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.curso.portalrickandmorty.data.local.dao.CharacterDao
import br.com.curso.portalrickandmorty.domain.model.Character
import br.com.curso.portalrickandmorty.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel(private val dao: CharacterDao) : ViewModel() {

    private val repository = CharacterRepository(dao)

    val characters: StateFlow<List<Character>> =
        repository.getAllCharacters().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()

    private val _character =
        MutableStateFlow<Character?>(null)

    val character: StateFlow<Character?> =
        _character.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        // ... (existing code)
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.syncCharacters()
            } catch (exception: Exception) {
                // If we have characters in cache, don't show error to the user
                if (characters.value.isEmpty()) {
                    _error.value = exception.message ?: "Erro ao carregar personagens."
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCharacterById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getCharacterById(id)
                if (result != null) {
                    _character.value = result
                } else {
                    _error.value = "Personagem não encontrado."
                }
            } catch (exception: Exception) {
                _error.value = exception.message ?: "Erro ao carregar detalhes."
            } finally {
                _isLoading.value = false
            }
        }
    }
}