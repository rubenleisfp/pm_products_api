package com.fp.ui.favorite

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
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fp.R
import com.fp.data.Datasource
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
fun FavoritesScreen(navController: NavController, storeViewModel: StoreViewModel) {
    val productState by storeViewModel.uiState.collectAsState()
    FavoriteGrid(navController, storeState = productState)
}

/**
 * A composable function that displays the main screen of the store, including a top app bar and a grid of products.
 * It observes the [storeState] to determine what to display: a list of products, an error message, or a loading indicator.
 *
 * @param navController The navigation controller used for navigating between screens.
 * @param storeState The current state of the store's UI, containing product data and the current action (e.g., READ, ERROR).
 * @param onExpand A lambda function to be invoked when a product item's expand/collapse button is clicked. It passes the product's ID.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun FavoriteGrid(navController: NavController, storeState: StoreState, modifier: Modifier = Modifier) {

    Scaffold(
        topBar = {
            StoreTopAppBar(navController = navController)
        }
    ) { innerPadding ->

        when (storeState.action) {
            ActionEnum.ERROR -> ErrorScreen()

            ActionEnum.READ ->
                Column(
                    modifier = modifier
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(36.dp)
                ) {

                    FavoriteList(
                        productWrapperList = storeState.productPageWrapper.productsWrapper,
                        onExpand = {},
                        onClickAddFavorite = {}
                    )

                }

            ActionEnum.IS_LOADING -> TODO() //DO NOTHING
        }
    }
}


/**
 * A composable function that displays a scrollable list of products.
 * It uses a `LazyColumn` to efficiently render only the items currently visible on screen.
 *
 * @param productWrapperList A list of `ProductWrapper` objects, where each item contains the product data and its UI state (e.g., expanded).
 * @param onExpand A lambda function to be invoked when a product's expand/collapse button is clicked. It passes the product's ID.
 * @param onClickAddFavorite A lambda function to be invoked when the "Add to Favorite" button is clicked for a product. It passes the [Product] object.
 * @param modifier The modifier to be applied to the `LazyColumn`.
 */
@Composable
fun FavoriteList(productWrapperList: List<ProductWrapper>, onExpand: (Int) -> Unit,  onClickAddFavorite: (Product) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(productWrapperList) { index, productWrapper ->
            FavoriteItem(
                productWrapper = productWrapper,
                onExpand = onExpand,
                onClickAddFavorite = onClickAddFavorite,
                modifier = modifier
            )
        }
    }
}




/**
 * A composable function that displays a single product item within a card.
 * This item includes the product's basic information, an icon, and a button to
 * expand or collapse a more detailed view. The card's content size animates
 * smoothly when expanded or collapsed. It also includes a button to add the
 * product to a favorites list.
 *
 * @param productWrapper The [ProductWrapper] containing the product's data and its expanded state.
 * @param onExpand A lambda function to be invoked when the expand/collapse button is clicked, passing the product's ID.
 * @param onClickAddFavorite A lambda function to be invoked when the "Add to Favorite" button is clicked, passing the [Product] object.
 * @param modifier The modifier to be applied to the card layout.
 */
@Composable
fun FavoriteItem(productWrapper: ProductWrapper, onExpand: (Int) -> Unit, onClickAddFavorite: (Product) -> Unit, modifier: Modifier = Modifier ) {
    Card(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
        Column(   modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            ) {


            }


        }
    }
}



@Preview(showBackground = true)
@Composable
fun FavoriteListPreview() {
    Pm_products_apiTheme {
        ProductList(Datasource().loadProductsWrapper(), onExpand = {}, onClickAddFavorite = {})
    }
}


@Preview(showBackground = true)
@Composable
fun FavoriteItemPreview() {
    val product = Product(
        1,
        "Essence Mascara Lash Princess",
        "The Essence Mascara Lash Princess is a popular mascara known for its volumizing and lengthening effects. Achieve dramatic lashes with this long-lasting and cruelty-free formula.",
        "beauty",
        9.99,
        2.56,
        99,
        "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/thumbnail.webp"
    )
    val productWrapper = ProductWrapper(product = product, id = 1, expanded = false)
    Pm_products_apiTheme {
        ProductItem(productWrapper, onExpand = {}, onClickAddFavorite = {})
    }
}





@Preview(showBackground = true)
@Composable
fun FavoriteGridPreview() {
    val storeState = StoreState(
        action = ActionEnum.READ,
        productPageWrapper = ProductPageWrapper(
            productsWrapper = Datasource().loadProductsWrapper(),
            total = 10,
            skip = 0,
            limit = 10
        )
    )
    Pm_products_apiTheme {
        FavoriteGrid(
            navController = rememberNavController(),
            storeState = storeState,

        )
    }
    }