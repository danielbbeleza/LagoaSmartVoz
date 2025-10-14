package com.lagoasmartvoz.app.android.newreport.ui

import android.net.Uri

data class ReportIssueUiState(
    val name: String = "",
    val email: String = "",
    val description: String = "",
    val category: String = "",
    val categoryIsExpanded: Boolean = false,
    val selectedImages: List<Uri> = emptyList(),
    val cameraImageUri: Uri? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
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
    data class PhotoTaken(val uri: Uri): ReportIssueIntent
}

sealed interface ReportIssueEvent {
    data class LaunchCamera(val uri: Uri) : ReportIssueEvent
    object LaunchGallery : ReportIssueEvent

}

enum class NewReportCategory {
    STREET, BUILDING, WATER, ELECTRICITY, OTHER
}
