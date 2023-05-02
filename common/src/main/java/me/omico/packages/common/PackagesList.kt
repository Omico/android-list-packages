package me.omico.packages.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun PackageList(
    @StringRes titleResId: Int,
    packages: List<Pair<String, String>>
) {
    LazyColumn(
        modifier = run {
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        },
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { Text(text = stringResource(id = titleResId)) }
        item { Text(text = "Total apps: ${packages.size}") }
        items(packages) { (name, packageName) ->
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = name)
                    Text(text = packageName)
                }
            }
        }
    }
}
