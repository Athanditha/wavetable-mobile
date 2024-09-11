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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wavetable.ContentSection
import com.example.wavetable.DetailedItemView
import com.example.wavetable.ListDisplay
import com.example.wavetable.R
import com.example.wavetable.data.Datasource
import com.example.wavetable.model.Brand
import com.example.wavetable.model.Item
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.ui.theme.AppTheme
import kotlinx.coroutines.delay

class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainUI(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun MainUI(navController: NavHostController, modifier: Modifier = Modifier) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                ContentSection {
                    Slideshow()
                }
                ContentSection(title = "New Releases") {
                    ListDisplay(
                        elements = Datasource().loadItems(),
                        content = { item ->
                            // Use navController to navigate to the detailed view
                            ItemCard(item = item, navController = navController)
                        }
                    )
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


@Composable
fun ItemCard(item: Item, navController: NavHostController, modifier: Modifier = Modifier) {
    
    Card(modifier = modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(16.dp))
        .width(200.dp)
        .height(250.dp)
        .border(
            width = 1.dp, color = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(16.dp)
        )
        .clickable {
            navController.navigate("detailedItemView/${item.imageResourceId}")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(item.imageResourceId),
                contentDescription = stringResource(item.stringResourceId),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.White)
            )
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                Image(
                    painter = painterResource(item.brandImgResID),
                    contentDescription = stringResource(item.brandTxtID),
                    modifier = Modifier
                        .height(20.dp)
                        .background(Color.White)
                        .fillMaxWidth()
                )
            }
            Divider(color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = stringResource(item.stringResourceId),
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
        R.drawable.prod2,
        R.drawable.prod3
    )

    var currentIndex by remember { mutableStateOf(0) }

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
