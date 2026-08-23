package com.example.gigshield.ui.plan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gigshield.data.model.InsuranceTier
import com.example.gigshield.theme.GigShieldTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanSelectionScreen(
    viewModel: PlanSelectionViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select Plan") })
        },
        bottomBar = {
            BottomAppBar(containerColor = MaterialTheme.colorScheme.background) {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    enabled = uiState.selectedTier != null && uiState.selectedTier != uiState.currentTier && !uiState.isLocked
                ) {
                    Text("Confirm Selection")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (uiState.isLocked) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Plan locked until ${uiState.lockExpiryDate}",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            InsuranceTier.entries.forEach { tier ->
                val isSelected = uiState.selectedTier == tier || (uiState.selectedTier == null && uiState.currentTier == tier)
                PlanCard(
                    tier = tier,
                    isSelected = isSelected,
                    isLocked = uiState.isLocked,
                    onClick = { viewModel.selectTier(tier) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Confirm Plan") },
                text = { Text("Your plan will be locked for 7 days. Continue?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.confirmSelection()
                        showConfirmDialog = false
                    }) {
                        Text("Continue")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun PlanCard(
    tier: InsuranceTier,
    isSelected: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val accentColor = if (tier == InsuranceTier.INCOME_PROTECTOR) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, borderColor),
        onClick = onClick,
        enabled = !isLocked
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (tier == InsuranceTier.INCOME_PROTECTOR) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "RECOMMENDED",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Text(
                text = tier.displayName,
                style = MaterialTheme.typography.titleLarge,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "₹${tier.dailyPremium}/day",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tier.getBenefitDescription(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewPlanSelectionScreen() {
    GigShieldTheme {
        PlanSelectionScreen(PlanSelectionViewModel(), onBack = {})
    }
}
