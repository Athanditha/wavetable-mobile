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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.wavetable.data.Datasource
import com.example.wavetable.helpers.FirebaseRepository
import com.example.wavetable.model.Item
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.ui.theme.AppTheme
import com.example.wavetable.viewmodel.ItemViewModel
import kotlinx.coroutines.delay

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
fun DetailedItemView(id: String?, navController: NavHostController, modifier: Modifier = Modifier) {
    val itemViewModel: ItemViewModel = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    var quantity by rememberSaveable { mutableStateOf(1) }

    LaunchedEffect(id) {
        Log.d("DetailedItemView", "Starting LaunchedEffect with ID: $id")
        id?.let {
            Log.d("DetailedItemView", "About to fetch item with ID: $it")
            itemViewModel.getItemById(it)
            delay(1000) // Give the API call some time to complete
            isLoading = false
        }
    }

    val currentItem = itemViewModel.item.collectAsState(initial = null).value
    Log.d("DetailedItemView", "Current item state: $currentItem")

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        currentItem?.let { item ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(state = rememberScrollState())
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Item Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = item.image,
                            builder = {
                                crossfade(true) // Smooth loading effect
                                placeholder(R.drawable.logodark) // Show a placeholder while loading
                                error(R.drawable.logodark) // Show an error image if loading fails
                            }
                        ),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )

                    // Gradient Overlay for a professional look
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.2f)
                                    )
                                )
                            )
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                // Item Details Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Item Name
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Item Price
                        val price = try {
                            item.sale_price.takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0
                        } catch (e: NumberFormatException) {
                            0.0
                        }
                        Text(
                            text = "Price: ${"$%.2f".format(price)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Item Description
                        Text(
                            text = item.description ?: "No description available.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quantity Selector and Total Price
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quantity Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Quantity:",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Decrease Quantity"
                                    )
                                }

                                Text(
                                    text = quantity.toString(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                IconButton(onClick = { quantity++ }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase Quantity"
                                    )
                                }
                            }
                        }

                        // Safely parse the price
                        val price = try {
                            item.sale_price.takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0
                        } catch (e: NumberFormatException) {
                            Log.e("DetailedItemView", "Error parsing price: ${item.sale_price}", e)
                            0.0
                        }

                        // Total Price calculation
                        val totalPrice = price * quantity
                        Text(
                            text = "Total: ${"$%.2f".format(totalPrice)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /* TODO: Add to Cart Logic */ },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Add to Cart", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    OutlinedButton(
                        onClick = { /* TODO: Add to Wishlist Logic */ },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Add to Wishlist")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Back Button
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Back", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Item not found.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.headlineSmall
                )
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

