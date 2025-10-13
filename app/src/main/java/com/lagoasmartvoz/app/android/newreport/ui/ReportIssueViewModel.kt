package com.lagoasmartvoz.app.android.newreport.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReportIssueViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReportIssueUiState())
    val uiState: StateFlow<ReportIssueUiState> = _uiState


    fun onEvent(event: ReportIssueEvent) {
        when(event) {
            is ReportIssueEvent.AddImages -> TODO()
            is ReportIssueEvent.CategoryChanged -> TODO()
            is ReportIssueEvent.DescriptionChanged -> TODO()
            is ReportIssueEvent.EmailChanged -> TODO()
            is ReportIssueEvent.NameChanged -> TODO()
            is ReportIssueEvent.RemoveImage -> TODO()
            ReportIssueEvent.Submit -> TODO()
        }
    }
}