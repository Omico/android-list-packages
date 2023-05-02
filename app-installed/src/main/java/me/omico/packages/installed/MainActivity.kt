package me.omico.packages.installed

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
                    R.string.app_name,
                    packages = rememberInstalledPackages()
                )
            }
        }
    }
}

@Composable
private fun Context.rememberInstalledPackages() = remember(this) {
    val packages = when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.TIRAMISU ->
            packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of((PackageManager.GET_META_DATA or PackageManager.GET_PERMISSIONS).toLong()))
        else -> packageManager.getInstalledPackages(PackageManager.GET_META_DATA or PackageManager.GET_PERMISSIONS)
    }
    packages
        .filter { packageInfo -> (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
        .filter { packageInfo -> packageInfo.requestedPermissions?.any { it == Manifest.permission.INTERNET } ?: false }
        .associate { packageInfo ->
            val label = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
            label to packageInfo.packageName
        }
        .toSortedMap()
        .toList()
}
