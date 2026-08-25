package com.example.gigshield.ui.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.data.model.InsuranceTier
import com.example.gigshield.theme.*

@Composable
fun PlanSelectionScreen(
    viewModel: PlanSelectionViewModel,
    onPlanSelected: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedTierToConfirm by remember { mutableStateOf<InsuranceTier?>(null) }
    var selectedTier by remember { mutableStateOf(uiState.selectedTier ?: InsuranceTier.INCOME_PROTECTOR) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBase)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Insurance Options",
                style = MaterialTheme.typography.labelLarge,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The Core Plans (Pay-Per-Day)",
                style = MaterialTheme.typography.displayMedium,
                color = TextHighContrast,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "These plans are billed only on the days you tap \"Start Work,\" deducted directly from your gig wallet or settled weekly.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (uiState.isLocked) {
                Surface(
                    color = SurfaceElevated.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceElevated),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "Locked", tint = ElectricBlue)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Tier Locked", style = MaterialTheme.typography.titleMedium, color = TextHighContrast)
                            Text("Your tier is locked for the week.", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- PLAN OPTIONS ---
            TierOptionCard(
                tier = InsuranceTier.SHIFT_SHIELD,
                isSelected = selectedTier == InsuranceTier.SHIFT_SHIELD,
                isRecommended = false,
                onSelect = { if (!uiState.isLocked) selectedTier = InsuranceTier.SHIFT_SHIELD }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TierOptionCard(
                tier = InsuranceTier.INCOME_PROTECTOR,
                isSelected = selectedTier == InsuranceTier.INCOME_PROTECTOR,
                isRecommended = true,
                onSelect = { if (!uiState.isLocked) selectedTier = InsuranceTier.INCOME_PROTECTOR }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
        
        Button(
            onClick = {
                selectedTierToConfirm = selectedTier
                showConfirmDialog = true
            },
            enabled = !uiState.isLocked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTier == InsuranceTier.INCOME_PROTECTOR) SafetyGreen else ElectricBlue,
                contentColor = androidx.compose.ui.graphics.Color(0xFF0A0A0A),
                disabledContainerColor = SurfaceElevated,
                disabledContentColor = TextMuted
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("CONFIRM WEEKLY LOCK-IN", style = MaterialTheme.typography.labelLarge)
        }
    }
    
    if (showConfirmDialog && selectedTierToConfirm != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = SurfaceCard,
            titleContentColor = TextHighContrast,
            textContentColor = TextMuted,
            title = { Text("Confirm Selection") },
            text = { Text("You are selecting ${selectedTierToConfirm?.displayName}. This selection will be locked for the next 7 days.") },
            confirmButton = {
                Button(
                    onClick = { 
                        viewModel.selectTier(selectedTierToConfirm!!)
                        showConfirmDialog = false
                        onPlanSelected()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SafetyGreen, contentColor = androidx.compose.ui.graphics.Color(0xFF0A0A0A))
                ) {
                    Text("Confirm", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun TierOptionCard(
    tier: InsuranceTier,
    isSelected: Boolean,
    isRecommended: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected && isRecommended) SafetyGreen else if (isSelected) ElectricBlue else SurfaceElevated
    val bgColor = if (isSelected) SurfaceCard else DeepBase
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelect() }
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tier.displayName.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = TextHighContrast
                )
                if (isRecommended) {
                    Surface(
                        color = SafetyGreen.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SafetyGreen)
                    ) {
                        Text(
                            text = "RECOMMENDED",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = SafetyGreen
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹${tier.dailyPremium}/day",
                style = MaterialTheme.typography.displayMedium,
                color = if (isRecommended) SafetyGreen else ElectricBlue
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // This is the loop that puts all the data into the cards themselves!
            tier.getBenefitsList().forEach { benefit ->
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Included",
                        tint = if (isRecommended) SafetyGreen else ElectricBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = benefit,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextHighContrast,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
