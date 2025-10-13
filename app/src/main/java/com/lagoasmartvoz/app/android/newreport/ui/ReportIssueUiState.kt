package com.lagoasmartvoz.app.android.newreport.ui

import android.net.Uri

data class ReportIssueUiState(
    val name: String = "",
    val email: String = "",
    val description: String = "",
    val category: String = "",
    val selectedImages: List<Uri> = emptyList(),
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ReportIssueEvent {
    data class NameChanged(val value: String) : ReportIssueEvent
    data class EmailChanged(val value: String) : ReportIssueEvent
    data class DescriptionChanged(val value: String) : ReportIssueEvent
    data class CategoryChanged(val value: String) : ReportIssueEvent
    data class AddImages(val uris: List<Uri>) : ReportIssueEvent
    data class RemoveImage(val uri: Uri) : ReportIssueEvent
    object Submit : ReportIssueEvent
}

enum class NewReportCategory {
    STREET, BUILDING, WATER, ELECTRICITY, OTHER
}
