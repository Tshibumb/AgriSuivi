package com.agrisuivi.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrisuivi.data.repository.CultureRepository
import com.agrisuivi.domain.model.CycleCulture
import com.agrisuivi.domain.model.StatutCulture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val cycles: List<CycleCulture> = emptyList(),
    val error: String? = null
) {
    val readyThisWeek: List<CycleCulture>
        get() = cycles.filter { it.isPretPourRecolte }

    val activeCycles: List<CycleCulture>
        get() = cycles
            .filter { it.statut == StatutCulture.EN_COURS }
            .sortedBy { it.joursRestants }
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: CultureRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    private val _error     = MutableStateFlow<String?>(null)

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.observeAllCycles(),
        _isLoading,
        _error
    ) { cycles, loading, error ->
        DashboardUiState(isLoading = loading, cycles = cycles, error = error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur inconnue"
                _isLoading.value = false
            }
        }
    }

    fun deleteCycle(id: String) {
        viewModelScope.launch {
            runCatching { repository.deleteCycle(id) }
                .onFailure { _error.value = it.message }
        }
    }

    fun markAsHarvested(id: String) {
        viewModelScope.launch {
            runCatching { repository.updateStatut(id, StatutCulture.RECOLTEE) }
                .onFailure { _error.value = it.message }
        }
    }
}
