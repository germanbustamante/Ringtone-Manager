package com.germandebustamante.ringtonemanager.data.remote.firestore

import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RingtoneListFirestoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : RingtoneListRemoteDataSource {

    override fun getPopularRingtones(): Flow<List<RingtoneBO>> =
        getPopularRingtoneList().map { it.map { it.toDomain() } }

    private fun getPopularRingtoneList(): Flow<List<RingtoneDTO>> =

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
    }
}