package com.example.contentfliter;

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.UserManager

class AdminReceiver : DeviceAdminReceiver() {
        override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val deviceAdmin = ComponentName(context, AdminReceiver::class.java)
        dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_CONFIG_VPN)
        }
        }