package com.trevorwiebe.timesheet

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.trevorwiebe.timesheet.calendar.presentation.CalendarScreen
import com.trevorwiebe.timesheet.core.presentation.BottomNavigationBar
import com.trevorwiebe.timesheet.more.presentation.MoreScreen
import com.trevorwiebe.timesheet.punch.presentation.PunchScreen
import com.trevorwiebe.timesheet.report.presentation.ReportScreen
import com.trevorwiebe.timesheet.signin.presentation.auth.SignInScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {

        val navController = rememberNavController()
        val deviceCutoutPadding = WindowInsets.displayCutout.asPaddingValues()
        val imePadding = WindowInsets.ime.asPaddingValues()

        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) {

            val finishedPadding = if (imePadding.calculateBottomPadding() == 0.dp) {
                it
            } else {
                imePadding
            }
            NavHost(
                modifier = Modifier
                    .padding(deviceCutoutPadding)
                    .padding(finishedPadding),
                navController = navController,
                startDestination = Route.SignIn
            ){

                // Sign In
                composable<Route.SignIn> {
                    SignInScreen(
                        onSignInSuccessful = {
                            navController.navigate(Route.Punch) {
                                popUpTo(Route.SignIn) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                // Main
                composable<Route.Punch> {
                    PunchScreen()
                }

                composable<Route.Report> {
                    ReportScreen(
                        onReportClick = { startTime, endTime ->
                            navController.navigate(Route.ReportDetail(startTime, endTime))
                        },
                    )
                }

                composable<Route.ReportDetail> { entry ->
                    val args = entry.toRoute<Route.ReportDetail>()
                    PunchScreen(
                        startDate = args.startTime,
                        endDate = args.endTime,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Route.Calendar> {
                    CalendarScreen()
                }

                composable<Route.More> {
                    MoreScreen(
                        onSignOut = {
                            navController.navigate(Route.SignIn) {
                                popUpTo(Route.Punch) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}