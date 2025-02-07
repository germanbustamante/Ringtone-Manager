package com.germandebustamante.ringtonemanager.data.repository

import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository

class RingtoneListRepositoryImpl(
    remoteDataSource: RingtoneListRemoteDataSource,
) : RingtoneListRepository {

    override val popularRingtones = remoteDataSource.getPopularRingtones()

}