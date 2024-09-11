package com.example.wavetable.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wavetable.ContentSection
import com.example.wavetable.DetailedItemView
import com.example.wavetable.ListDisplay
import com.example.wavetable.data.Datasource
import com.example.wavetable.model.Item
import com.example.wavetable.navbar.BottomNav
import com.example.wavetable.navbar.TopNav
import com.example.wavetable.ui.theme.AppTheme

class Items : ComponentActivity() {
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
fun ItemsUI(navController: NavHostController, modifier: Modifier = Modifier) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(Datasource().loadItems()) { item ->
                ItemDisplayCard(
                    item = item,
                    navController = navController // Use navController for navigation actions
                )
            }
        }
    }

@Composable
fun ItemDisplayCard(item: Item, navController: NavHostController, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))

            .fillMaxWidth()
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
                    .height(175.dp)
                    .background(Color.White)
            )
            Image(
                painter = painterResource(item.brandImgResID),
                contentDescription = stringResource(item.brandTxtID),
                modifier = Modifier
                    .height(50.dp)
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                Text(
                    text = stringResource(item.stringResourceId),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
            Divider(color = MaterialTheme.colorScheme.onSurface)
        }
    }
}



@Preview
@Composable
fun ItemsPreview(){
    AppTheme {
    ItemsUI(navController = rememberNavController())
        }
}