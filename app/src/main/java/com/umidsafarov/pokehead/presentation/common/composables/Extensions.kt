package com.umidsafarov.pokehead.presentation.common.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun LazyListState.OnBottomReached(
    onLoadMore: () -> Unit,
    buffer: UInt = 0u,
) {

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer.toInt()
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { if (it) onLoadMore() }
    }
}

@Composable
fun OpenUrl(url: String) {
    LocalContext.current.startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    )
}