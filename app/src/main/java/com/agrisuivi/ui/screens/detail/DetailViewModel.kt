package com.agrisuivi.ui.screens.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrisuivi.data.repository.CultureRepository
import com.agrisuivi.domain.model.CycleCulture
import com.agrisuivi.domain.model.PhotoSuivi
import com.agrisuivi.domain.model.StatutCulture
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = true,
    val cycle: CycleCulture? = null,
    val photos: List<PhotoSuivi> = emptyList(),
    val error: String? = null,
    val isUploadingPhoto: Boolean = false,
    val isDeleted: Boolean = false
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CultureRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cycleId: String = checkNotNull(savedStateHandle["cycleId"])
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val cycle = repository.getCycleById(cycleId)
                val photos = repository.getPhotos(cycleId)
                _uiState.update { it.copy(isLoading = false, cycle = cycle, photos = photos) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun addFollowUpPhoto(uri: Uri, description: String, signaleParasite: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploadingPhoto = true, error = null) }
            try {
                val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
                    ?: throw IllegalStateException("Impossible de lire l'image")
                val url = repository.uploadImage(bytes)
                repository.addPhoto(PhotoSuivi(
                    id = UUID.randomUUID().toString(),
                    cycleCultureId = cycleId,
                    photoUrl = url,
                    description = description,
                    signaleParasite = signaleParasite
                ))
                val photos = repository.getPhotos(cycleId)
                _uiState.update { it.copy(isUploadingPhoto = false, photos = photos) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUploadingPhoto = false, error = e.message) }
            }
        }
    }

    fun markAsHarvested() = updateStatut(StatutCulture.RECOLTEE)
    fun markAsFailed()    = updateStatut(StatutCulture.ECHEC)

    private fun updateStatut(statut: StatutCulture) {
        viewModelScope.launch {
            runCatching { repository.updateStatut(cycleId, statut) }
                .onSuccess { loadData() }
                .onFailure { _uiState.update { s -> s.copy(error = it.message) } }
        }
    }

    fun deleteCycle() {
        viewModelScope.launch {
            runCatching { repository.deleteCycle(cycleId) }
                .onSuccess { _uiState.update { it.copy(isDeleted = true) } }
                .onFailure { _uiState.update { s -> s.copy(error = it.message) } }
        }
    }
}
