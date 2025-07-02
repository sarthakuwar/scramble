package com.example.unscramble.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.unscramble.ui.GameScreen
import com.example.unscramble.ui.HomeScreen
import com.example.unscramble.ui.WordleScreen

/**
 * Add new screens by extending [Destination] and registering them in [NavGraph].
 */
sealed  class Destination(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Destination("home", "Home", Icons.Default.Home)
    object Wordle : Destination("wordle", "Wordle", Icons.Default.Person)
    object Unscramble : Destination("unscramble", "Unscramble", Icons.Default.PlayArrow)

    companion object {
        fun fromRoute(route: String?): Destination? = when (route) {
            Home.route -> Home
            Wordle.route -> Wordle
            Unscramble.route -> Unscramble
            else -> null
        }

        /** List of destinations that should appear in the bottom bar */
        val bottomBarItems = listOf(Home, Wordle, Unscramble)
    }
}

@Composable
fun AppNavigation(startDestination: Destination = Destination.Home) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBar(navController) },
//        bottomBar = {
//            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//            if (Destination.bottomBarItems.any { it.route == currentRoute }) {
//                BottomBar(navController)
//            }
//        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            start = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun NavGraph(
    navController: NavHostController,
    start: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = start.route,
        modifier = modifier
    ) {
        composable(Destination.Home.route) {
            HomeScreen { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
        composable(Destination.Wordle.route) {
            WordleScreen()
        }
        composable(Destination.Unscramble.route) {
            GameScreen()
        }
    }
}

/********************  Top Bar  ********************/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = Destination.fromRoute(backStackEntry?.destination?.route)
    val canNavigateBack = navController.previousBackStackEntry != null

    CenterAlignedTopAppBar(
        title = { Text(currentDestination?.title ?: "") },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

/********************  Bottom Bar  ********************/
//@Composable
//private fun BottomBar(navController: NavHostController) {
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = backStackEntry?.destination?.route
//
//    // Only show the bottom bar if currentRoute is in known destinations
//    val currentDestination = Destination.fromRoute(currentRoute)
//    if (currentDestination !in Destination.bottomBarItems) return
//
//    NavigationBar {
//        Destination.bottomBarItems.forEach { destination ->
//            NavigationBarItem(
//                selected = currentRoute == destination.route,
//                onClick = {
//                    navController.navigate(destination.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
//                icon = { Icon(destination.icon, contentDescription = destination.title) },
//                label = { Text(destination.title) }
//            )
//        }
//    }
//}


/***************  Placeholder screens  ****************/


