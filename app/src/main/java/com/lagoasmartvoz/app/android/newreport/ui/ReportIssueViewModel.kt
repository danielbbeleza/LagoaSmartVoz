package com.lagoasmartvoz.app.android.newreport.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReportIssueViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReportIssueUiState())
    val uiState: StateFlow<ReportIssueUiState> = _uiState


    fun onEvent(event: ReportIssueEvent) {
        when (event) {
            is ReportIssueEvent.AddImages -> TODO()
            is ReportIssueEvent.CategoryChanged -> TODO()
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
            is ReportIssueEvent.RemoveImage -> TODO()
            ReportIssueEvent.Submit -> TODO()
        }
    }
}