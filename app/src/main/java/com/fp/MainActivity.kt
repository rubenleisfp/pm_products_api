package com.fp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fp.ui.theme.Pm_products_apiTheme
import com.fp.ui.screens.artist.ArtistViewModel
import com.fp.ui.screens.artist.ArtistApp

class MainActivity : ComponentActivity() {

    private val artistViewModel by viewModels<ArtistViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        artistViewModel.loadArtist()
        super.onCreate(savedInstanceState)
        setContent {
            Pm_products_apiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtistApp(artistViewModel)
                }
            }
        }
    }
}
