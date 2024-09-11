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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wavetable.ContentSection
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.ui.theme.AppTheme
import com.example.wavetable.data.Datasource  // Import your data source
import com.example.wavetable.model.Item       // Import the Item model

class Wishlist : ComponentActivity() {
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
fun WishlistUI(navController: NavHostController, modifier: Modifier = Modifier) {
    val items: List<Item> = Datasource().loadItems()
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            // Header Text
            Text(
                text = "Wishlist",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),


                style = MaterialTheme.typography.headlineMedium
            )
            LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .height(600.dp)
                ){
                    items(items.size) { index ->
                        WishlistItemCard(item = items[index])
                    }

            }
            Row(modifier = Modifier.fillMaxWidth()
                .background(Color.Transparent),
                horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = { /* TODO: Handle checkout logic */ },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(16.dp),
                ){
                    Text(text = "Add all to Cart")
                }
                Button(
                    onClick = { /* TODO: Handle checkout logic */ },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(16.dp),
                ){
                    Text(text = "Clear Wishlist")
                }
            }
        }
    }


// Define a card for displaying each wishlist item
@Composable
fun WishlistItemCard(item: Item, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(10.dp))
    ) {
        // Row to display the item's image
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = item.imageResourceId),
                contentDescription = stringResource(id = item.stringResourceId),
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))

            )
            Column (
                modifier = Modifier
                    .padding(10.dp)
                    .width(220.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ){
                Text(
                    text = stringResource(item.stringResourceId),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = stringResource(item.priceItemID),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodySmall,
                )

            }
            Column{
                IconButton(onClick = { /* TODO: Handle Delete Wishlist Button */ }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Back"
                    )
                }
                IconButton(onClick = { /* TODO: Add to Cart */ }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Back"
                    )
                }
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun WishlistPreview() {
    AppTheme {
        WishlistUI(navController = rememberNavController())
    }
}
