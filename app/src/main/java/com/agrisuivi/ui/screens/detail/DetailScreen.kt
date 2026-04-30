package com.agrisuivi.ui.screens.detail

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agrisuivi.domain.model.PhotoSuivi
import com.agrisuivi.domain.model.StatutCulture
import com.agrisuivi.ui.theme.ForestGreen
import com.agrisuivi.ui.theme.SunflowerYellow
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH)

    LaunchedEffect(uiState.isDeleted) { if (uiState.isDeleted) onBack() }

    var showPhotoDialog by remember { mutableStateOf(false) }
    var pendingUri by remember { mutableStateOf<Uri?>(null) }
    var photoDescription by remember { mutableStateOf("") }
    var signaleParasite by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { pendingUri = it; showPhotoDialog = true }
    }

    if (showPhotoDialog && pendingUri != null) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Ajouter une photo de suivi") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = photoDescription, onValueChange = { photoDescription = it },
                        label = { Text("Description (optionnel)") },
                        modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = signaleParasite, onCheckedChange = { signaleParasite = it })
                        Text("Signaler un problème (parasites, maladies)")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    pendingUri?.let { viewModel.addFollowUpPhoto(it, photoDescription, signaleParasite) }
                    showPhotoDialog = false; photoDescription = ""; signaleParasite = false
                }) { Text("Ajouter") }
            },
            dismissButton = { TextButton(onClick = { showPhotoDialog = false }) { Text("Annuler") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.cycle?.variete ?: "Détail") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour") }
                },
                actions = {
                    uiState.cycle?.let { cycle ->
                        if (cycle.statut == StatutCulture.EN_COURS) {
                            IconButton(onClick = { onEdit(cycle.id) }) { Icon(Icons.Default.Edit, "Modifier") }
                        }
                        IconButton(onClick = viewModel::deleteCycle) {
                            Icon(Icons.Default.Delete, "Supprimer", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            uiState.cycle == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Culture introuvable")
            }
            else -> {
                val cycle = uiState.cycle!!
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                ) {
                    // Hero image
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        if (cycle.photoUrl != null) {
                            AsyncImage(model = cycle.photoUrl, contentDescription = cycle.variete,
                                       contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        } else {
                            Text("🌱", style = MaterialTheme.typography.displayLarge,
                                 modifier = Modifier.align(Alignment.Center))
                        }
                        val (badgeColor, badgeText) = when (cycle.statut) {
                            StatutCulture.EN_COURS -> MaterialTheme.colorScheme.primary to "En cours"
                            StatutCulture.RECOLTEE -> Color(0xFF4CAF50) to "✅ Récoltée"
                            StatutCulture.ECHEC    -> MaterialTheme.colorScheme.error to "❌ Échec"
                        }
                        Surface(color = badgeColor, shape = RoundedCornerShape(bottomEnd = 12.dp),
                                modifier = Modifier.align(Alignment.TopStart)) {
                            Text(badgeText, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                 color = Color.White, style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(cycle.variete, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Parcelle ${cycle.numeroParcelle}", style = MaterialTheme.typography.bodyLarge,
                             color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(16.dp))

                        InfoRow("📅 Date de semis", cycle.dateSemis.format(dateFormatter))
                        InfoRow("🎯 Récolte théorique", cycle.dateRecolteTheorique.format(dateFormatter))
                        InfoRow("⏳ Durée de croissance", "${cycle.dureesCroissanceJours} jours")
                        if (cycle.notes.isNotBlank()) InfoRow("📝 Notes", cycle.notes)

                        Spacer(Modifier.height(20.dp))
                        Text("Progression de croissance", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { cycle.progressionCroissance },
                            modifier = Modifier.fillMaxWidth().height(10.dp).clip(MaterialTheme.shapes.medium),
                            color = if (cycle.joursRestants <= 7) SunflowerYellow else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        val countdownText = when {
                            cycle.joursRestants < 0  -> "⚠️ En retard de ${-cycle.joursRestants} jours"
                            cycle.joursRestants == 0L -> "🎉 Récolte aujourd'hui !"
                            cycle.joursRestants <= 7  -> "🚜 Récolte dans ${cycle.joursRestants} jour(s)"
                            else                      -> "⏳ ${cycle.joursRestants} jours avant la récolte"
                        }
                        Text(countdownText, style = MaterialTheme.typography.bodyMedium)

                        if (cycle.statut == StatutCulture.EN_COURS) {
                            Spacer(Modifier.height(24.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(
                                    onClick = { imagePicker.launch("image/*") },
                                    modifier = Modifier.weight(1f),
                                    enabled = !uiState.isUploadingPhoto
                                ) {
                                    if (uiState.isUploadingPhoto) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                    } else {
                                        Icon(Icons.Default.AddAPhoto, null)
                                        Spacer(Modifier.width(6.dp))
                                        Text("Suivi photo")
                                    }
                                }
                                Button(onClick = viewModel::markAsHarvested, modifier = Modifier.weight(1f)) {
                                    Icon(Icons.Default.TaskAlt, null)
                                    Spacer(Modifier.width(6.dp))
                                    Text("Récolter")
                                }
                            }
                        }

                        if (uiState.photos.isNotEmpty()) {
                            Spacer(Modifier.height(24.dp))
                            Text("📸 Historique photos (${uiState.photos.size})", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(12.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(uiState.photos) { photo -> PhotoHistoryItem(photo) }
                            }
                        }

                        uiState.error?.let {
                            Spacer(Modifier.height(12.dp))
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun PhotoHistoryItem(photo: PhotoSuivi) {
    Column(modifier = Modifier.width(120.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(120.dp).clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant)) {
            AsyncImage(model = photo.photoUrl, contentDescription = photo.description,
                       contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            if (photo.signaleParasite) {
                Surface(color = MaterialTheme.colorScheme.error, shape = CircleShape,
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(22.dp)) {
                    Text("⚠", style = MaterialTheme.typography.labelSmall,
                         modifier = Modifier.wrapContentSize())
                }
            }
        }
        if (photo.description.isNotBlank()) {
            Text(photo.description, style = MaterialTheme.typography.labelSmall, maxLines = 2)
        }
    }
}
