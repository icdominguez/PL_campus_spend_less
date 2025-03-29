package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.model.Category
import com.icdominguez.spendless.presentation.model.CategoryItem

@Composable
fun CategoryItem(
    category: CategoryItem,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 4.dp,
                end = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryIcon(text = category.icon)

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(category.stringId),
            style = LocalSpendlessTypography.current.labelMedium,
        )

        if (category.isSelected) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = LocalSpendlessColorsPalette.current.primary
            )
        }
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
        CategoryItem(category = CategoryItem(
            name = Category.HOME.name,
            icon = Category.HOME.icon,
            stringId = Category.HOME.stringId,
            isSelected = false
        )
    )
}
