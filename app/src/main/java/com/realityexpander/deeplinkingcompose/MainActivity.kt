package com.realityexpander.deeplinkingcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.realityexpander.deeplinkingcompose.ui.theme.DeeplinkingComposeTheme

// Deep link validation are kept on github website:
// https://github.com/realityexpander/realityexpander.github.io
// in /.well_known/assetlinks.json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeeplinkingComposeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = {
                                navController.navigate("detail")
                            }) {
                                Text(text = "To detail")
                            }
                        }
                    }
                    composable(
                        route = "detail",
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "http://realityexpander.github.io/{id}"
                                action = Intent.ACTION_VIEW
                            },
                            navDeepLink {
                                uriPattern = "https://realityexpander.github.io/{id}"
                                action = Intent.ACTION_VIEW
                            }
                        ),
                        arguments = listOf(
                            navArgument("id") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) { entry ->
                        val id = entry.arguments?.getInt("id")
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "The id is $id")
                        }
                    }
                }
            }
        }
    }
}