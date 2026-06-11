package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO

data class RingtonePageCursor(val popularity: Int, val documentId: String)

data class RingtoneListPage(
    val ringtones: List<RingtoneBO>,
    val nextCursor: RingtonePageCursor?,
    val hasMore: Boolean,
)

interface RingtoneListRemoteDataSource {
    suspend fun fetchPopularRingtones(
        pageSize: Int,
        cursor: RingtonePageCursor? = null,
    ): Either<ErrorBO, RingtoneListPage>
}
