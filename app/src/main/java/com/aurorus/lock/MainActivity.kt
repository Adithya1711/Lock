package com.aurorus.lock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
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
        setContent {
            // We've removed the custom theme wrapper to keep it simple for now.
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LockScreenApp()
            }
        }
    }
}

@Composable
fun LockScreenApp() {
    val context = LocalContext.current
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    // The componentName now correctly uses your package structure.
    val componentName = ComponentName(context, DeviceAdmin::class.java)

    // A state to track if the app is a device admin.
    var isAdminActive by remember {
        mutableStateOf(devicePolicyManager.isAdminActive(componentName))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isAdminActive) "Ready to Lock" else "Permission Needed",
            fontSize = 24.sp,
            color = if (isAdminActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isAdminActive) {
                "Device admin permission is active. You can now lock the screen."
            } else {
                "This app requires Device Administrator permission to lock the screen. Please activate it."
            },
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isAdminActive) {
                    devicePolicyManager.lockNow()
                } else {
                    val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                        putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                        putExtra(
                            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "This app needs permission to lock the screen."
                        )
                    }
                    context.startActivity(intent)
                    // NOTE: The UI state might not update automatically after returning
                    // from the settings. A restart of the app will show the correct state.
                    // A more advanced solution would use ActivityResultLauncher.
                    isAdminActive = devicePolicyManager.isAdminActive(componentName)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isAdminActive) "Lock Screen Now" else "Activate Permission")
        }
    }
}
