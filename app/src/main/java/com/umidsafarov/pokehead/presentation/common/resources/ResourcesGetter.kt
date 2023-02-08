package com.umidsafarov.pokehead.presentation.common.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.umidsafarov.pokehead.R

object ResourcesGetter {
    @Composable
    fun loadingPlaceholder(): Painter {
        return painterResource(id = R.drawable.ic_image_loading)
    }

    @Composable
    fun errorPlaceholder(): Painter {
        return painterResource(id = R.drawable.ic_image_error)
    }
}