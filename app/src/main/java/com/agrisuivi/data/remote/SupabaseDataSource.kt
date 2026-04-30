package com.agrisuivi.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseDataSource @Inject constructor(
    private val supabase: SupabaseClient
) {

    // ─── cycles_culture ──────────────────────────────────────────────────────

    suspend fun getAllCycles(): List<CycleCultureDto> =
        supabase.postgrest["cycles_culture"]
            .select()
            .decodeList()

    suspend fun getCycleById(id: String): CycleCultureDto =
        supabase.postgrest["cycles_culture"]
            .select { filter { eq("id", id) } }
            .decodeSingle()

    suspend fun insertCycle(dto: CycleCultureDto): CycleCultureDto =
        supabase.postgrest["cycles_culture"]
            .insert(dto) { select() }
            .decodeSingle()

    suspend fun updateCycle(dto: CycleCultureDto) {
        supabase.postgrest["cycles_culture"]
            .update(dto) { filter { eq("id", dto.id) } }
    }

    suspend fun deleteCycle(id: String) {
        supabase.postgrest["cycles_culture"]
            .delete { filter { eq("id", id) } }
    }

    // ─── photos_suivi ─────────────────────────────────────────────────────────

    suspend fun getPhotosForCycle(cycleId: String): List<PhotoSuiviDto> =
        supabase.postgrest["photos_suivi"]
            .select { filter { eq("cycle_culture_id", cycleId) } }
            .decodeList()

    suspend fun insertPhoto(dto: PhotoSuiviDto): PhotoSuiviDto =
        supabase.postgrest["photos_suivi"]
            .insert(dto) { select() }
            .decodeSingle()

    // ─── Storage ──────────────────────────────────────────────────────────────

    /**
     * Upload vers le bucket "cultures" et retourne l'URL publique.
     */
    suspend fun uploadImage(imageBytes: ByteArray, extension: String = "jpg"): String {
        val fileName = "${UUID.randomUUID()}.$extension"
        // Correction pour supabase-kt 3.x : upsert se configure dans la lambda options
        supabase.storage["cultures"].upload(fileName, imageBytes) {
            upsert = false
        }
        return supabase.storage["cultures"].publicUrl(fileName)
    }
}
