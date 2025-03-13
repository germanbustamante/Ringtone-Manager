package com.germandebustamante.ringtonemanager.domain.authorization.model

sealed interface LoginTypeBO {
    data class Default(val email: String, val password: String) : LoginTypeBO
    data class Google(val googleAccessToken: String): LoginTypeBO
}