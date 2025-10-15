package com.lagoasmartvoz.app.android.newreport.ui

import android.net.Uri

data class ReportIssueUiState(
    val name: Pair<String, ReportIssueUiError?> = ("" to null),
    val email: Pair<String, ReportIssueUiError?> = ("" to null),
    val description: Pair<String, ReportIssueUiError?> = ("" to null),
    val category: Pair<String, ReportIssueUiError?> = ("" to null),
    val categoryIsExpanded: Boolean = false,
    val selectedImages: List<Uri> = emptyList(),
    val cameraImageUri: Uri? = null,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ReportIssueIntent {
    data class NameChanged(val value: String) : ReportIssueIntent
    data class EmailChanged(val value: String) : ReportIssueIntent
    data class DescriptionChanged(val value: String) : ReportIssueIntent
    data class OnCategorySelected(val category: String) : ReportIssueIntent
    data class OnImageAdded(val uri: Uri) : ReportIssueIntent
    data class RemoveImage(val uri: Uri) : ReportIssueIntent
    object Submit : ReportIssueIntent
    object OnDropdownClick : ReportIssueIntent
    object OnCameraSelected : ReportIssueIntent
    object OnGallerySelected : ReportIssueIntent
    data class PhotoTaken(val uri: Uri) : ReportIssueIntent
}

sealed interface ReportIssueEvent {
    data class LaunchCamera(val uri: Uri) : ReportIssueEvent
    object LaunchGallery : ReportIssueEvent
    data class Submit(
        val name: String,
        val email: String,
        val description: String,
        val category: String,
        val selectedImages: List<Uri>
    ) : ReportIssueEvent
}

enum class NewReportCategory {
    STREET, BUILDING, WATER, ELECTRICITY, OTHER
}

data class ReportIssueUiError(val message: String)
