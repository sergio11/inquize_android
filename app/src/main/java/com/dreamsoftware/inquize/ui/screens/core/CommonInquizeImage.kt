package com.dreamsoftware.inquize.ui.screens.core

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieAsyncImage
import com.dreamsoftware.inquize.R

@Composable
fun CommonInquizeImage(
    imageUrl: String
) {
    val context = LocalContext.current
    BrownieAsyncImage(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .width(250.dp)
            .aspectRatio(1f)
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.primary
            ),
        context = context,
        imageUrl = imageUrl,
        defaultImagePlaceholderRes = R.drawable.main_logo_inverse
    )
}