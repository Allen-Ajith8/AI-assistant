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
import com.example.gigshield.ui.home.HomeScreen
import com.example.gigshield.ui.home.HomeViewModel
import com.example.gigshield.ui.permissions.PermissionsScreen
import com.example.gigshield.ui.plan.PlanSelectionScreen
import com.example.gigshield.ui.plan.PlanSelectionViewModel
import com.example.gigshield.ui.score.ScoreboardScreen
import com.example.gigshield.ui.score.ScoreboardViewModel

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Home)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
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
                        onNavigateToPermissions = { backStack.add(Permissions) }
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
            }
        )
    }
}
