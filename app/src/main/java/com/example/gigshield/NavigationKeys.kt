package com.example.gigshield

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data object PlanSelection : NavKey

@Serializable
data object Scoreboard : NavKey

@Serializable
data object Permissions : NavKey
