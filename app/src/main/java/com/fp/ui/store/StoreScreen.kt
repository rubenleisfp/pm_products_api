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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
 * Created by Your name on 16/09/2025.
 */

@Composable
fun StoreApp(storeViewModel: StoreViewModel) {
    val productState by storeViewModel.uiState.collectAsState()
    StoreGrid(storeState = productState)
}

@Composable
fun StoreGrid(storeState: StoreState, modifier : Modifier = Modifier) {

    Scaffold(
        topBar = {
            StoreTopAppBar()
        }
    ) { innerPadding ->

        when (storeState.action) {
            ActionEnum.IS_LOADING ->
                IsLoading()

            ActionEnum.ERROR -> ErrorScreen()

            ActionEnum.READ ->
                Column(
                    modifier = modifier
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(36.dp)
                ) {
                    ProductList(
                        productList = storeState.productPageWrapper
                        modifier = modifier
                    )
                }


        }
    }
}

@Composable
fun ProductList(productList: List<Product>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(productList) { index, product ->
            ProductItem(
                product = product,
                modifier = modifier
            )
        }
    }
}


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


@Composable
fun IsLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.image_size_large)),
            painter = painterResource(R.drawable.loading),
            contentDescription = null
        )

     }
}

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


@Composable
fun ProductItem(product: Product, modifier: Modifier = Modifier ) {
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
                ProductIcon(product.thumbnail)
                ProductInformation(product = product)
                Spacer(modifier = Modifier.weight(1f))

            }
            EnviarEmail()

        }
    }
}

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


@Composable
fun ProductIcon(
    thumbnail: String,
    modifier: Modifier = Modifier
)  {
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
        ProductList(Datasource().loadProducts())
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
    Pm_products_apiTheme {
        ProductItem(product)
    }
}


@Preview(showBackground = true)
@Composable
fun StoreTopAppBarPreview() {

    Pm_products_apiTheme {
        StoreTopAppBar()
    }
}

