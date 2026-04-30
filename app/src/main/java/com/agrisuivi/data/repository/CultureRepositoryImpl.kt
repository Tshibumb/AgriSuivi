package com.agrisuivi.data.repository

import android.util.Log
import com.agrisuivi.data.remote.SupabaseDataSource
import com.agrisuivi.data.remote.toDto
import com.agrisuivi.domain.model.CycleCulture
import com.agrisuivi.domain.model.PhotoSuivi
import com.agrisuivi.domain.model.StatutCulture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CultureRepositoryImpl @Inject constructor(
    private val remote: SupabaseDataSource
) : CultureRepository {

    override fun observeAllCycles(): Flow<List<CycleCulture>> = flow {
        emit(remote.getAllCycles().map { it.toDomain() })
    }.catch { e ->
        Log.e("CultureRepository", "Erreur lors de la récupération des cycles", e)
        emit(emptyList())
    }

    override suspend fun getCycleById(id: String): CycleCulture? =
        runCatching { remote.getCycleById(id).toDomain() }.getOrNull()

    override suspend fun createCycle(cycle: CycleCulture): CycleCulture =
        remote.insertCycle(cycle.toDto()).toDomain()

    override suspend fun updateCycle(cycle: CycleCulture) =
        remote.updateCycle(cycle.copy(id = cycle.id).toDto())

    override suspend fun deleteCycle(id: String) =
        remote.deleteCycle(id)

    override suspend fun updateStatut(id: String, statut: StatutCulture) {
        val cycle = getCycleById(id) ?: return
        remote.updateCycle(cycle.copy(statut = statut).toDto())
    }

    override suspend fun getPhotos(cycleId: String): List<PhotoSuivi> =
        remote.getPhotosForCycle(cycleId).map { it.toDomain() }

    override suspend fun addPhoto(photo: PhotoSuivi): PhotoSuivi =
        remote.insertPhoto(photo.toDto()).toDomain()

    override suspend fun uploadImage(imageBytes: ByteArray): String =
        remote.uploadImage(imageBytes)
}
