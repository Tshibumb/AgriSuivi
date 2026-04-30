package com.agrisuivi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import com.agrisuivi.ui.screens.addedit.AddEditCultureScreen
import com.agrisuivi.ui.screens.dashboard.DashboardScreen
import com.agrisuivi.ui.screens.detail.DetailScreen

sealed class Screen(val route: String) {
    object Dashboard     : Screen("dashboard")
    object AddCulture    : Screen("culture/add")
    object EditCulture   : Screen("culture/edit/{cycleId}") {
        fun createRoute(id: String) = "culture/edit/$id"
    }
    object DetailCulture : Screen("culture/detail/{cycleId}") {
        fun createRoute(id: String) = "culture/detail/$id"
    }
}

@Composable
fun AgriNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onAddCulture   = { navController.navigate(Screen.AddCulture.route) },
                onCultureClick = { id -> navController.navigate(Screen.DetailCulture.createRoute(id)) }
            )
        }

        composable(Screen.AddCulture.route) {
            AddEditCultureScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.EditCulture.route,
            arguments = listOf(navArgument("cycleId") { type = NavType.StringType })
        ) {
            AddEditCultureScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.DetailCulture.route,
            arguments = listOf(navArgument("cycleId") { type = NavType.StringType })
        ) {
            DetailScreen(
                onBack = { navController.popBackStack() },
                onEdit = { id -> navController.navigate(Screen.EditCulture.createRoute(id)) }
            )
        }
    }
}
