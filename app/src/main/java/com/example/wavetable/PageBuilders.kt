package com.example.wavetable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wavetable.data.Datasource
import com.example.wavetable.model.Item
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.ui.theme.AppTheme

@Composable
fun ContentSection(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        if (title != null) {
            Text(
                text = title,
                modifier = modifier.padding(start = 20.dp, top = 10.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        content()
    }
}

@Composable
fun DetailedItemView(
    item: Item,
    navController: NavHostController, // Use the existing NavController
    modifier: Modifier = Modifier.fillMaxSize()
) {
    // Retrieve price from string resources
    val priceString = stringResource(id = item.priceItemID)
    val itemprice = priceString.removePrefix("$").toFloatOrNull() ?: 0f // Convert to Float, default to 0 if conversion fails

    var quantity by remember { mutableStateOf(1) }
    var totprice by remember { mutableStateOf(quantity * itemprice) }

    // Recalculate total price when quantity changes
    LaunchedEffect(quantity) {
        totprice = quantity * itemprice
        Log.d("DetailedItemView", "Updated total price: $totprice") // Debug logging
    }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp) // Optional: Adding elevation for depth
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        // Top Row: Back and Favorite buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) { // Navigate back
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                            IconButton(onClick = { /* TODO: Handle heart button click */ }) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Image of the item
                        Image(
                            painter = painterResource(id = item.imageResourceId),
                            contentDescription = stringResource(id = item.stringResourceId),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(id = item.stringResourceId),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = stringResource(id = item.priceItemID), // Display price from strings.xml
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = stringResource(id = item.ItemDescription),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Decrease quantity"
                                    )
                                }
                                Text(
                                    text = quantity.toString(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(onClick = { quantity++ }) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Increase quantity"
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total: $${String.format("%.2f", totprice)}", // Format total price as currency
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { /* TODO: Handle Add to Cart */ },
                                modifier = Modifier.fillMaxWidth(0.6f)
                            ) {
                                Text("Add to Cart", style = MaterialTheme.typography.headlineSmall)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Button(
                                    onClick = { /* TODO: Buy */ },
                                    modifier = Modifier.width(100.dp)
                                ) {
                                    Text("Buy", style = MaterialTheme.typography.headlineSmall)
                                }
                                Button(
                                    onClick = { /* TODO: Rent */ }
                                ) {
                                    Text("Rent", style = MaterialTheme.typography.headlineSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }




@Composable
fun <T> ListDisplay(
    elements: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    Box(
        modifier = modifier
            .padding(top = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        LazyRow(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceBright)
        ) {
            items(elements) { item ->
                content(item)
            }
        }
    }
}

