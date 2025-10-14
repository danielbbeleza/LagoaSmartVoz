package com.lagoasmartvoz.app.android.newreport.ui

import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ReportIssueViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ReportIssueUiState())
    val uiState: StateFlow<ReportIssueUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<ReportIssueEvent>()
    val uiEvent: SharedFlow<ReportIssueEvent> = _uiEvent


    fun onIntent(event: ReportIssueIntent) {
        when (event) {
            is ReportIssueIntent.OnImageAdded -> {
                val selectedImages: List<Uri> = _uiState.value.selectedImages + event.uri
                if (selectedImages.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(selectedImages = selectedImages)
                }
            }
            is ReportIssueIntent.OnCategorySelected -> {
                val category = event.category
                if (category.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(category = category, categoryIsExpanded = false)
                }
            }
            is ReportIssueIntent.DescriptionChanged -> {
                val description = event.value
                if (description.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(description = description)
                }
            }
            is ReportIssueIntent.EmailChanged -> {
                val email = event.value
                if (email.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(email = email)
                }
            }
            is ReportIssueIntent.NameChanged -> {
                val name = event.value
                if (name.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(name = name)
                }
            }
            is ReportIssueIntent.RemoveImage -> {
                val imageUri = event.uri
                val updatedUris = _uiState.value.selectedImages.filter { it != imageUri }
                _uiState.value = _uiState.value.copy(selectedImages = updatedUris)
            }
            ReportIssueIntent.Submit -> TODO()
            ReportIssueIntent.OnDropdownClick -> {
                _uiState.value = _uiState.value.copy(categoryIsExpanded = _uiState.value.categoryIsExpanded.not())
            }
            is ReportIssueIntent.PhotoTaken -> {
                _uiState.value = _uiState.value.copy(cameraImageUri = event.uri)
            }
            ReportIssueIntent.OnCameraSelected -> {
                val photoFile = File.createTempFile("photo_", ".jpg", application.cacheDir)
                val uri = FileProvider.getUriForFile(
                    application,
                    "${application.packageName}.fileprovider",
                    photoFile
                )
                _uiState.value = _uiState.value.copy(cameraImageUri = uri)
                viewModelScope.launch {
                    _uiEvent.emit(ReportIssueEvent.LaunchCamera(uri))
                }
            }
            ReportIssueIntent.OnGallerySelected -> {
                viewModelScope.launch {
                    _uiEvent.emit(ReportIssueEvent.LaunchGallery)
                }
            }
        }
    }
}