package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.model.ThousandSeparator
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun CustomSelector(
    modifier: Modifier = Modifier,
    label: String = "",
    onItemSelected: (String) -> Unit,
    items: List<String>,
    selectedItem: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 4.dp),
            text = label,
            style = LocalSpendlessTypography.current.labelSmall.copy(fontWeight = FontWeight.Bold),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = LocalSpendlessColorsPalette.current.primary.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(all = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.map { item ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            onClick = {
                                onItemSelected(item)
                            },
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .background(
                            color = if (selectedItem == item) LocalSpendlessColorsPalette.current.surfaceContainerLowest else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        text = item,
                        style = LocalSpendlessTypography.current.titleMedium.copy(
                            color = if (selectedItem == item) LocalSpendlessColorsPalette.current.onSurface else LocalSpendlessColorsPalette.current.onPrimaryFixed
                        ),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomSelectorPreview() {
    CustomSelector(
        label = "Thousand separator",
        onItemSelected = {},
        items = ThousandSeparator.entries.map { it.getThousandSeparatorExample() },
        selectedItem = ThousandSeparator.COMMA.getThousandSeparatorExample(),
    )
}