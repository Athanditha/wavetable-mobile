package com.example.wavetable.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wavetable.viewmodel.NetworkStatusViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear


@Composable
fun NetworkStatusIndicator(
    modifier: Modifier = Modifier,
    networkStatusViewModel: NetworkStatusViewModel = viewModel()
) {
    val isOnline by networkStatusViewModel.isOnline.collectAsState()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isOnline) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.errorContainer
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isOnline) Icons.Default.Check else Icons.Default.Clear,
                contentDescription = if (isOnline) "Online" else "Offline",
                modifier = Modifier.size(16.dp),
                tint = if (isOnline) 
                    MaterialTheme.colorScheme.onPrimaryContainer
                else 
                    MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isOnline) "Online" else "Offline",
                style = MaterialTheme.typography.labelMedium,
                color = if (isOnline) 
                    MaterialTheme.colorScheme.onPrimaryContainer
                else 
                    MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
} 