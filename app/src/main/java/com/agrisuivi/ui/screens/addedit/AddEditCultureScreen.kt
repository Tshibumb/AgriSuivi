package com.agrisuivi.ui.screens.addedit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agrisuivi.ui.theme.ForestGreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCultureScreen(
    onBack: () -> Unit,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    LaunchedEffect(uiState.isSaved) { if (uiState.isSaved) onBack() }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.onPhotoSelected(it)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateSemis.toEpochDay() * 86_400_000L
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onDateSemisChange(LocalDate.ofEpochDay(millis / 86_400_000L))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.variete.isEmpty()) "Nouvelle culture" else "Modifier") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sélecteur photo circulaire
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable { imagePicker.launch("image/*") }
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.photoUri != null) {
                    AsyncImage(
                        model = uiState.photoUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddAPhoto, null,
                             tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                        Text("Ajouter photo", style = MaterialTheme.typography.labelLarge,
                             color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            OutlinedTextField(
                value = uiState.variete, onValueChange = viewModel::onVarieteChange,
                label = { Text("Variété de plante *") },
                placeholder = { Text("ex: Tomates Roma, Chou vert…") },
                modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium, singleLine = true
            )
            OutlinedTextField(
                value = uiState.numeroParcelle, onValueChange = viewModel::onNumeroParcelleChange,
                label = { Text("Numéro de parcelle *") },
                placeholder = { Text("ex: A1, B3…") },
                modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium, singleLine = true
            )

            // Date de semis
            OutlinedTextField(
                value = uiState.dateSemis.format(dateFormatter),
                onValueChange = {},
                label = { Text("Date de semis *") },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                enabled = false,
                shape = MaterialTheme.shapes.medium,
                trailingIcon = { Icon(Icons.Default.CalendarToday, null, tint = MaterialTheme.colorScheme.primary) }
            )

            OutlinedTextField(
                value = uiState.dureeCroissance, onValueChange = viewModel::onDureeCroissanceChange,
                label = { Text("Durée de croissance (jours) *") },
                placeholder = { Text("ex: 90") },
                modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true
            )
            OutlinedTextField(
                value = uiState.notes, onValueChange = viewModel::onNotesChange,
                label = { Text("Notes") },
                placeholder = { Text("Observations, traitements…") },
                modifier = Modifier.fillMaxWidth().height(100.dp), shape = MaterialTheme.shapes.medium
            )

            uiState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                shape = MaterialTheme.shapes.large
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp,
                                              color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Enregistrer", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
