package com.example.wavetable.screens

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wavetable.ContentSection
import com.example.wavetable.ListDisplay
import com.example.wavetable.R
import com.example.wavetable.data.Datasource
import com.example.wavetable.model.Brand
import com.example.wavetable.model.Item
import com.example.wavetable.ui.theme.AppTheme
import com.example.wavetable.viewmodel.ItemViewModel
import kotlinx.coroutines.delay
import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.wavetable.components.NetworkStatusIndicator
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        setContent {
            AppTheme {
                MainUI(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun MainUI(navController: NavHostController, itemViewModel: ItemViewModel = viewModel()) {
    val items by itemViewModel.items.collectAsState()
    var isLoading by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        itemViewModel.getItems()
    }

    // Update loading state based on items
    LaunchedEffect(items) {
        isLoading = items.isEmpty()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                ContentSection {
                    Slideshow()
                }
                ContentSection(title = "New Releases") {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Loading items...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    } else if (items.isNotEmpty()) {
                        LazyRow {
                            items(items) { item ->
                                ItemCard(item = item, navController = navController)
                            }
                        }
                    } else {
                        Text(
                            text = "No items available",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                ContentSection(title = "Brands") {
                    ListDisplay(
                        elements = Datasource().loadBrand(),
                        content = { brand ->
                            BrandCard(brand = brand)
                        }
                    )
                }
                val categories = listOf("Headphones", "Speakers", "Mixers", "Amps")

                ContentSection(title = "Categories") {
                    ListDisplay(
                        elements = categories,
                        content = { category ->
                            CategoryCard(category = category)
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun ItemCard(item: Item, navController: NavHostController, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .width(200.dp)
            .height(300.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                // Navigate to DetailedItemView with the item's ID
                navController.navigate("detailedItemView/${item.id}")
            }
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
            Divider(color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = item.name,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceDim)
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
fun CategoryCard(category: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .width(150.dp)
            .height(80.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun BrandCard(brand: Brand, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .width(200.dp)
            .height(100.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(brand.brandImgResID),
                contentDescription = stringResource(brand.brandTxtID),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun Slideshow(slideInterval: Long = 5000) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val imageHeight = if (isPortrait) 200.dp else 350.dp

    val imageResources = listOf(
        R.drawable.promo1,
        R.drawable.promo2,
        R.drawable.promo3
    )

    var currentIndex by rememberSaveable { mutableStateOf(0) }

    // Update the current index after every slideInterval duration
    LaunchedEffect(currentIndex) {
        while (true) {
            delay(slideInterval)
            currentIndex = (currentIndex + 1) % imageResources.size
        }
    }

    // Crossfade for smooth transitions
    Crossfade(targetState = currentIndex, modifier = Modifier.fillMaxWidth()) { index ->
        Image(
            painter = painterResource(id = imageResources[index]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight))
    }
}

@Preview
@Composable
fun MainPreview(){
    AppTheme {
        MainUI(navController = rememberNavController())
    }
}
