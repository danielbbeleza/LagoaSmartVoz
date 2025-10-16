package com.lagoasmartvoz.app.android.newreport.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.lagoasmartvoz.app.android.designsystem.dottedBorder
import com.lagoasmartvoz.app.android.widgets.LSVDropdown
import com.lagoasmartvoz.app.android.widgets.LSVRoundedTextField
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueScreen(
    viewModel: ReportIssueViewModel = viewModel(),
    onBackClick: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onIntent(ReportIssueIntent.OnImageAdded(it)) }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = state.value.cameraImageUri
        if (success && uri != null) {
            viewModel.onIntent(ReportIssueIntent.OnImageAdded(uri))
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ReportIssueEvent.LaunchCamera -> {
                    cameraLauncher.launch(event.uri)
                }
                ReportIssueEvent.LaunchGallery -> {
                    galleryLauncher.launch("image/*")
                }
                is ReportIssueEvent.Submit -> Toast.makeText(context, "Ocorrência reportada com sucesso", Toast.LENGTH_SHORT).show()
            }
        }
    }

    ShowForm(
        name = state.value.name.first,
        email = state.value.email.first,
        description = state.value.description.first,
        selectedCategory = state.value.category.first,
        categoryIsExpanded = state.value.categoryIsExpanded,
        selectedImages = state.value.selectedImages,
        isSubmitting = state.value.isSubmitting,
        errorMessage = state.value.errorMessage,
        onBackClick = onBackClick,
        onEvent = viewModel::onIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowForm(
    innerPadding: PaddingValues = PaddingValues(),
    name: String,
    email: String,
    description: String,
    selectedCategory: String,
    categoryIsExpanded: Boolean,
    selectedImages: List<Uri>,
    isSubmitting: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onEvent: (ReportIssueIntent) -> Unit
) {

    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {
            TopAppBar(
                title = { Text("Reportar Ocorrência") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }, content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    LSVRoundedTextField(
                        value = name,
                        onValueChange = { onEvent(ReportIssueIntent.NameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Nome"
                    )
                }

                item {
                    LSVRoundedTextField(
                        value = email,
                        onValueChange = { onEvent(ReportIssueIntent.EmailChanged(it)) },
                        placeholder = "Email",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    Box {
                        LSVRoundedTextField(
                            value = description,
                            onValueChange = { onEvent(ReportIssueIntent.DescriptionChanged(it)) },
                            placeholder = "Descrição",
                            modifier = Modifier
                                .fillMaxSize()
                                .height(250.dp)
                        )
                    }
                }

                item {
                    val categories = listOf("Iluminação", "Água", "Estrada", "Lixo", "Outro")

                    LSVDropdown(
                        selectedOption = selectedCategory,
                        options = categories,
                        placeholder = "Categoria",
                        isExpanded = categoryIsExpanded,
                        onOptionSelected = { onEvent(ReportIssueIntent.OnCategorySelected(it)) },
                        onExpanded = { onEvent(ReportIssueIntent.OnDropdownClick) },
                    )
                }

                item {
                    Text(
                        "Fotos",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(selectedImages) { uri ->
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                IconButton(
                                    onClick = { onEvent(ReportIssueIntent.RemoveImage(uri)) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(20.dp)
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remover",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        item {
                            AddPhotoButton(
                                onCameraSelected = { onEvent(ReportIssueIntent.OnCameraSelected) },
                                onGallerySelected = { onEvent(ReportIssueIntent.OnGallerySelected) }
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = { onEvent(ReportIssueIntent.Submit) },
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text("Enviar", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }

                if (errorMessage != null) {
                    item {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        })
}

@Composable
fun AddPhotoButton(
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit,
) {
    // Store camera output URI
    var showPickerDialog by remember { mutableStateOf(false) }

    // Gallery picker launcher


    // Show the picker dialog
    if (showPickerDialog) {
        AlertDialog(
            onDismissRequest = { showPickerDialog = false },
            title = { Text("Adicionar foto") },
            text = { Text("Escolhe de onde queres adicionar a imagem") },
            confirmButton = {},
            dismissButton = {}
        )

        AlertDialog(
            onDismissRequest = { showPickerDialog = false },
            confirmButton = {},
            text = {
                Column {
                    TextButton(onClick = {
                        showPickerDialog = false
                        onCameraSelected()
                    }) { Text("📸 Tirar foto") }


                    TextButton(onClick = {
                        showPickerDialog = false
                        onGallerySelected()
                    }) {
                        Text("🖼️ Escolher da galeria")
                    }
                }
            },
        )
    }

    // Your dotted border button
    Box(
        modifier = Modifier
            .size(100.dp)
            .dottedBorder(color = Color.Gray)
            .clip(RoundedCornerShape(12.dp))
            .clickable { showPickerDialog = true },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.CameraAlt, contentDescription = "Adicionar Foto", tint = Color.Gray)
    }
}