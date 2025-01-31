package com.example.wavetable.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wavetable.R
import com.example.wavetable.ui.theme.AppTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wavetable.viewmodel.AuthViewModel

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                RegisterApp(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun RegisterApp(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val colorScheme = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            val imageResource = if (isSystemInDarkTheme()) {
                R.drawable.logodark
            } else {
                R.drawable.logolight
            }

            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "WaveTable Logo",
                modifier = Modifier.height(200.dp)
            )

            Column(
                modifier = modifier
                    .width(375.dp)
                    .padding(10.dp)
                    .background(colorScheme.surface)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(15.dp),
                        color = colorScheme.secondary,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .background(colorScheme.secondary)
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Register",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        color = colorScheme.onSecondary
                    )
                }

                // Email
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            "Email",
                            style = MaterialTheme.typography.headlineSmall,
                            color = colorScheme.onSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Password
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            "Password",
                            style = MaterialTheme.typography.headlineSmall,
                            color = colorScheme.onSurface
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colorScheme.secondary,
                        unfocusedIndicatorColor = colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Show error message if any
            when (val state = authState) {
                is AuthViewModel.AuthState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                is AuthViewModel.AuthState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("login")
                    }
                }
                else -> {}
            }

            Button(
                onClick = { 
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authViewModel.register(email, password)
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondaryContainer,
                    contentColor = colorScheme.onSecondaryContainer
                ),
                enabled = email.isNotEmpty() && password.isNotEmpty() && 
                         authState !is AuthViewModel.AuthState.Loading
            ) {
                if (authState is AuthViewModel.AuthState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = colorScheme.onSecondaryContainer
                    )
                } else {
                    Text(
                        "Register",
                        color = colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Your existing UI components...

        when (val state = authState) {
            is AuthViewModel.AuthState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthViewModel.AuthState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate("login")
                }
            }
            is AuthViewModel.AuthState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            else -> { /* Initial state, do nothing */ }
        }

        Button(
            onClick = { authViewModel.register(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegPrev() {
    AppTheme {
        RegisterApp(navController = rememberNavController())
    }
}

