package com.trevorwiebe.timesheet

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trevorwiebe.timesheet.authentication.presentation.auth.SignInScreen
import com.trevorwiebe.timesheet.punch.presentation.PunchScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.SignIn
        ){
            composable<Route.Punch> {
                PunchScreen()
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