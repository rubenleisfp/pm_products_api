package com.fp.ui.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fp.R
import com.fp.data.repository.Datasource
import com.fp.ui.screens.artist.Artist
import com.fp.ui.screens.artist.ArtistList
import com.fp.ui.theme.Pm_products_apiTheme
import java.time.LocalDate

/**
 * Created by Your name on 16/09/2025.
 */

@Composable
fun ProductApp(productViewModel: ProductViewModel) {

    val products by productViewModel.products.collectAsState()
    val productState by productViewModel.uiState.collectAsState()
    ProductForm(productState = productState)
    /*
        val artists by artistViewModel.artists.collectAsState()
        ArtistList(
            artistList = artists
        )*/
}


@Composable
fun ProductForm(productState : ProductState) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        color = MaterialTheme.colorScheme.background
    ) {
        when (productState.action) {
            ActionEnum.IS_LOADING ->
                IsLoading()

            ActionEnum.ERROR -> ErrorScreen()

            ActionEnum.READ ->
                ProductList(
                    productList = productState.products
                )


        }
    }
}

@Composable
fun IsLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
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
fun ProductList(productList: List<Product>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(productList) { index, product ->
            ProductCard(
                product = product,
                numberInTheList = (index + 1).toString() // Convertir el índice en número (inicia desde 1)
            )
        }
    }
}



@Composable
fun ProductCard(product: Product, numberInTheList: String, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(15.dp)) {
        /*
        Image(
            painter = painterResource(artist.imageResourceId),
            contentDescription = artist.name,
            modifier = modifier
                .fillMaxWidth()
                .height(194.dp),
            contentScale = ContentScale.Crop
        )
        */
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp), // Margen para separar del resto
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp), // Espaciado interno del rectángulo
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = numberInTheList, fontSize = 20.sp)
                Text(text = product.title, fontWeight = FontWeight.Bold)
            }

        }

        Row(modifier = modifier) {
            Image(
                painter = painterResource(R.drawable.calendar),
                contentDescription = "Birthday",
                modifier = modifier
                    .height(50.dp)
                    .width(50.dp)
                    .padding(8.dp),

                contentScale = ContentScale.Fit
            )
            Text(text = product.description, modifier = modifier.padding(15.dp))
        }
        Row(modifier = modifier) {
            Image(
                painter = painterResource(R.drawable.dollar),
                contentDescription = "Salary",
                modifier = modifier
                    .height(50.dp)
                    .width(50.dp)
                    .padding(8.dp),

                contentScale = ContentScale.Fit
            )
            Text(text = product.price.toString(), modifier = modifier.padding(15.dp))
        }
        Spacer(modifier = modifier.height(10.dp))
        //Text(text = stringResource(artist.stringResourceId), fontSize = 10.sp)
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
fun ProductCardPreview() {
    val product = Product(1, "Boli", "Boli Bic sensancional", "Papeleria", 3.12, 3.0, 20)
    Pm_products_apiTheme {
        ProductCard(product, "1")
    }
}

