package com.example.votingapp.core.ui

import android.content.res.Resources
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.votingapp.core.navigation.Destination
import com.example.votingapp.core.navigation.navigateToCreateVote
import com.example.votingapp.core.navigation.navigateToHome
import com.example.votingapp.core.navigation.navigateToJoinVote
import com.example.votingapp.core.navigation.navigateToLogin
import com.example.votingapp.core.navigation.navigateToRegister
import com.example.votingapp.core.navigation.navigateToWelcome
import kotlinx.coroutines.CoroutineScope


@Composable
fun rememberAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AppState {
    return remember(
        navController,
        windowSizeClass,
        coroutineScope
    ) {
        AppState(
            navController = navController,
            windowSizeClass = windowSizeClass,
            coroutineScope = coroutineScope
        )
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    val coroutineScope: CoroutineScope,

    ) {
    val currentDestinationAsState: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentDestination: Destination?
        @Composable get() = Destination.values().asList()
            .filter { it.route == currentDestinationAsState?.route }.firstOrNull()

    val shouldShowBottomBar: Boolean
        @Composable get() = Destination.values().asList()
            .filter { it.isBottomBarTab }.map { it.route }
            .contains(currentDestinationAsState?.route)

    val shouldShowTopAppBar: Boolean
        @Composable get() = Destination.values().asList()
            .filter { it.isTopBarTab }.map { it.route }.contains(currentDestinationAsState?.route)

    val destinationWithBottomBars: List<Destination>
        get() = Destination.values().asList()
            .filter { it.isBottomBarTab && it.isTopLevelDestination }

    val destinationWithTopBar: List<Destination>
        get() = Destination.values().asList()
            .filter { it.isTopBarTab }


    fun navigateToTopLevelDestination(destination: Destination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (destination) {
            Destination.Welcome -> navController.navigateToWelcome(topLevelNavOptions)
            Destination.REGISTER -> navController.navigateToRegister(topLevelNavOptions)
            Destination.LOGIN -> navController.navigateToLogin(topLevelNavOptions)
            Destination.Home -> navController.navigateToHome(topLevelNavOptions)
            Destination.CreateVoting -> navController.navigateToCreateVote(topLevelNavOptions)
            Destination.JoinVoting -> navController.navigateToJoinVote(topLevelNavOptions)
            Destination.ChoseVote -> navController.navigateToCreateVote(topLevelNavOptions)
        }

    }

    fun onBackClick() {
        navController.popBackStack()
    }

}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
