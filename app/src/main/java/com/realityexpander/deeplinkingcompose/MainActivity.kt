package com.realityexpander.deeplinkingcompose

import android.app.PendingIntent
import android.app.TaskStackBuilder
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
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Column

// Deep link validation are kept on github website:
// https://github.com/realityexpander/realityexpander.github.io
// in /.well_known/assetlinks.json

// Source video: https://www.youtube.com/watch?v=z6VlP0o_sDc&list=WL&index=37&t=1s

// To test from shell
//adb shell am start -a android.intent.action.VIEW \                                       1381  02:46:07 
// -c android.intent.category.BROWSABLE \
// -d "https://realityexpander.github.io/44"

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
                            Column {

                                Button(onClick = {
                                    navController.navigate("detail")
                                }) {
                                    Text(text = "To detail")
                                }

                                // Trigger a http-link to open the detail
                                // note: this is an example, but its here how other apps can open our app.
                                Button(
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://realityexpander.github.io/33")
                                        )
                                        val pendingIntent =
                                            TaskStackBuilder.create(applicationContext).run {
                                                addNextIntentWithParentStack(intent)
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    getPendingIntent(
                                                        0,
                                                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                                    )
                                                } else {
                                                    getPendingIntent(
                                                        0,
                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                    )
                                                }
                                            }
                                        pendingIntent.send()
                                    }
                                ) {
                                    Text("Trigger Deeplink (33)")
                                }
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