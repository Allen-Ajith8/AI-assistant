package com.example.gigshield.ui.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    
    // We'll mock the selection state here for UI demonstration
    var selectedTier by remember { mutableStateOf(uiState.selectedTier ?: InsuranceTier.INCOME_PROTECTOR) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBase)
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Insurance",
            style = MaterialTheme.typography.labelLarge,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select Your Daily Protection",
            style = MaterialTheme.typography.displayMedium,
            color = TextHighContrast
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
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

        // Tier 1 Card
        TierCard(
            tier = InsuranceTier.SHIFT_SHIELD,
            isSelected = selectedTier == InsuranceTier.SHIFT_SHIELD,
            isRecommended = false,
            onSelect = { if (!uiState.isLocked) selectedTier = InsuranceTier.SHIFT_SHIELD }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Tier 2 Card (Recommended)
        TierCard(
            tier = InsuranceTier.INCOME_PROTECTOR,
            isSelected = selectedTier == InsuranceTier.INCOME_PROTECTOR,
            isRecommended = true,
            onSelect = { if (!uiState.isLocked) selectedTier = InsuranceTier.INCOME_PROTECTOR }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                selectedTierToConfirm = selectedTier
                showConfirmDialog = true
            },
            enabled = !uiState.isLocked,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SafetyGreen,
                contentColor = DeepBase,
                disabledContainerColor = SurfaceElevated,
                disabledContentColor = TextMuted
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("CONFIRM WEEKLY LOCK-IN", style = MaterialTheme.typography.labelLarge)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
    
    if (showConfirmDialog && selectedTierToConfirm != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = SurfaceCard,
            titleContentColor = TextHighContrast,
            textContentColor = TextMuted,
            title = { Text("Confirm Selection") },
            text = { Text("You are selecting ${selectedTierToConfirm?.displayName}. This selection will be locked for the next 7 days and cannot be changed.") },
            confirmButton = {
                Button(
                    onClick = { 
                        viewModel.selectTier(selectedTierToConfirm!!)
                        showConfirmDialog = false
                        onPlanSelected()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SafetyGreen, contentColor = DeepBase)
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
fun TierCard(
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
            
            tier.getBenefitsList().forEach { benefit ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Included",
                        tint = if (isRecommended) SafetyGreen else ElectricBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = benefit,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
