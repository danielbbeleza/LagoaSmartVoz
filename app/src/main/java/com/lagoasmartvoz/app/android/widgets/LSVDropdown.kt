package com.lagoasmartvoz.app.android.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RoundedDropdown(
    selectedOption: String?,
    options: List<String>,
    placeholder: String,
    isExpanded: Boolean,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: (Boolean) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFededed), RoundedCornerShape(16.dp))
            .clickable {
                onExpanded(!isExpanded)
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // Text label or placeholder
        Text(
            text = selectedOption ?: placeholder,
            color = if (selectedOption == null) Color.Gray else Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        // Dropdown icon
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Expandir",
            tint = Color.Gray,
            modifier = Modifier.align(Alignment.CenterEnd)
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpanded(!isExpanded) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}
