package br.com.curso.portalrickandmorty.screens.portal

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import br.com.curso.portalrickandmorty.notification.NotificationHelper
import br.com.curso.portalrickandmorty.service.SyncForegroundService

@Composable
fun MyPortalScreen(
    viewModel: PortalViewModel
) {
    val context = LocalContext.current
    val location by viewModel.location.collectAsState()
    val isTracking by viewModel.isTracking.collectAsState()
    
    val notificationHelper = remember { NotificationHelper(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            viewModel.startTracking()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationHelper.showNotification(
                title = "Portal Rick and Morty",
                message = "O portal foi aberto com sucesso!"
            )
        }
    }

    LaunchedEffect(Unit) {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFineLocation || hasCoarseLocation) {
            viewModel.startTracking()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Meu Portal",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isTracking) "Portal Ativo" else "Portal Inativo",
            fontSize = 20.sp,
            color = if (isTracking) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (location != null) {
            Text(text = "Latitude: ${location?.latitude}", fontSize = 18.sp)
            Text(text = "Longitude: ${location?.longitude}", fontSize = 18.sp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Você está pronto para viajar pelo multiverso!",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            Text(text = "Buscando coordenadas...", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                val intent = Intent(context, SyncForegroundService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Monitoramento (Service)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                val intent = Intent(context, SyncForegroundService::class.java)
                context.stopService(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Parar Monitoramento")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val hasNotificationPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasNotificationPermission) {
                        notificationHelper.showNotification(
                            title = "Portal Rick and Morty",
                            message = "O portal foi aberto com sucesso!"
                        )
                    } else {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    notificationHelper.showNotification(
                        title = "Portal Rick and Morty",
                        message = "O portal foi aberto com sucesso!"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Testar Notificação")
        }
    }
}