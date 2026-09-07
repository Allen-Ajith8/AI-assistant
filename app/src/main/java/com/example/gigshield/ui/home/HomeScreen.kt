package com.example.gigshield.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.theme.*

import androidx.compose.material.icons.filled.Menu

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToPlanSelection: () -> Unit,
    onNavigateToScoreboard: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ChatScreenContent(
        uiState = uiState,
        onInputTextChanged = viewModel::onInputTextChanged,
        onSendMessage = viewModel::sendMessage,
        onOpenDrawer = onOpenDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenContent(
    uiState: ChatUiState,
    onInputTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "AI Assistant", 
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextHighContrast
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextHighContrast)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepBase,
                    titleContentColor = TextHighContrast
                )
            )
        },
        containerColor = DeepBase
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Chat Messages List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.messages) { message ->
                    ChatMessageBubble(message = message)
                }
            }

            // Input Area
            ChatInputArea(
                inputText = uiState.inputText,
                onInputTextChanged = onInputTextChanged,
                onSendMessage = onSendMessage
            )
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    val isUser = message.isUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // AI Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SafetyGreen),
                contentAlignment = Alignment.Center
            ) {
                Text("AI", color = DeepBase, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

        Box(
            modifier = Modifier
                .weight(1f, fill = false)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .background(if (isUser) SurfaceCard else SurfaceElevated)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = message.text,
                color = TextHighContrast,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp
            )
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(12.dp))
            // User Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ElectricBlue),
                contentAlignment = Alignment.Center
            ) {
                Text("U", color = TextHighContrast, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ChatInputArea(
    inputText: String,
    onInputTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepBase)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = onInputTextChanged,
            placeholder = { Text("Message AI Assistant...", color = TextMuted) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceCard,
                unfocusedContainerColor = SurfaceCard,
                focusedBorderColor = SafetyGreen,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = TextHighContrast,
                unfocusedTextColor = TextHighContrast,
                cursorColor = SafetyGreen
            ),
            maxLines = 4
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        IconButton(
            onClick = onSendMessage,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (inputText.isNotBlank()) SafetyGreen else SurfaceElevated)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = if (inputText.isNotBlank()) DeepBase else TextMuted
            )
        }
    }
}
