package com.agrisuivi.domain.model

data class PhotoSuivi(
    val id: String = "",
    val cycleCultureId: String,
    val photoUrl: String,
    val description: String = "",
    val signaleParasite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
