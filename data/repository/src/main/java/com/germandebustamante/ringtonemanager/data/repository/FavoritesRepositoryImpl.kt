package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.di.DispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.datasource.FavoritesRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FavoritesRepositoryImpl(
    private val remoteDataSource: FavoritesRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider,
) : FavoritesRepository {

    override fun observeFavoriteIds(userId: String): Flow<Either<ErrorBO, Set<String>>> =
        remoteDataSource.observeFavoriteIds(userId).flowOn(dispatcherProvider.io)

    override suspend fun toggleFavorite(
        userId: String,
        ringtoneId: String,
        isFavorite: Boolean,
    ): Either<ErrorBO, Unit> = withContext(dispatcherProvider.io) {
        if (isFavorite) {
            remoteDataSource.removeFavorite(userId, ringtoneId)
        } else {
            remoteDataSource.addFavorite(userId, ringtoneId)
        }
    }
}
