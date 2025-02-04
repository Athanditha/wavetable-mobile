package com.example.wavetable.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wavetable.ui.theme.AppTheme
import com.example.wavetable.viewmodel.AuthViewModel

class ForgotPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            singleLine = true,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.secondaryContainer,
                unfocusedContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = authState) {
            is AuthViewModel.AuthState.Loading -> {
                CircularProgressIndicator(color = colorScheme.primary)
            }
            is AuthViewModel.AuthState.Success -> {
                Text(
                    text = state.message,
                    color = Color.Green,
                    modifier = Modifier.padding(8.dp)
                )
            }
            is AuthViewModel.AuthState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }
            else -> {}
        }

        Button(
            onClick = { authViewModel.forgotPassword(email) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary
            )
        ) {
            Text("Send Reset Email", color = colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
            ){
            ClickableText(
            text = AnnotatedString("Back to Login"),
            onClick = { navController.popBackStack() },
            style = TextStyle(
                color = colorScheme.primary,
                fontSize = 14.sp
            ),
            )
        }
    }
}


