package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.designsystem.theme.SpendLessTheme

@Composable
fun KeypadComponent(
    pinWrote: List<Int> = emptyList(),
    onNumberClicked: (Int) -> Unit = {},
    onRemoveButtonClicked: () -> Unit = {},
    enabled: Boolean = true,
) {
    LazyRow(
        modifier = Modifier
            .padding(18.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(5) { index ->
            PinIndicator(
                enabled = enabled,
                isProvided = pinWrote.getOrNull(index) != null
            )
        }
    }

    LazyVerticalGrid(
        modifier = Modifier
            .padding(
                top = 32.dp,
                start = 40.dp,
                end = 40.dp
            ),
        columns = GridCells.Fixed(3),
        content = {
            items(12) { index ->
                if(index < 9) {
                    PinItem(
                        index = index + 1,
                        onClick = { onNumberClicked(index + 1) },
                        enabled = enabled,
                    )
                } else if(index == 10) {
                    PinItem(
                        index = 0,
                        onClick = { onNumberClicked(0) },
                        enabled = enabled,
                    )
                } else if(index == 11) {
                    RemoveItem(
                        onClick = { onRemoveButtonClicked() },
                        enabled = enabled,
                    )
                }
            }
        },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    )
}

@Composable
private fun PinItem(
    modifier: Modifier = Modifier,
    index: Int = 0,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(32.dp))
            .background(
                color = LocalSpendlessColorsPalette.current.primaryFixed.copy(
                    alpha = if(enabled) 1f else 0.5f
                )
            )
            .clickable(
                enabled = enabled,
                onClick = {
                    onClick()
                }
            )
            .padding(vertical = 34.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$index",
            style = LocalSpendlessTypography.current.headlineLarge.copy(
                color = LocalSpendlessColorsPalette.current.onPrimaryFixed.copy(
                    alpha = if(enabled) 1f else 0.5f
                ),
            ),
        )
    }
}

@Composable
fun RemoveItem(
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(32.dp))
            .background(color = LocalSpendlessColorsPalette.current.primaryFixed.copy(
                    alpha = if(enabled) 1f else 0.5f
                )
            )
            .clickable(
                enabled = enabled,
                onClick = {
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .padding(vertical = 32.dp)
                .size(40.dp),
            painter = painterResource(R.drawable.remove_icon),
            colorFilter = ColorFilter.tint(if(enabled) LocalSpendlessColorsPalette.current.onPrimaryFixed else LocalSpendlessColorsPalette.current.onBackground.copy(alpha = 0.3f)),
            contentDescription = null
        )
    }
}

@Composable
fun PinIndicator(
    isProvided: Boolean = false,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .background(
                color = if (isProvided) LocalSpendlessColorsPalette.current.primary else if (enabled) LocalSpendlessColorsPalette.current.onBackground.copy(alpha = 0.12f) else LocalSpendlessColorsPalette.current.onBackground.copy(alpha = 0.03f),
                shape = CircleShape,
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun KeypadComponentPreview() {
    SpendLessTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KeypadComponent(enabled = false)
        }
    }
}

@Preview
@Composable
private fun PinItemPreview() {
    PinItem()
}

@Preview
@Composable
private fun PinIndicatorPreview() {
    PinIndicator()
}