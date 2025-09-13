package com.aurorus.lock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- CORE LOGIC MOVED HERE ---
        val devicePolicyManager = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(this, DeviceAdmin::class.java)
        val isAdminActive = devicePolicyManager.isAdminActive(componentName)

        if (isAdminActive) {
            // If we have permission, lock the screen and immediately close the app.
            devicePolicyManager.lockNow()
            finish() // This is the key to closing the activity.
        } else {
            // If we don't have permission, show the UI to request it.
            setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // The UI is now only for requesting permission.
                    PermissionRequestScreen()
                }
            }
        }
    }
}

@Composable
fun PermissionRequestScreen() {
    val context = LocalContext.current
    val componentName = ComponentName(context, DeviceAdmin::class.java)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Permission Needed",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "For one-tap locking, this app requires Device Administrator permission. Please activate it once.",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // The button's only job is now to ask for permission.
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                    putExtra(
                        DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "This permission is required to allow the app to lock the screen."
                    )
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Activate Permission")
        }
    }
}

