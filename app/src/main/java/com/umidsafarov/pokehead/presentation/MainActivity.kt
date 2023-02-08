package com.umidsafarov.pokehead.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.umidsafarov.pokehead.presentation.navigation.AppNavigation
import com.umidsafarov.pokehead.presentation.theme.PokeheadAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeheadAppTheme {
                AppNavigation()
            }
        }
    }
}