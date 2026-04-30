package com.agrisuivi.domain.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class CycleCulture(
    val id: String = "",
    val variete: String,
    val numeroParcelle: String,
    val dateSemis: LocalDate,
    val dureesCroissanceJours: Int,
    val photoUrl: String? = null,
    val statut: StatutCulture = StatutCulture.EN_COURS,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    val dateRecolteTheorique: LocalDate
        get() = dateSemis.plusDays(dureesCroissanceJours.toLong())

    val joursRestants: Long
        get() = ChronoUnit.DAYS.between(LocalDate.now(), dateRecolteTheorique)

    val progressionCroissance: Float
        get() {
            val totalDays = dureesCroissanceJours.toFloat()
            val elapsed = ChronoUnit.DAYS.between(dateSemis, LocalDate.now()).toFloat()
            return (elapsed / totalDays).coerceIn(0f, 1f)
        }

    val isPretPourRecolte: Boolean
        get() = joursRestants in -7..7 && statut == StatutCulture.EN_COURS
}

enum class StatutCulture { EN_COURS, RECOLTEE, ECHEC }
