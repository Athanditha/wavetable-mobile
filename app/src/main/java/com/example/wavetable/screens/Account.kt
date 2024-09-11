package com.example.wavetable.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wavetable.R
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.ui.theme.AppTheme

class Account : ComponentActivity() {
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
fun AccountUI(navController: NavHostController, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center
        ) {
            ProfileHeader()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    ProfileSection()
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Buttons()
                }
            }
        }
    }




@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.jonesy), // Replace with your profile picture resource
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.primaryContainer) // Placeholder background
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ProfileSection() {
    Column {
        SettingsItem("User Name", "Jonesy")
        SettingsItem("Password", "********")
        SettingsItem("E-Mail", "jonesy@fortnite.com")
        SettingsItem("Address", "Tilted Towers")
        SettingsItem("Phone No.", "0719091920")
    }
}

@Composable
fun SettingsItem(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .width(200.dp)
                    .padding(start = 8.dp)

            ) {
                Text(text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                    )
            }
        }
    }
}

@Composable
fun Buttons() {
    Column {
        Row {
            Button(
                onClick = { /* Handle Edit */ },
                modifier = Modifier
                    .width(200.dp)
                    .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer // Background color
                        )
            ) {
                Text(
                    text = "Edit",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Button(
                onClick = { /* Handle logout */ },
                modifier = Modifier
                    .width(200.dp)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer // Background color
                )

            ) {
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        Button(
            onClick = { /* Handle logout */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer // Background color
            )
        ) {
            Text(
                text = "Logout",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview
@Composable
fun AccountPreview() {
    AppTheme {
        AccountUI(navController = rememberNavController())
    }
}
