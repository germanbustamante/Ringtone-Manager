package com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl

import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GoogleSignInUseCase
import com.germandebustamante.ringtonemanager.domain.error.CustomError

class GoogleSignInImpl(
    private val authenticationRepository: AuthenticationRepository,
) : GoogleSignInUseCase {
    override suspend fun invoke(googleTokenId: String): CustomError? = authenticationRepository.googleSignIn(googleTokenId)
}