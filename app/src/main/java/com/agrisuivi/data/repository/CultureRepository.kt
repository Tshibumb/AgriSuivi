package com.agrisuivi.data.repository

import com.agrisuivi.domain.model.CycleCulture
import com.agrisuivi.domain.model.PhotoSuivi
import com.agrisuivi.domain.model.StatutCulture
import kotlinx.coroutines.flow.Flow

interface CultureRepository {
    fun observeAllCycles(): Flow<List<CycleCulture>>
    suspend fun getCycleById(id: String): CycleCulture?
    suspend fun createCycle(cycle: CycleCulture): CycleCulture
    suspend fun updateCycle(cycle: CycleCulture)
    suspend fun deleteCycle(id: String)
    suspend fun updateStatut(id: String, statut: StatutCulture)
    suspend fun getPhotos(cycleId: String): List<PhotoSuivi>
    suspend fun addPhoto(photo: PhotoSuivi): PhotoSuivi
    suspend fun uploadImage(imageBytes: ByteArray): String
}
