import re

file_path = "app/src/main/java/com/example/gigshield/Navigation.kt"

with open(file_path, "r") as f:
    content = f.read()

# Define the 25 items
items = [
    ("Dashboard", "Home", "Icons.Default.Home"),
    ("Insurance Plans", "PlanSelection", "Icons.Default.Shield"),
    ("Vehicle Garage", "VehicleGarage", "Icons.Default.DirectionsCar"),
    ("Start Work", "StartWork", "Icons.Default.PlayArrow"),
    ("Work Activity Detection", "WorkActivity", "Icons.Default.Speed"),
    ("iQOO / Device Status", "DeviceStatus", "Icons.Default.PhoneAndroid"),
    ("Gig Platform", "GigPlatform", "Icons.Default.Work"),
    ("My Policy", "MyPolicy", "Icons.Default.Description"),
    ("Daily Premium", "DailyPremium", "Icons.Default.AttachMoney"),
    ("Payments", "Payments", "Icons.Default.Payment"),
    ("Coverage & Benefits", "CoverageBenefits", "Icons.Default.HealthAndSafety"),
    ("Work History", "WorkHistory", "Icons.Default.History"),
    ("Analytics", "Analytics", "Icons.Default.Insights"),
    ("Report Accident (Voice)", "VoiceClaim", "Icons.Default.Mic"),
    ("My Claims", "MyClaims", "Icons.Default.FactCheck"),
    ("Upload Evidence", "UploadEvidence", "Icons.Default.UploadFile"),
    ("AI Verification", "AiVerification", "Icons.Default.Verified"),
    ("Fraud/Risk Status", "FraudRisk", "Icons.Default.Warning"),
    ("Payouts", "Payouts", "Icons.Default.AccountBalance"),
    ("Emergency SOS", "EmergencySos", "Icons.Default.Sos"),
    ("Emergency Contacts", "EmergencyContacts", "Icons.Default.Contacts"),
    ("Notifications", "Notifications", "Icons.Default.Notifications"),
    ("Profile & KYC", "ProfileKyc", "Icons.Default.Person"),
    ("Help & Support", "HelpSupport", "Icons.Default.Help"),
    ("Settings & Privacy", "SettingsPrivacy", "Icons.Default.Settings")
]

imports = """import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.*
import com.example.gigshield.ui.work.*
import com.example.gigshield.ui.insurance.*
import com.example.gigshield.ui.claims.*
import com.example.gigshield.ui.profile.*
import com.example.gigshield.ui.garage.VehicleGarageScreen
import com.example.gigshield.ui.claim.VoiceClaimScreen
"""

drawer_items_code = """
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
"""
for label, key, icon in items:
    drawer_items_code += f"""
                    item {{
                        NavigationDrawerItem(
                            label = {{ Text("{label}") }},
                            selected = currentKey == {key},
                            icon = {{ Icon(androidx.compose.material.icons.{icon}, contentDescription = null) }},
                            onClick = {{ 
                                if (currentKey != {key}) backStack.add({key})
                                scope.launch {{ drawerState.close() }}
                            }},
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = com.example.gigshield.theme.SurfaceElevated,
                                selectedTextColor = com.example.gigshield.theme.ElectricBlue,
                                selectedIconColor = com.example.gigshield.theme.ElectricBlue,
                                unselectedContainerColor = Color.Transparent, 
                                unselectedTextColor = com.example.gigshield.theme.TextHighContrast,
                                unselectedIconColor = com.example.gigshield.theme.TextMuted
                            )
                        )
                    }}
"""
drawer_items_code += "                }\n"

entries_code = """
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
"""

for label, key, icon in items:
    if key not in ["Home", "PlanSelection"]:
        screen_name = key + "Screen"
        entries_code += f"""
                    entry<{key}> {{
                        {screen_name}(onBack = {{ backStack.removeLastOrNull() }})
                    }}
"""

# Now inject these into the content.
# Replace imports
content = content.replace("import androidx.compose.ui.graphics.Color", "import androidx.compose.ui.graphics.Color\n" + imports)

# Replace drawer items
# Find the start of the drawer items block after HorizontalDivider
pattern = r"(HorizontalDivider[^\n]*\n\s*val currentKey = backStack\.lastOrNull\(\)\n)([\s\S]*?)(?=\s*\}\s*\n\s*\}\s*\n\s*\)\s*\{\s*\n\s*Surface)"
content = re.sub(pattern, r"\1" + drawer_items_code, content)

# Replace entryProvider entries
entry_pattern = r"(entryProvider\s*\{)([\s\S]*?)(?=\s*\n\s*\}\s*\n\s*\)\s*\n\s*\}\s*\n\s*\}\s*\n\})"
content = re.sub(entry_pattern, r"\1" + entries_code, content)

with open(file_path, "w") as f:
    f.write(content)
print("Updated Navigation.kt successfully!")
