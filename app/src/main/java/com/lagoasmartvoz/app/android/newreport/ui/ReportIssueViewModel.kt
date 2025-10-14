package com.lagoasmartvoz.app.android.newreport.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReportIssueViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReportIssueUiState())
    val uiState: StateFlow<ReportIssueUiState> = _uiState


    fun onEvent(event: ReportIssueEvent) {
        when (event) {
            is ReportIssueEvent.OnImageAdded -> {
                val uris = event.uris
                if (uris.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(selectedImages = uris)
                }
            }
            is ReportIssueEvent.OnCategorySelected -> {
                val category = event.category
                if (category.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(category = category, categoryIsExpanded = false)
                }
            }
            is ReportIssueEvent.DescriptionChanged -> {
                val description = event.value
                if (description.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(description = description)
                }
            }
            is ReportIssueEvent.EmailChanged -> {
                val email = event.value
                if (email.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(email = email)
                }
            }
            is ReportIssueEvent.NameChanged -> {
                val name = event.value
                if (name.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(name = name)
                }
            }
            is ReportIssueEvent.RemoveImage -> {
                val imageUri = event.uri
                val updatedUris = _uiState.value.selectedImages.filter { it != imageUri }
                _uiState.value = _uiState.value.copy(selectedImages = updatedUris)
            }
            ReportIssueEvent.Submit -> TODO()
            ReportIssueEvent.OnDropdownClick -> {
                _uiState.value = _uiState.value.copy(categoryIsExpanded = _uiState.value.categoryIsExpanded.not())
            }
        }
    }
}