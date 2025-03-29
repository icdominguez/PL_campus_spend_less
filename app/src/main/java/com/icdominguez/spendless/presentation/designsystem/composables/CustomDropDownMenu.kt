package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.model.getCategoryItems

@Composable
fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    labelIcon: @Composable () -> Unit = {},
    labelText: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
    expanded: Boolean = false,
    onDropDownMenuExpanded: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var isScrollable by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState.value) {
        isScrollable = scrollState.value > 0
    }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = LocalSpendlessColorsPalette.current.surfaceContainerLowest
                )
                .padding(
                    start = 4.dp,
                    top = 2.dp,
                    bottom = 2.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            labelIcon()
            labelText()

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { onDropDownMenuExpanded() }
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }

        Column {
            if (expanded) {
                Popup(
                    onDismissRequest = {
                        onDropDownMenuExpanded()
                    },
                    properties = PopupProperties(focusable = true),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 4.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .heightIn(max = 260.dp)
                                    .verticalScroll(scrollState),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                content()
                            }
                        }

                        if(scrollState.maxValue > 0) {
                            ScrollBar(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .heightIn(max = 240.dp),
                                scrollState = scrollState
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CustomDropDownMenuPreview() {
    CustomDropdownMenu()
}

@Composable
fun CustomDropDownMenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    trailingIcon: @Composable () -> Unit = {},
    divider: Boolean = false,
    text: String,
    isSelected: Boolean = false,
    labelModifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
    ) {
        Row(
            modifier = labelModifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            trailingIcon()

            Text(
                modifier = Modifier
                    .weight(1f),
                text = text,
                style = LocalSpendlessTypography.current.labelMedium.copy(
                    color = LocalSpendlessColorsPalette.current.onSurface,
                    fontWeight = FontWeight.Bold
                ),
            )

            Icon(
                modifier = Modifier
                    .alpha(if (isSelected) 1f else 0f)
                    .padding(end = 12.dp),
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = LocalSpendlessColorsPalette.current.primary,
            )

        }
        if (divider) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalSpendlessColorsPalette.current.outline)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomDropDownMenuItemPreview() {
    val category = getCategoryItems()[0]
    CustomDropDownMenuItem(
        onClick = {},
        trailingIcon = {
            CategoryItem(category = category)
        },
        text = stringResource(category.stringId)
    )
}