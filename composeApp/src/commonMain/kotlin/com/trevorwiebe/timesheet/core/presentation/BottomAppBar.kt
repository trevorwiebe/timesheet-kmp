package com.trevorwiebe.timesheet.core.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.trevorwiebe.timesheet.Route
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Punch,
        BottomNavItem.Report,
        BottomNavItem.Calendar
    )

    // TODO: Fix bug where we can't easily figure out current route because we are using new compose routes
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route as? Route?

    BottomNavigation(
        backgroundColor = tertiary,
        contentColor = Color.White
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.padding(16.dp),
                icon = {  },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: Route, val label: String) {
    data object Punch : BottomNavItem(Route.Punch,"Punch")
    data object Report : BottomNavItem(Route.Report, "Report")
    data object Calendar : BottomNavItem(Route.Calendar, "Calendar")
}