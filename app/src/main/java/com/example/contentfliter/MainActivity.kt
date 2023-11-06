package com.example.contentfliter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var checkBoxViolence: CheckBox
    private lateinit var checkBoxExplicitContent: CheckBox
    private lateinit var checkBoxBlockVPN: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       //app always running
        val intent = Intent()
        val packageName = packageName
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }

        checkBoxViolence = findViewById(R.id.checkBoxViolence)
        checkBoxExplicitContent = findViewById(R.id.checkBoxExplicitContent)
        checkBoxBlockVPN=findViewById(R.id.checkBoxBlockVPN)
        val btnApplyFilters: Button = findViewById(R.id.btnApplyFilters)

        // Load saved filter states
        val sharedPreferences: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        checkBoxViolence.isChecked = sharedPreferences.getBoolean("violence", false)
        checkBoxExplicitContent.isChecked = sharedPreferences.getBoolean("explicitContent", false)
        checkBoxBlockVPN.isChecked=sharedPreferences.getBoolean("blockVPN",false)

        btnApplyFilters.setOnClickListener {
            // Save filter states
            val editor = sharedPreferences.edit()
            editor.putBoolean("violence", checkBoxViolence.isChecked)
            editor.putBoolean("explicitContent", checkBoxExplicitContent.isChecked)
            editor.putBoolean("blockVPN",checkBoxBlockVPN.isChecked)
            editor.apply()

            // Apply filters (you would implement the filtering logic here)
            applyFilters(checkBoxViolence.isChecked, checkBoxExplicitContent.isChecked, checkBoxBlockVPN.isChecked)
        }
    }

    private fun applyFilters(
        violenceFilter: Boolean,
        explicitContentFilter: Boolean,
        blockVPN: Boolean
    ) {
        // Implement filtering logic here
        // You can use the selected filters to block or filter content in your app
        // For VPN blocking, you can use the following code

        if (blockVPN) {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            val isVpnActive = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true

            if (isVpnActive) {
                // VPN is active, take appropriate action (e.g., restrict access)
                showToast("disable vpn")
            } else {
                // VPN is not active, allow access
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

    }
}