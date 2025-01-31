package com.example.wavetable

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.example.wavetable.model.Item
import com.example.wavetable.screens.AccountUI
import com.example.wavetable.screens.Items
import com.example.wavetable.screens.ItemsUI
import com.example.wavetable.screens.MainUI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.launch
import android.os.Build


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermissions()
        lifecycleScope.launch {
            getItems()
        }
        setContent {
            val navController = rememberNavController()
            AppTheme {
                AppNav(modifier = Modifier)
            }
        }
    }

    private suspend fun getItems() {
        Log.d("ItemViewModel", "Starting network request...")
        val api = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        try {
            val response = api.getItems()
            response.body()?.let {
                for (item in it) {
                    Log.i("test", "item fetches")
                }
            }
        } catch (e: Exception) {
            Log.i("test", "Failure: ${e.message}")
        }
    }

    private fun requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}


