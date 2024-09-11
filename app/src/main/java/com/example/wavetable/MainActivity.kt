package com.example.wavetable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.screens.Account
import com.example.wavetable.screens.Login
import com.example.wavetable.screens.MainMenu
import com.example.wavetable.screens.Register
import com.example.wavetable.ui.theme.AppTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavHostController
import com.example.wavetable.screens.AccountUI
import com.example.wavetable.screens.Items
import com.example.wavetable.screens.ItemsUI
import com.example.wavetable.screens.MainUI
import com.example.wavetable.screens.WishlistUI


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                    AppNav(modifier = Modifier)
                }
            }
        }
    }

