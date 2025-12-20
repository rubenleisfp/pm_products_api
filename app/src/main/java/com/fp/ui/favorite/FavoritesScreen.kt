package com.fp.ui.favorite

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fp.R
import com.fp.data.Datasource
import com.fp.data.FavoriteProduct
import com.fp.model.Product
import com.fp.model.ProductPageWrapper
import com.fp.model.ProductWrapper
import com.fp.ui.store.ActionEnum
import com.fp.ui.store.ErrorScreen
import com.fp.ui.store.ProductItem
import com.fp.ui.store.ProductList
import com.fp.ui.store.StoreState
import com.fp.ui.store.StoreTopAppBar
import com.fp.ui.store.StoreViewModel
import com.fp.ui.theme.Pm_products_apiTheme


@Composable
fun FavoritesScreen(navController: NavController, favoriteViewModel: FavoriteViewModel) {
    val favoriteUiState by favoriteViewModel.uiState.collectAsState()

    FavoriteGrid(navController, favoriteUiState = favoriteUiState, onDeleteFavorite = {
        favoriteProduct: FavoriteProduct ->
        favoriteViewModel.deleteFavoriteProduct(favoriteProduct)
    })
}


//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteGrid(
    navController: NavController,
    favoriteUiState: FavoriteUiState,
    onDeleteFavorite: (FavoriteProduct) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = { StoreTopAppBar(stringResource(R.string.favorites), navController = navController) }
    ) { innerPadding ->
        FavoriteList(favoriteUiState.favoriteProductList, onDeleteFavorite = onDeleteFavorite, modifier = modifier.padding(innerPadding))
    }
}


@Composable
fun FavoriteList(favoriteList: List<FavoriteProduct>,  onDeleteFavorite: (FavoriteProduct) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(favoriteList) { index, favorite ->
            FavoriteItem(
                favorite = favorite,
                onDeleteFavorite = onDeleteFavorite
            )
        }
    }
}

@Composable
fun FavoriteItem(favorite: FavoriteProduct, onDeleteFavorite: (FavoriteProduct) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            ) {
                Text(
                    text = favorite.name,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)).weight(4f),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = favorite.price.toString(),
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)).weight(2f),
                    style = MaterialTheme.typography.bodyLarge,
                )
                IconButton(onClick = { onDeleteFavorite(favorite) }) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FavoriteListPreview() {
    Pm_products_apiTheme {
        FavoriteList(
            onDeleteFavorite = {},
            favoriteList = Datasource().loadFavorites()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun FavoriteItemPreview() {
    Pm_products_apiTheme {
        FavoriteItem(onDeleteFavorite = {}, favorite = FavoriteProduct(1, "Boli", 3.12))
    }
}


@Preview(showBackground = true)
@Composable
fun FavoriteGridPreview() {
    Pm_products_apiTheme {
        FavoriteGrid(
            onDeleteFavorite = {},
            navController = rememberNavController(),
            favoriteUiState = FavoriteUiState(
                favoriteProductList = Datasource().loadFavorites()
            )

        )
    }
}