package com.fp.ui.frontpage

import android.view.Surface
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fp.R
import com.fp.Screen

/**
 * A composable function that represents the front page or welcome screen of the application.
 *
 * This screen displays the application's logo, name, and a button that navigates
 * to the main store screen. It serves as the entry point for the user to start browsing.
 *
 * @param navController The [NavController] used for handling navigation actions,
 *                      specifically to navigate to the `StoreScreen`.
 */
@Composable
fun FrontPageScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ecommerce),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .size(200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {     navController.navigate(route = Screen.StoreScreen.route) }) {
                Text(text = stringResource(id = R.string.action_store))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FrontPageScreenPreview() {
    FrontPageScreen(navController = rememberNavController())
}
