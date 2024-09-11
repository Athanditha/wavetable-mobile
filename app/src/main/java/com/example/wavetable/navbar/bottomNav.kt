package com.example.wavetable.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

    @Composable
    fun BottomNav(navController: NavHostController) {
        val currentBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry.value?.destination?.route

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
                .height(80.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            BottomNavItem(icon = Icons.Default.Home, text = "Home", navController, "home", currentRoute == "home")
            BottomNavItem(icon = Icons.Default.Menu, text = "Items", navController, "item", currentRoute == "item")
            BottomNavItem(icon = Icons.Default.FavoriteBorder, text = "Wishlist", navController, "wishlist", currentRoute == "wishlist")
            BottomNavItem(icon = Icons.Default.AccountCircle, text = "Account", navController, "account", currentRoute == "account")
        }
    }

    @Composable
    fun BottomNavItem(
        icon: ImageVector,
        text: String,
        navController: NavHostController,
        route: String,
        isSelected: Boolean
    ) {
        val iconColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate(route) }
        ) {
            Icon(
                imageVector = icon,
                tint = iconColor,
                contentDescription = text,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
