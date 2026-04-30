package com.agrisuivi.data.remote

import com.agrisuivi.domain.model.CycleCulture
import com.agrisuivi.domain.model.PhotoSuivi
import com.agrisuivi.domain.model.StatutCulture
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CycleCultureDto(
    val id: String = "",
    val variete: String,
    @SerialName("numero_parcelle") val numeroParcelle: String,
    @SerialName("date_semis") val dateSemis: String,
    @SerialName("duree_croissance_jours") val dureesCroissanceJours: Int,
    @SerialName("photo_url") val photoUrl: String? = null,
    val statut: String = "EN_COURS",
    val notes: String = "",
    @SerialName("created_at") val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain() = CycleCulture(
        id = id,
        variete = variete,
        numeroParcelle = numeroParcelle,
        dateSemis = LocalDate.parse(dateSemis),
        dureesCroissanceJours = dureesCroissanceJours,
        photoUrl = photoUrl,
        statut = StatutCulture.valueOf(statut),
        notes = notes,
        createdAt = createdAt
    )
}

fun CycleCulture.toDto() = CycleCultureDto(
    id = id,
    variete = variete,
    numeroParcelle = numeroParcelle,
    dateSemis = dateSemis.toString(),
    dureesCroissanceJours = dureesCroissanceJours,
    photoUrl = photoUrl,
    statut = statut.name,
    notes = notes,
    createdAt = createdAt
)

@Serializable
data class PhotoSuiviDto(
    val id: String = "",
    @SerialName("cycle_culture_id") val cycleCultureId: String,
    @SerialName("photo_url") val photoUrl: String,
    val description: String = "",
    @SerialName("signale_parasite") val signaleParasite: Boolean = false,
    @SerialName("created_at") val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain() = PhotoSuivi(
        id = id,
        cycleCultureId = cycleCultureId,
        photoUrl = photoUrl,
        description = description,
        signaleParasite = signaleParasite,
        createdAt = createdAt
    )
}

fun PhotoSuivi.toDto() = PhotoSuiviDto(
    id = id,
    cycleCultureId = cycleCultureId,
    photoUrl = photoUrl,
    description = description,
    signaleParasite = signaleParasite,
    createdAt = createdAt
)
