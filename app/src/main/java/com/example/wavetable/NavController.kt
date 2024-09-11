package com.example.wavetable

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wavetable.data.Datasource
import com.example.wavetable.screens.AccountUI
import com.example.wavetable.screens.ItemsUI
import com.example.wavetable.screens.LoginLMNT
import com.example.wavetable.screens.MainUI
import com.example.wavetable.screens.WishlistUI
import com.example.wavetable.ui.theme.AppTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.screens.CartUI
import com.example.wavetable.screens.RegisterApp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()

    val colorScheme = MaterialTheme.colorScheme
    val primaryColor = colorScheme.primaryContainer
    val backgroundColor = colorScheme.background

    val isDarkTheme = !isSystemInDarkTheme()

    // Apply status bar and navigation bar colors dynamically
    DisposableEffect(systemUiController, primaryColor, backgroundColor, isDarkTheme) {
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = isDarkTheme
        )
        systemUiController.setNavigationBarColor(
            color = primaryColor,
            darkIcons = isDarkTheme
        )

        onDispose { }
    }


    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val showSearch = currentRoute == "home"
    val showTopAndBottomNav = currentRoute != "login" && currentRoute != "register"

    Scaffold(
        modifier = modifier,
        topBar = {
            if (showTopAndBottomNav) TopNav(showSearch = currentRoute == "home", navController = navController)
        },
        bottomBar = {
            if (showTopAndBottomNav) BottomNav(navController = navController)
        }
    ) { innerPadding ->
        AnimatedNavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding), // Apply inner padding to avoid overlap with bottom bar
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)) }
        ) {
            composable("cart") {
                AppTheme {
                    CartUI()
                }
            }
            composable("account") {
                AppTheme {
                    AccountUI(navController = navController)
                }
            }
            composable("login") {
                AppTheme {
                    LoginLMNT(Login = { navController.navigate("home")}, Register = { navController.navigate("register")})
                }
            }
            composable("register") {
                AppTheme {
                    RegisterApp(navController = navController)
                }
            }
            composable("home") {
                AppTheme {
                    MainUI(navController = navController)
                }
            }
            composable("item") {
                AppTheme {
                    ItemsUI(navController = navController)
                }
            }
            composable("wishlist") {
                AppTheme {
                    WishlistUI(navController = navController)
                }
            }
            composable(
                route = "detailedItemView/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                val item = Datasource().loadItems().find { it.imageResourceId == itemId }
                item?.let {
                    DetailedItemView(item = it, navController = navController)
                }
            }
        }
    }
}
