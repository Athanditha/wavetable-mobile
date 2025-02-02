package com.example.wavetable.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.wavetable.R
import com.example.wavetable.ui.theme.AppTheme
import com.example.wavetable.model.Item
import com.example.wavetable.viewmodel.WishlistViewModel

@Composable
fun WishlistUI(navController: NavHostController, modifier: Modifier = Modifier) {
    val wishlistViewModel: WishlistViewModel = viewModel()
    val wishlistItems = wishlistViewModel.wishlistItems.collectAsState(initial = emptyList()).value
    val context = LocalContext.current
    var isConnected by rememberSaveable { mutableStateOf(checkInternetConnection(context)) }

    // Check internet connection when UI recomposes
    LaunchedEffect(Unit) {
        isConnected = checkInternetConnection(context)
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (!isConnected) { // No Internet
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    val isDarkMode = isSystemInDarkTheme()
                    val logo = if (isDarkMode) R.drawable.logodark else R.drawable.logolight

                    Image(
                        painter = painterResource(id = logo),
                        contentDescription = "Loading",
                        modifier = Modifier
                            .width(150.dp)
                            .height(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Please connect to the internet to show your wishlist.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }
        } else if (wishlistItems.isEmpty()) { // Loading State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val isDarkMode = isSystemInDarkTheme()
                    val logo = if (isDarkMode) R.drawable.logodark else R.drawable.logolight

                    Image(
                        painter = painterResource(id = logo),
                        contentDescription = "Loading",
                        modifier = Modifier
                            .width(150.dp)
                            .height(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        } else { // Show Wishlist Items
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                items(wishlistItems.size) { index ->
                    WishlistItemCard(item = wishlistItems[index], navController = navController)
                }
            }
        }

        // Buttons for Wishlist Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { /* TODO: Handle add all to cart logic */ },
                modifier = Modifier
                    .width(200.dp)
                    .padding(16.dp)
            ) {
                Text(text = "Add all to Cart")
            }
            Button(
                onClick = { /* TODO: Handle clear wishlist logic */ },
                modifier = Modifier
                    .width(200.dp)
                    .padding(16.dp)
            ) {
                Text(text = "Clear Wishlist")
            }
        }
    }
}

// Function to check internet connectivity
fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}


@Composable
fun WishlistItemCard(item: Item, navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp)
            ),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Row to display the item's image and details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberImagePainter(item.image),  // Assuming `image` is a URL
                contentDescription = item.name,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .width(220.dp)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Price: ${item.sale_price}",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column {
                IconButton(onClick = { /* TODO: Handle Delete Wishlist Item */ }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove from Wishlist")
                }
                IconButton(onClick = { /* TODO: Add to Cart */ }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Add to Cart")
                }
            }
        }
    }
}
