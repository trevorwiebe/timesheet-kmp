package com.trevorwiebe.timesheet

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trevorwiebe.timesheet.authentication.presentation.auth.SignInScreen
import com.trevorwiebe.timesheet.core.presentation.BottomNavigationBar
import com.trevorwiebe.timesheet.punch.presentation.PunchScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {

        val navController = rememberNavController()
        val deviceCutoutPadding = WindowInsets.displayCutout.asPaddingValues()
        val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()

        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) {
            NavHost(
                modifier = Modifier
                    .padding(it)
                    .padding(navigationBarPadding)
                    .padding(deviceCutoutPadding),
                navController = navController,
                startDestination = Route.SignIn
            ){
                composable<Route.Punch> {
                    PunchScreen()
                }

                composable<Route.Report> {

                }

                composable<Route.Calendar> {

                }

                composable<Route.SignIn> {
                    SignInScreen(
                        onSignInSuccessful = {
                            navController.navigate(Route.Punch)
                        }
                    )
                }
            }
        }
    }
}