package me.omico.packages.holdingpermissions

import android.Manifest
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import me.omico.packages.common.PackageList
import me.omico.packages.common.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PackageList(
                    titleResId = R.string.app_name,
                    packages = rememberPackagesWithHoldingPermissions(Manifest.permission.INTERNET)
                )
            }
        }
    }
}

@Composable
private fun Context.rememberPackagesWithHoldingPermissions(vararg permissions: String) =
    remember(this, permissions) {
        val packages = when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU ->
                packageManager.getPackagesHoldingPermissions(
                    permissions,
                    PackageManager.PackageInfoFlags.of(0)
                )
            else -> packageManager.getPackagesHoldingPermissions(permissions, 0)
        }
        packages
            .filter { packageInfo -> (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            .associate { packageInfo ->
                val label =
                    packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
                label to packageInfo.packageName
            }
            .toSortedMap()
            .toList()
    }
