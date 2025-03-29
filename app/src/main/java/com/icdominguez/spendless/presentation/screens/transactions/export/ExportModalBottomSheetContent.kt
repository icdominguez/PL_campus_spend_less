package com.icdominguez.spendless.presentation.screens.transactions.export

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.CustomDropDownMenuItem
import com.icdominguez.spendless.presentation.designsystem.composables.CustomDropdownMenu
import com.icdominguez.spendless.presentation.designsystem.composables.SpendLessButton
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.model.ExportOption

@Composable
fun ExportModalBottomSheetContent(
    onDismiss: () -> Unit,
    state: ExportModalBottomSheetViewModel.State = ExportModalBottomSheetViewModel.State(),
    uiEvent: (ExportModalBottomSheetViewModel.Event) -> Unit = {},
    navigateToAuthenticationScreen: () -> Unit = {},
) {
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
        )
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = stringResource(R.string.export),
                    style = LocalSpendlessTypography.current.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    text = stringResource(R.string.export_to_csv),
                    style = LocalSpendlessTypography.current.bodySmall,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { onDismiss() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 4.dp,
                ),
            text = stringResource(R.string.export_range),
            style = LocalSpendlessTypography.current.labelSmall.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        CustomDropdownMenu(
            labelText = {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    text = stringResource(state.exportOptions.first { it.isSelected }.stringId),
                    style = LocalSpendlessTypography.current.labelMedium.copy(
                        color = LocalSpendlessColorsPalette.current.onSurface,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            },
            onDropDownMenuExpanded = {
                dropdownMenuExpanded = !dropdownMenuExpanded
            },
            content = {
                state.exportOptions.map { exportOption ->
                    CustomDropDownMenuItem(
                        onClick = {
                            uiEvent(ExportModalBottomSheetViewModel.Event.OnExportItemClicked(exportOption))
                            dropdownMenuExpanded = false
                        },
                        text = stringResource(exportOption.stringId),
                        isSelected = exportOption.isSelected,
                        divider = exportOption.name == ExportOption.ALL_DATA.name,
                        labelModifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 12.dp,
                                top = 12.dp,
                                bottom = 12.dp
                            )
                    )
                }
            },
            expanded = dropdownMenuExpanded,
        )

        Spacer(modifier = Modifier.height(12.dp))

        SpendLessButton(
            enabled = true,
            onClick = {
                uiEvent(ExportModalBottomSheetViewModel.Event.OnExportButtonClicked)
                onDismiss()
            },
            text = stringResource(R.string.export)
        )
    }

    if(state.shouldShowAuthenticationScreen) {
        navigateToAuthenticationScreen()
    }
}

@Composable
@Preview(showBackground = true)
fun ExportModalBottomSheetContentPreview() {
    ExportModalBottomSheetContent(onDismiss = {})
}