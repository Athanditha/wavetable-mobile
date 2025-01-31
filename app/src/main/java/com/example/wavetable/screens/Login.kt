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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wavetable.R
import com.example.wavetable.ui.theme.AppTheme
import com.example.wavetable.viewmodel.AuthViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                LoginLMNT(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun LoginLMNT(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    
    var username by rememberSaveable { mutableStateOf("") }
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
                        text = "Login",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth(),
                        color = colorScheme.onSecondary
                    )
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
                    else -> {}
                }

                TextField(
                    value = username,
                    singleLine = true,
                    onValueChange = { username = it },
                    label = { Text("Email", style = MaterialTheme.typography.headlineSmall,
                        color = colorScheme.onSurface) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    singleLine = true,
                    onValueChange = { password = it },
                    label = { Text("Password", style = MaterialTheme.typography.headlineSmall,
                        color = colorScheme.onSurface) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colorScheme.secondary,
                        unfocusedIndicatorColor = colorScheme.onSurface
                    )
                )
            }

            when (val state = authState) {
                is AuthViewModel.AuthState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = colorScheme.primary
                    )
                }
                is AuthViewModel.AuthState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                else -> {
                    Button(
                        onClick = { authViewModel.login(username, password) },
                        modifier = Modifier
                            .padding(horizontal = 40.dp, vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.secondaryContainer,
                            contentColor = colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(
                            "Login",
                            color = colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.displaySmall,
                            fontSize = 30.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Don't have an account?",
                    textAlign = TextAlign.Center,
                    color = colorScheme.onBackground,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString("Register"),
                    onClick = { navController.navigate("register") },
                    style = TextStyle(
                        color = colorScheme.primary,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginLMNTPreview() {
    AppTheme {
        LoginLMNT(navController = rememberNavController())
    }
    }
