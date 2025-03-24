package com.trevorwiebe.timesheet.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomAppBar(navController: NavController) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val mainScreenRoutes = listOf(
        "com.trevorwiebe.timesheet.Route.Punch",
        "com.trevorwiebe.timesheet.Route.Report",
        "com.trevorwiebe.timesheet.Route.Calendar",
        "com.trevorwiebe.timesheet.Route.More"
    )

    if (mainScreenRoutes.contains(currentRoute)) {
        MainScreenBottomBar(
            currentRoute = currentRoute,
            onNavigate = {
                navController.navigate(it) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
    }
}