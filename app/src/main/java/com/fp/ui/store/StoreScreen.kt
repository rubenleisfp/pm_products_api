package com.fp.ui.store

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.fp.R
import com.fp.Screen
import com.fp.data.Datasource
import com.fp.model.Product
import com.fp.model.ProductPageWrapper
import com.fp.model.ProductWrapper
import com.fp.ui.favorite.FavoriteViewModel
import com.fp.ui.theme.Pm_products_apiTheme

/**
 * A composable function that serves as the main entry point for the store screen.
 * It collects the UI state from the [storeViewModel] and displays the appropriate content,
 * such as a list of products, a loading indicator, or an error message.
 *
 * @param navController The navigation controller for handling screen transitions.
 * @param storeViewModel The ViewModel responsible for managing the store's state and business logic.
 */

@Composable
fun StoreScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    favoriteViewModel: FavoriteViewModel
) {
    val productState by storeViewModel.uiState.collectAsState()
    StoreGrid(
        navController,
        storeState = productState,
        onExpand = { storeViewModel.onDetailSelected(it) },
        isFavoriteProduct = { favoriteViewModel.isFavoriteProduct(it) },
        onClickAddFavorite = { favoriteViewModel.addFavoriteProduct(it) },
        onClickRemoveFavorite = { favoriteViewModel.deleteFavoriteProductById(it.id) })

}

/**
 * A composable function that displays the main screen of the store, including a top app bar and a grid of products.
 * It observes the [storeState] to determine what to display: a list of products, an error message, or a loading indicator.
 *
 * @param navController The navigation controller used for navigating between screens.
 * @param storeState The current state of the store's UI, containing product data and the current action (e.g., READ, ERROR).
 * @param onExpand A lambda function to be invoked when a product item's expand/collapse button is clicked. It passes the product's ID.
 * @param onClickAddFavorite A lambda function to be invoked when a product item's add favorite button is clicked. It passes the [Product] object.
 * @param onClickRemoveFavorite A lambda function to be invoked when a product item's remove favorite button is clicked. It passes the product's ID.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun StoreGrid(
    navController: NavController,
    storeState: StoreState,
    onExpand: (Int) -> Unit,
    isFavoriteProduct: (ProductWrapper) -> Boolean,
    onClickAddFavorite: (Product) -> Unit,
    onClickRemoveFavorite: (Product) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            StoreTopAppBar(title = stringResource(R.string.app_name), navController = navController)
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

                    ProductList(
                        productWrapperList = storeState.productPageWrapper.productsWrapper,
                        onExpand = onExpand,
                        isFavoriteProduct = isFavoriteProduct,
                        onClickAddFavorite = onClickAddFavorite,
                        onClickRemoveFavorite = onClickRemoveFavorite,
                        modifier = modifier,
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
 * @param onClickRemoveFavorite A lambda function to be invoked when the "Remove from Favorite" button is clicked for a product. It passes the [Product] object.
 * @param modifier The modifier to be applied to the `LazyColumn`.
 */
@Composable
fun ProductList(
    productWrapperList: List<ProductWrapper>,
    onExpand: (Int) -> Unit,
    isFavoriteProduct: (ProductWrapper) -> Boolean,
    onClickAddFavorite: (Product) -> Unit,
    onClickRemoveFavorite: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(productWrapperList) { index, productWrapper ->
            ProductItem(
                productWrapper = productWrapper,
                onExpand = onExpand,
                isFavoriteProduct = isFavoriteProduct,
                onClickAddFavorite = onClickAddFavorite,
                onClickRemoveFavorite = onClickRemoveFavorite,
                modifier = modifier
            )
        }
    }
}


/**
 * A composable function that creates a centered top app bar for the store screen.
 * It displays the app's logo and name. The logo is clickable and navigates
 * to the front page screen when tapped.
 *
 * @param navController The navigation controller used for handling screen transitions.
 * @param modifier The modifier to be applied to the top app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopAppBar(title: String, navController: NavController, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size))
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .clickable(onClick = { navController.navigate(Screen.StoreScreen.route) }),
                    painter = painterResource(R.drawable.store),
                    contentDescription = null,

                    )
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayLarge
                )

//                Image(
//                    modifier = Modifier
//                        .size(dimensionResource(id = R.dimen.image_size))
//                        .padding(dimensionResource(id = R.dimen.padding_small))
//                        .clickable(onClick = { navController.navigate(Screen.FavoriteScreen.route) }),
//                    painter = painterResource(id = R.drawable.favorite),
//                    contentDescription = stringResource(id = R.string.descripcion)
//                )

                IconButton(
                    onClick = { navController.navigate(Screen.FavoriteScreen.route) },
                    modifier = modifier
                ) {
                    Icon(
                        // Elige el icono basándote en el estado `isFavorite`
                        imageVector = Icons.Filled.Favorite,
                        // Cambia el tamaño para darle más énfasis
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(R.string.add_to_favorites_description), // Añade una descripción de accesibilidad
                        modifier = Modifier.size(100.dp)
                    )
                }


            }
        }
    )
}


/**
 * A composable function that displays a full-screen error message.
 * This screen is shown when the data fetching fails. It has a distinct background color
 * to indicate an error state and presents a user-friendly error text.
 */
@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.error_message),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer
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
 * @param onClickRemoveFavorite A lambda function to be invoked when the "Remove from Favorite" button is clicked, passing the [Product] object.
 * @param modifier The modifier to be applied to the card layout.
 */
@Composable
fun ProductItem(
    productWrapper: ProductWrapper,
    onExpand: (Int) -> Unit,
    isFavoriteProduct: (ProductWrapper) -> Boolean,
    onClickAddFavorite: (Product) -> Unit,
    onClickRemoveFavorite: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
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
                ProductIcon(productWrapper.product.thumbnail)
                ProductInformation(product = productWrapper.product)
                Spacer(modifier = Modifier.weight(1f))
                ProductItemButton(
                    expanded = productWrapper.expanded,
                    onExpand = { onExpand(productWrapper.id) }
                )

            }
            if (productWrapper.expanded) {
                ProductDetails(
                    productWrapper = productWrapper,
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
            if (isFavoriteProduct(productWrapper)) {
                RemoveFavorite(onClickRemoveFavorite, productWrapper)
            } else {
                AddFavorite(onClickAddFavorite, productWrapper)
            }

        }
    }
}

/**
 * A composable function that displays the detailed information of a product,
 * including its description and stock level. This component is typically shown
 * when a product item is expanded.
 *
 * @param productWrapper The [ProductWrapper] containing the product's data to be displayed.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun ProductDetails(
    productWrapper: ProductWrapper,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = productWrapper.product.description,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        Text(
            text = stringResource(R.string.stock),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = productWrapper.product.stock.toString(),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

/**
 * Muestra un botón para expandir o contraer los detalles de un producto.
 *
 * @param expanded Indica si los detalles del producto están expandidos o no.
 * @param onExpand La función que se ejecutará cuando se haga clic en el botón.
 * @param modifier El modificador de diseño para este componente.
 */
@Composable
private fun ProductItemButton(
    expanded: Boolean,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onExpand,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun AddFavorite(onClickAddFavorite: (Product) -> Unit, productWrapper: ProductWrapper) {
    val mContext = LocalContext.current
    Button(onClick = {
        onClickAddFavorite(productWrapper.product)
        Toast.makeText(mContext, R.string.product_added_to_favorites, Toast.LENGTH_SHORT).show()
    }) {
        Text(stringResource(R.string.add_favorite))
    }
}

@Composable
fun RemoveFavorite(onClickRemoveFavorite: (Product) -> Unit, productWrapper: ProductWrapper) {
    val mContext = LocalContext.current
    Button(onClick = {
        onClickRemoveFavorite(productWrapper.product)
        Toast.makeText(mContext, R.string.product_removed_from_favorites, Toast.LENGTH_SHORT).show()
    }) {
        Text(stringResource(R.string.remove_favorite))
    }
}


/**
 * A composable function that displays a product's thumbnail image.
 * It uses Coil's `AsyncImage` to load the image from a URL asynchronously.
 * The image is styled with a specific size, padding, and clipped to a small shape.
 *
 * @param thumbnail The URL string of the product's thumbnail image.
 * @param modifier The modifier to be applied to the image.
 */
@Composable
fun ProductIcon(
    thumbnail: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = thumbnail,
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Crop,
        contentDescription = "Imagen del producto"
    )
}


/**
 * A composable function that displays key information about a product,
 * specifically its title and price.
 *
 * @param product The [Product] object containing the data to be displayed.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun ProductInformation(product: Product, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = product.title,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(R.string.price) + product.price + " €",
            style = MaterialTheme.typography.bodyMedium
        )


    }
}


@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
    Pm_products_apiTheme {
        ProductList(Datasource().loadProductsWrapper(), isFavoriteProduct = {false}, onExpand = {}, onClickAddFavorite = {}, onClickRemoveFavorite = {})
    }
}


@Preview(showBackground = true)
@Composable
fun ProductItemFavoritePreview() {
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
        ProductItem(productWrapper, onExpand = {}, isFavoriteProduct = { true }, onClickAddFavorite = {}, onClickRemoveFavorite = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemNoFavoritePreview() {
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
        ProductItem(productWrapper, onExpand = {}, isFavoriteProduct = { false }, onClickAddFavorite = {}, onClickRemoveFavorite = {})
    }
}


@Preview(showBackground = true)
@Composable
fun StoreTopAppBarPreview() {

    Pm_products_apiTheme {
        StoreTopAppBar(title = "Store", navController = rememberNavController())
    }
}


@Preview(showBackground = true)
@Composable
fun StoreGridPreview() {
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
        StoreGrid(
            navController = rememberNavController(),
            storeState = storeState,
            onExpand = {},
            isFavoriteProduct = { false },
            onClickAddFavorite = {},
            onClickRemoveFavorite = {}
        )
    }
}