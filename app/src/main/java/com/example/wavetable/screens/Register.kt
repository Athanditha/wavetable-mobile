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

class Register : ComponentActivity() {
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
fun RegisterApp(
    modifier: Modifier =Modifier,
    navController: NavController
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("")}
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth(),
                        color = colorScheme.onSecondary
                    )
                }
                // Username
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = {
                        Text(
                            "Username",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
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
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onSurface
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colorScheme.secondary,
                        unfocusedIndicatorColor = colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                // FirstName
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = {
                        Text(
                            "First Name",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Last Name
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = {
                        Text(
                            "Last Name",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Email
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            "Email",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Address
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = {
                        Text(
                            "Address",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Phone Number
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = {
                        Text(
                            "Phone Number",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Set keyboard type to number
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondaryContainer,
                    contentColor = colorScheme.onSecondaryContainer
                )
            ) {
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
@Preview(showBackground = true)
@Composable
fun RegPrev() {
    AppTheme {
        RegisterApp(navController = rememberNavController())
    }
}

