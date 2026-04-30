package com.agrisuivi.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agrisuivi.ui.components.CultureCard
import com.agrisuivi.ui.components.SummaryBanner
import com.agrisuivi.ui.theme.ForestGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddCulture: () -> Unit,
    onCultureClick: (String) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🌿 AgriSuivi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Tableau de bord", style = MaterialTheme.typography.bodySmall,
                             color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualiser")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddCulture,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Nouvelle culture") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                uiState.error != null -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⚠️ ${uiState.error}", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = viewModel::refresh) { Text("Réessayer") }
                }

                uiState.activeCycles.isEmpty() -> Column(
                    modifier = Modifier.align(Alignment.Center).padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🌾", style = MaterialTheme.typography.displayMedium)
                    Spacer(Modifier.height(16.dp))
                    Text("Aucune culture en cours", style = MaterialTheme.typography.headlineSmall)
                    Text("Appuyez sur + pour commencer.", style = MaterialTheme.typography.bodyMedium,
                         color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                else -> LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SummaryBanner(
                            readyCount = uiState.readyThisWeek.size,
                            totalActive = uiState.activeCycles.size
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    if (uiState.readyThisWeek.isNotEmpty()) {
                        item {
                            Text("🚜 Prêtes cette semaine", style = MaterialTheme.typography.titleMedium,
                                 color = MaterialTheme.colorScheme.tertiary)
                        }
                        items(uiState.readyThisWeek, key = { it.id }) { cycle ->
                            CultureCard(
                                cycle = cycle,
                                onClick = { onCultureClick(cycle.id) },
                                onHarvest = { viewModel.markAsHarvested(cycle.id) },
                                onDelete = { viewModel.deleteCycle(cycle.id) },
                                highlight = true
                            )
                        }
                        item { Spacer(Modifier.height(4.dp)) }
                    }

                    item {
                        Text("🌱 Parcelles actives", style = MaterialTheme.typography.titleMedium)
                    }
                    items(
                        uiState.activeCycles.filter { !it.isPretPourRecolte },
                        key = { it.id }
                    ) { cycle ->
                        CultureCard(
                            cycle = cycle,
                            onClick = { onCultureClick(cycle.id) },
                            onHarvest = { viewModel.markAsHarvested(cycle.id) },
                            onDelete = { viewModel.deleteCycle(cycle.id) }
                        )
                    }
                }
            }
        }
    }
}
