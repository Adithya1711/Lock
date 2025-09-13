package com.aurorus.lock

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * This is the Device Admin Receiver class. It's mostly empty for this simple
 * use case, but it's required for the Device Admin API to work.
 * The system uses this to notify you of admin-related events.
 */
class DeviceAdmin : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Toast.makeText(context, "Device Admin: Enabled", Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Toast.makeText(context, "Device Admin: Disabled", Toast.LENGTH_SHORT).show()
    }
}
