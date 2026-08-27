package br.com.curso.portalrickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import br.com.curso.portalrickandmorty.screens.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = (application as PortalApplication).database

        setContent {

            MaterialTheme {
                AppNavigation(database = database)
            }
        }
    }
}