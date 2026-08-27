package br.com.curso.portalrickandmorty.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.curso.portalrickandmorty.data.dao.UserDao
import br.com.curso.portalrickandmorty.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val dao: UserDao) : ViewModel() {

    private val repository = UserRepository(dao)

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _password.value = newValue
    }

    fun login() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Preencha todos os campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Mock login logic
                repository.login(name = "User Rick", email = _email.value)
                _loginSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao fazer login"
            } finally {
                _isLoading.value = false
            }
        }
    }
}