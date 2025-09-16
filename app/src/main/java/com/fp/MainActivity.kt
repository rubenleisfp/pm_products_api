package com.fp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fp.ui.store.StoreApp
import com.fp.ui.store.StoreViewModel
import com.fp.ui.theme.Pm_products_apiTheme

class MainActivity : ComponentActivity() {

    /*
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
    }*/

    private val storeViewModel by viewModels<StoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        storeViewModel.loadProductsV2()
        super.onCreate(savedInstanceState)
        setContent {
            Pm_products_apiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    StoreApp(storeViewModel)
                }
            }
        }
    }
}
