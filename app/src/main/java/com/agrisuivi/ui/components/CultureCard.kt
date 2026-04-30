package com.agrisuivi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.agrisuivi.domain.model.CycleCulture
import com.agrisuivi.ui.theme.ForestGreenContainer
import com.agrisuivi.ui.theme.SunflowerYellow

@Composable
fun CultureCard(
    cycle: CycleCulture,
    onClick: () -> Unit,
    onHarvest: () -> Unit,
    onDelete: () -> Unit,
    highlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) MaterialTheme.colorScheme.tertiaryContainer
                             else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Photo circulaire
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(ForestGreenContainer),
                contentAlignment = Alignment.Center
            ) {
                if (cycle.photoUrl != null) {
                    AsyncImage(
                        model = cycle.photoUrl,
                        contentDescription = cycle.variete,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("🌱", style = MaterialTheme.typography.headlineMedium)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(cycle.variete, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Parcelle ${cycle.numeroParcelle}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))

                // Barre de progression
                LinearProgressIndicator(
                    progress = { cycle.progressionCroissance },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(MaterialTheme.shapes.small),
                    color = if (cycle.joursRestants <= 7) SunflowerYellow
                            else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(Modifier.height(4.dp))

                // Compte à rebours
                val countdownText = when {
                    cycle.joursRestants < 0  -> "⚠️ En retard de ${-cycle.joursRestants}j"
                    cycle.joursRestants == 0L -> "🎉 Récolte aujourd'hui !"
                    cycle.joursRestants <= 7  -> "🚜 Dans ${cycle.joursRestants} jour(s)"
                    else                      -> "⏳ ${cycle.joursRestants} jours restants"
                }
                Text(
                    countdownText,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (cycle.joursRestants <= 7) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onSurface
                )
            }

            // Menu contextuel
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Marquer récoltée") },
                        leadingIcon = { Icon(Icons.Default.TaskAlt, null) },
                        onClick = { onHarvest(); showMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Supprimer", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                        },
                        onClick = { onDelete(); showMenu = false }
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryBanner(readyCount: Int, totalActive: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(value = totalActive.toString(), label = "Parcelles\nactives",      emoji = "🌱")
            VerticalDivider(modifier = Modifier.height(48.dp))
            StatItem(value = readyCount.toString(),  label = "Récoltes\ncette semaine", emoji = "🚜")
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, emoji: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, style = MaterialTheme.typography.titleLarge)
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelLarge,
             color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}
