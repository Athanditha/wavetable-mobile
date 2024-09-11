package com.example.wavetable.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wavetable.R
import com.example.wavetable.data.Datasource
import com.example.wavetable.model.Item
import com.example.wavetable.model.cartItem
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.ui.theme.AppTheme

class Cart : ComponentActivity() {
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
fun CartUI(){
    Text(text = "Your Cart")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ){
            Text(
                text = "Your Cart",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            CartItemsList()
                }
            }







@Composable
fun CartItemsList(modifier: Modifier = Modifier.fillMaxSize()) {
    val items: List<cartItem> = Datasource().loadCartItems()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .height(600.dp)
    ){
        items(items.size) { index ->
            CartItemCard(cartItem = items[index])
        }
    }
}


@Composable
fun CartItemCard(cartItem: cartItem, modifier: Modifier = Modifier) {

    val priceString = stringResource(id = cartItem.CartItemPrice)
    val qtnString = stringResource(id = cartItem.CartItemQuantity)

    val itemPrice = priceString.removePrefix("$").toFloatOrNull() ?: 0f
    val itemQuantity = qtnString.toFloatOrNull() ?: 0f 
    val totalPrice = itemPrice * itemQuantity
    Column(
        modifier = modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp) // Added padding inside the card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp) // Spacing between image and text columns
        ) {
            // Item Image
            Image(
                painter = painterResource(id = cartItem.CartItemImage),
                contentDescription = stringResource(id = cartItem.CartItemId),
                modifier = Modifier
                    .size(100.dp) // Combined width and height
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant) // Adds background to image for better contrast
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(12.dp)) // Space between image and text

            // Item Details
            Column(
                modifier = Modifier
                    .weight(1f) // Ensures the column takes up the remaining space
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(cartItem.CartItemId),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Price: ${stringResource(cartItem.CartItemPrice)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Quantity: ${stringResource(cartItem.CartItemQuantity)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp)) // Space between text and buttons

            // Action Buttons
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { /* TODO: Handle Remove Item */ }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Remove Item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        Divider()
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Total Price:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$${totalPrice}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}





@Preview
@Composable
fun CartPreview(){
    AppTheme {
        CartUI()
    }
}