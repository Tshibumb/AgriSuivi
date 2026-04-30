package com.agrisuivi.ui.screens.addedit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrisuivi.data.repository.CultureRepository
import com.agrisuivi.domain.model.CycleCulture
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AddEditUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val variete: String = "",
    val numeroParcelle: String = "",
    val dateSemis: LocalDate = LocalDate.now(),
    val dureeCroissance: String = "",
    val photoUri: Uri? = null,
    val notes: String = ""
)

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: CultureRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cycleId: String? = savedStateHandle["cycleId"]
    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    init { cycleId?.let { loadCycle(it) } }

    private fun loadCycle(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cycle = repository.getCycleById(id)
            if (cycle != null) {
                _uiState.update { s -> s.copy(
                    isLoading = false,
                    variete = cycle.variete,
                    numeroParcelle = cycle.numeroParcelle,
                    dateSemis = cycle.dateSemis,
                    dureeCroissance = cycle.dureesCroissanceJours.toString(),
                    notes = cycle.notes
                )}
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onVarieteChange(v: String)          = _uiState.update { it.copy(variete = v) }
    fun onNumeroParcelleChange(v: String)   = _uiState.update { it.copy(numeroParcelle = v) }
    fun onDateSemisChange(v: LocalDate)     = _uiState.update { it.copy(dateSemis = v) }
    fun onDureeCroissanceChange(v: String)  = _uiState.update { it.copy(dureeCroissance = v) }
    fun onPhotoSelected(uri: Uri?)          = _uiState.update { it.copy(photoUri = uri) }
    fun onNotesChange(v: String)            = _uiState.update { it.copy(notes = v) }

    fun save() {
        val state = _uiState.value
        val duree = state.dureeCroissance.toIntOrNull()
        if (state.variete.isBlank() || state.numeroParcelle.isBlank() || duree == null || duree <= 0) {
            _uiState.update { it.copy(error = "Veuillez remplir tous les champs obligatoires.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                var photoUrl: String? = null
                state.photoUri?.let { uri ->
                    context.contentResolver.openInputStream(uri)?.readBytes()?.let { bytes ->
                        photoUrl = repository.uploadImage(bytes)
                    }
                }
                val cycle = CycleCulture(
                    id = cycleId ?: "",
                    variete = state.variete.trim(),
                    numeroParcelle = state.numeroParcelle.trim(),
                    dateSemis = state.dateSemis,
                    dureesCroissanceJours = duree,
                    photoUrl = photoUrl,
                    notes = state.notes.trim()
                )
                if (cycleId == null) repository.createCycle(cycle)
                else repository.updateCycle(cycle)
                _uiState.update { it.copy(isSaved = true, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
