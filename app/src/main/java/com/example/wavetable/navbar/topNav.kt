package com.example.wavetable.navbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wavetable.R

@Composable
fun TopNav(showSearch: Boolean, navController: NavController) {
    var search by remember { mutableStateOf("Search") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
            .height(60.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)

    ) {
        // Conditionally show the TextField
        if (showSearch) {
            TextField(
                value = search,
                onValueChange = { search = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                modifier = Modifier
                    .width(300.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        } else {
            val imageResource = (R.drawable.logolight)

            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "WaveTable Logo",
                modifier = Modifier.height(200.dp))

        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { navController.navigate("cart")}) {
            Icon(
                Icons.Default.ShoppingCart,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "Shopping Cart")
        }
    }
}