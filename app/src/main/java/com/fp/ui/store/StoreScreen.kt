package com.fp.ui.store

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import coil.compose.AsyncImage
import com.fp.R
import com.fp.data.repository.Datasource
import com.fp.ui.theme.Pm_products_apiTheme

/**
 * Esta es la vista principal de la aplicación, donde se muestran los productos.
 *
 * @param storeViewModel El ViewModel encargado de administrar los estados de la aplicación.
 */


/**
 * Muestra una cuadrícula de productos.
 *
 * @param storeState El estado actual de la aplicación.
 * @param onClick La función que se ejecutará cuando se haga clic en un producto.
 * @param modifier El modificador de diseño para este componente.
 */
@Composable
fun StoreApp(storeViewModel: StoreViewModel) {
    val productState by storeViewModel.uiState.collectAsState()
    StoreGrid(storeState = productState, onClick = { storeViewModel.onDetailSelected(it) })
}



/**
 * Muestra una lista de productos en forma de parrilla.
 *
 * @param productWrapperList La lista de productos que se mostrarán.
 * @param onClick La función que se ejecutará cuando se haga clic en un producto.
 * @param modifier El modificador de diseño para este componente.
 */
@Composable
fun StoreGrid(storeState: StoreState, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {

    Scaffold(
        topBar = {
            StoreTopAppBar()
        }
    ) { innerPadding ->


        Column(
            modifier = modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            ProductList(
                productWrapperList = storeState.productWrapperList,
                onClick = onClick,
                modifier = modifier
            )
        }


    }
}


@Composable
fun ProductList(
    productWrapperList: List<ProductWrapper>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(productWrapperList) { index, productWrapper ->
            ProductItem(
                productWrapper = productWrapper,
                onClick = onClick,
                modifier = modifier
            )
        }
    }
}

/**
 * Muestra la barra de navegación superior de la aplicación.
 *
 * @param modifier El modificador de diseño para este componente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size))
                        .padding(dimensionResource(id = R.dimen.padding_small)),
                    painter = painterResource(R.drawable.store),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}

/**
 * Muestra un elemento de producto en la cuadrícula de productos.
 *
 * @param productWrapper El objeto de producto que se mostrará.
 * @param onClick La función que se ejecutará cuando se haga clic en el producto.
 * @param modifier El modificador de diseño para este componente.
 */
@Composable
fun ProductItem(
    productWrapper: ProductWrapper,
    onClick: (Int) -> Unit,
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
                    onClick = { onClick(productWrapper.id) }
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
            EnviarEmail()
        }
    }
}

/**
 * Muestra los detalles de un producto cuando se expande.
 *
 * @param productWrapper El objeto de producto que se mostrará.
 * @param modifier El modificador de diseño para este componente.
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
            text = stringResource(R.string.descripcion),
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
 * @param onClick La función que se ejecutará cuando se haga clic en el botón.
 * @param modifier El modificador de diseño para este componente.
 */
@Composable
private fun ProductItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

/**
 * Muestra un botón para enviar una recomendación por email.
 */
@Composable
fun EnviarEmail() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_SUBJECT, "Recomendación de producto")
            putExtra(Intent.EXTRA_TEXT, "Te recomiendo comprar esto.")
            // putExtra(Intent.EXTRA_STREAM, uriDeLaImagen) // si quieres adjuntar multimedia
        }
        context.startActivity(Intent.createChooser(intent, "Enviar recomendacion"))
    }) {
        Text(stringResource(R.string.enviar_recomendacion))
    }
}

/**
 * Muestra una imagen de un producto.
 *
 * @param thumbnail La URL de la imagen del producto.
 * @param modifier El modificador de diseño para este componente.
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
 * Muestra la información de un producto.
 *
 * @param product El objeto de producto que se mostrará.
 * @param modifier El modificador de diseño para este componente.
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
            text = stringResource(R.string.precio) + product.price + " €",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
    Pm_products_apiTheme {
        ProductList(Datasource().loadProductsWrapper(), onClick = {})
    }
}


@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
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
    val productWrapper = ProductWrapper(product = product, id = 1, expanded = true)
    Pm_products_apiTheme {
        ProductItem(productWrapper, onClick = {})
    }
}


@Preview(showBackground = true)
@Composable
fun StoreTopAppBarPreview() {

    Pm_products_apiTheme {
        StoreTopAppBar()
    }
}

