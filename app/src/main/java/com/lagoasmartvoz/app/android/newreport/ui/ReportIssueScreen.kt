package com.lagoasmartvoz.app.android.newreport.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.lagoasmartvoz.app.android.designsystem.dottedBorder
import com.lagoasmartvoz.app.android.widgets.LSVRoundedTextField
import com.lagoasmartvoz.app.android.widgets.RoundedDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueScreen(
    viewModel: ReportIssueViewModel = viewModel { ReportIssueViewModel() },
    onBackClick: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState()

    ShowForm(
        name = state.value.name,
        email = state.value.email,
        description = state.value.description,
        selectedCategory = state.value.category,
        categoryIsExpanded = state.value.categoryIsExpanded,
        selectedImages = state.value.selectedImages,
        isSubmitting = state.value.isSubmitting,
        errorMessage = state.value.errorMessage,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
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
    onEvent: (ReportIssueEvent) -> Unit
) {

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            onEvent(ReportIssueEvent.OnImageAdded(uris))
        }
    }

    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {
            TopAppBar(
                title = { Text("Reportar Ocorrência") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
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
                        onValueChange = { onEvent(ReportIssueEvent.NameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Nome"
                    )
                }

                item {
                    LSVRoundedTextField(
                        value = email,
                        onValueChange = { onEvent(ReportIssueEvent.EmailChanged(it)) },
                        placeholder = "Email",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    Box {
                        LSVRoundedTextField(
                            value = description,
                            onValueChange = { onEvent(ReportIssueEvent.DescriptionChanged(it)) },
                            placeholder = "Descrição",
                            modifier = Modifier
                                .fillMaxSize()
                                .height(250.dp)
                        )
                    }
                }

                item {
                    val categories = listOf("Iluminação", "Água", "Estrada", "Lixo", "Outro")

                    RoundedDropdown(
                        selectedOption = selectedCategory,
                        options = categories,
                        placeholder = "Categoria",
                        isExpanded = categoryIsExpanded,
                        onOptionSelected = { onEvent(ReportIssueEvent.OnCategorySelected(it)) },
                        onExpanded = { onEvent(ReportIssueEvent.OnDropdownClick) },
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
                                    onClick = { onEvent(ReportIssueEvent.RemoveImage(uri)) },
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
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .dottedBorder()
                                    .clickable { photoPickerLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar Foto", tint = Color.Gray)
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = { onEvent(ReportIssueEvent.Submit) },
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


data class ReportData(
    val name: String,
    val email: String,
    val description: String,
    val category: String,
    val imageUris: List<Uri>
)