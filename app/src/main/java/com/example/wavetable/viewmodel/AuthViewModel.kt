package com.example.wavetable.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wavetable.helpers.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    val authRepository = AuthRepository()

    val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    init {
        // Observe auth state changes
        viewModelScope.launch {
            authRepository.getAuthState().collect { user ->
                _authState.value = if (user != null) {
                    AuthState.Success("User authenticated")
                } else {
                    AuthState.Initial
                }
            }
        }
    }

    sealed class AuthState {
        object Initial : AuthState()
        object Loading : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = authRepository.login(email, password)
                result.fold(
                    onSuccess = {
                        _authState.value = AuthState.Success("Login successful")
                    },
                    onFailure = {
                        _authState.value = AuthState.Error(it.message ?: "Login failed")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = authRepository.register(email, password)
                result.fold(
                    onSuccess = {
                        _authState.value = AuthState.Success("Registration successful")
                    },
                    onFailure = {
                        _authState.value = AuthState.Error(it.message ?: "Registration failed")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun logout(onResult: (Boolean) -> Unit) {
        try{
            authRepository.logout()
            onResult(true)
            _authState.value = AuthState.Initial
        } catch (e: Exception) {
            onResult(false)
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.changePassword(currentPassword, newPassword)
            result.fold(
                onSuccess = {
                    _authState.value = AuthState.Success("Password changed successfully")
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Password change failed")
                }
            )
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = authRepository.forgotPassword(email)
                result.fold(
                    onSuccess = {
                        _authState.value = AuthState.Success("Password reset email sent")
                    },
                    onFailure = {
                        _authState.value = AuthState.Error(it.message ?: "Failed to send password reset email")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to send password reset email")
            }
        }

    }

}