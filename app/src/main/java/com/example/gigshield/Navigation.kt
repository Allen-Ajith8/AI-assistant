package com.example.gigshield

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.compose.ui.platform.LocalContext
import com.example.gigshield.GigShieldApplication
import com.example.gigshield.ui.SharedViewModel
import com.example.gigshield.ui.components.GradientWaveBackground
import com.example.gigshield.ui.home.HomeScreen
import com.example.gigshield.ui.home.HomeViewModel
import com.example.gigshield.ui.permissions.PermissionsScreen
import com.example.gigshield.ui.plan.PlanSelectionScreen
import com.example.gigshield.ui.plan.PlanSelectionViewModel
import com.example.gigshield.ui.score.ScoreboardScreen
import com.example.gigshield.ui.score.ScoreboardViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.gigshield.ui.garage.VehicleGarageScreen
import com.example.gigshield.ui.claim.VoiceClaimScreen
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.*
import com.example.gigshield.ui.work.*
import com.example.gigshield.ui.insurance.*
import com.example.gigshield.ui.claims.*
import com.example.gigshield.ui.profile.*
import com.example.gigshield.ui.garage.VehicleGarageScreen
import com.example.gigshield.ui.claim.VoiceClaimScreen


@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Home)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = com.example.gigshield.theme.SurfaceCard,
                drawerContentColor = com.example.gigshield.theme.TextHighContrast
            ) {
                Text(
                    "GigGuard Menu", 
                    style = MaterialTheme.typography.titleLarge,
                    color = com.example.gigshield.theme.ElectricBlue,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider(color = com.example.gigshield.theme.SurfaceElevated)
                
                val currentKey = backStack.lastOrNull()

                LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {

                    item {
                        NavigationDrawerItem(
                            label = { Text("Dashboard") },
                            selected = currentKey == Home,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Home, contentDescription = null) },
                            onClick = { 
                                if (currentKey != Home) backStack.add(Home)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Insurance Plans") },
                            selected = currentKey == PlanSelection,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Shield, contentDescription = null) },
                            onClick = { 
                                if (currentKey != PlanSelection) backStack.add(PlanSelection)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Vehicle Garage") },
                            selected = currentKey == VehicleGarage,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.DirectionsCar, contentDescription = null) },
                            onClick = { 
                                if (currentKey != VehicleGarage) backStack.add(VehicleGarage)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Start Work") },
                            selected = currentKey == StartWork,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.PlayArrow, contentDescription = null) },
                            onClick = { 
                                if (currentKey != StartWork) backStack.add(StartWork)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Work Activity Detection") },
                            selected = currentKey == WorkActivity,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Speed, contentDescription = null) },
                            onClick = { 
                                if (currentKey != WorkActivity) backStack.add(WorkActivity)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("iQOO / Device Status") },
                            selected = currentKey == DeviceStatus,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.PhoneAndroid, contentDescription = null) },
                            onClick = { 
                                if (currentKey != DeviceStatus) backStack.add(DeviceStatus)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Gig Platform") },
                            selected = currentKey == GigPlatform,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Work, contentDescription = null) },
                            onClick = { 
                                if (currentKey != GigPlatform) backStack.add(GigPlatform)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("My Policy") },
                            selected = currentKey == MyPolicy,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Description, contentDescription = null) },
                            onClick = { 
                                if (currentKey != MyPolicy) backStack.add(MyPolicy)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Daily Premium") },
                            selected = currentKey == DailyPremium,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.AttachMoney, contentDescription = null) },
                            onClick = { 
                                if (currentKey != DailyPremium) backStack.add(DailyPremium)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Payments") },
                            selected = currentKey == Payments,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Payment, contentDescription = null) },
                            onClick = { 
                                if (currentKey != Payments) backStack.add(Payments)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Coverage & Benefits") },
                            selected = currentKey == CoverageBenefits,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.HealthAndSafety, contentDescription = null) },
                            onClick = { 
                                if (currentKey != CoverageBenefits) backStack.add(CoverageBenefits)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Work History") },
                            selected = currentKey == WorkHistory,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.History, contentDescription = null) },
                            onClick = { 
                                if (currentKey != WorkHistory) backStack.add(WorkHistory)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Analytics") },
                            selected = currentKey == Analytics,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Insights, contentDescription = null) },
                            onClick = { 
                                if (currentKey != Analytics) backStack.add(Analytics)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Report Accident (Voice)") },
                            selected = currentKey == VoiceClaim,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Mic, contentDescription = null) },
                            onClick = { 
                                if (currentKey != VoiceClaim) backStack.add(VoiceClaim)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("My Claims") },
                            selected = currentKey == MyClaims,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.FactCheck, contentDescription = null) },
                            onClick = { 
                                if (currentKey != MyClaims) backStack.add(MyClaims)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Upload Evidence") },
                            selected = currentKey == UploadEvidence,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.UploadFile, contentDescription = null) },
                            onClick = { 
                                if (currentKey != UploadEvidence) backStack.add(UploadEvidence)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("AI Verification") },
                            selected = currentKey == AiVerification,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Verified, contentDescription = null) },
                            onClick = { 
                                if (currentKey != AiVerification) backStack.add(AiVerification)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Fraud/Risk Status") },
                            selected = currentKey == FraudRisk,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Warning, contentDescription = null) },
                            onClick = { 
                                if (currentKey != FraudRisk) backStack.add(FraudRisk)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Payouts") },
                            selected = currentKey == Payouts,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.AccountBalance, contentDescription = null) },
                            onClick = { 
                                if (currentKey != Payouts) backStack.add(Payouts)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Emergency SOS") },
                            selected = currentKey == EmergencySos,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Sos, contentDescription = null) },
                            onClick = { 
                                if (currentKey != EmergencySos) backStack.add(EmergencySos)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Emergency Contacts") },
                            selected = currentKey == EmergencyContacts,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Contacts, contentDescription = null) },
                            onClick = { 
                                if (currentKey != EmergencyContacts) backStack.add(EmergencyContacts)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Notifications") },
                            selected = currentKey == Notifications,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Notifications, contentDescription = null) },
                            onClick = { 
                                if (currentKey != Notifications) backStack.add(Notifications)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Profile & KYC") },
                            selected = currentKey == ProfileKyc,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Person, contentDescription = null) },
                            onClick = { 
                                if (currentKey != ProfileKyc) backStack.add(ProfileKyc)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Help & Support") },
                            selected = currentKey == HelpSupport,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Help, contentDescription = null) },
                            onClick = { 
                                if (currentKey != HelpSupport) backStack.add(HelpSupport)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            label = { Text("Settings & Privacy") },
                            selected = currentKey == SettingsPrivacy,
                            icon = { Icon(androidx.compose.material.icons.Icons.Default.Settings, contentDescription = null) },
                            onClick = { 
                                if (currentKey != SettingsPrivacy) backStack.add(SettingsPrivacy)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }
                }

            }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            color = Color.Transparent
        ) {
            val context = LocalContext.current
            val app = context.applicationContext as GigShieldApplication
            val sharedViewModel: SharedViewModel = viewModel<SharedViewModel>(
                factory = SharedViewModel.provideFactory(
                    app.container.workSessionRepository,
                    app.container.claimRepository
                )
            )
            
            GradientWaveBackground {
                NavDisplay(
                    backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<Home> {
                        val viewModel: HomeViewModel = viewModel()
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateToPlanSelection = { backStack.add(PlanSelection) },
                            onNavigateToScoreboard = { backStack.add(Scoreboard) },
                            onNavigateToPermissions = { backStack.add(Permissions) },
                            onOpenDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                    entry<PlanSelection> {
                        val viewModel: PlanSelectionViewModel = viewModel()
                        PlanSelectionScreen(
                            viewModel = viewModel,
                            onPlanSelected = { backStack.removeLastOrNull() }
                        )
                    }
                    entry<Scoreboard> {
                        val viewModel: ScoreboardViewModel = viewModel()
                        ScoreboardScreen(
                            viewModel = viewModel,
                            onNavigateHome = { backStack.removeLastOrNull() }
                        )
                    }
                    entry<Permissions> {
                        PermissionsScreen(
                            onBack = { backStack.removeLastOrNull() },
                            onContinue = { backStack.removeLastOrNull() }
                        )
                    }

                    entry<VehicleGarage> {
                        VehicleGarageScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<StartWork> {
                        StartWorkScreen(viewModel = sharedViewModel, onBack = { backStack.removeLastOrNull() })
                    }

                    entry<WorkActivity> {
                        WorkActivityScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<DeviceStatus> {
                        DeviceStatusScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<GigPlatform> {
                        GigPlatformScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<MyPolicy> {
                        MyPolicyScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<DailyPremium> {
                        DailyPremiumScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<Payments> {
                        PaymentsScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<CoverageBenefits> {
                        CoverageBenefitsScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<WorkHistory> {
                        WorkHistoryScreen(viewModel = sharedViewModel, onBack = { backStack.removeLastOrNull() })
                    }

                    entry<Analytics> {
                        AnalyticsScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<VoiceClaim> {
                        VoiceClaimScreen(viewModel = sharedViewModel, onBack = { backStack.removeLastOrNull() })
                    }

                    entry<MyClaims> {
                        MyClaimsScreen(viewModel = sharedViewModel, onBack = { backStack.removeLastOrNull() })
                    }

                    entry<UploadEvidence> {
                        UploadEvidenceScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<AiVerification> {
                        AiVerificationScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<FraudRisk> {
                        FraudRiskScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<Payouts> {
                        PayoutsScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<EmergencySos> {
                        EmergencySosScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<EmergencyContacts> {
                        EmergencyContactsScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<Notifications> {
                        NotificationsScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<ProfileKyc> {
                        ProfileKycScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<HelpSupport> {
                        HelpSupportScreen(onBack = { backStack.removeLastOrNull() })
                    }

                    entry<SettingsPrivacy> {
                        SettingsPrivacyScreen(onBack = { backStack.removeLastOrNull() })
                    }

                }
            )
            }
        }
    }
}
