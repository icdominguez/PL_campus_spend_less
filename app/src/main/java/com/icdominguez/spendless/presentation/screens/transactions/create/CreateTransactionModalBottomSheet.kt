package com.icdominguez.spendless.presentation.screens.transactions.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.core.Commons.DECIMAL_FORMAT_PATTERN
import com.icdominguez.spendless.core.Commons.EMPTY_STRING
import com.icdominguez.spendless.core.Commons.MAX_DECIMALS
import com.icdominguez.spendless.core.Commons.MAX_INPUT_VALUE
import com.icdominguez.spendless.core.Commons.MIN_INPUT_VALUE_TO_REPLACE
import com.icdominguez.spendless.model.DecimalSeparator
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.model.ThousandSeparator
import com.icdominguez.spendless.presentation.designsystem.composables.CategoryIcon
import com.icdominguez.spendless.presentation.designsystem.composables.CustomDropDownMenuItem
import com.icdominguez.spendless.presentation.designsystem.composables.CustomDropdownMenu
import com.icdominguez.spendless.presentation.designsystem.composables.SpendLessButton
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.designsystem.composables.CustomTextField
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun CreateTransactionModalBottomSheet(
    onDismissRequest: () -> Unit,
    state: CreateTransactionModalBottomSheetViewModel.State = CreateTransactionModalBottomSheetViewModel.State(),
    uiEvent: (CreateTransactionModalBottomSheetViewModel.Event) -> Unit = {},
) {
    var categoryDropdownMenuExpanded by remember { mutableStateOf(false) }
    var recurringFrequencyMenuExpanded by remember { mutableStateOf(false) }

    var amountString by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        uiEvent(CreateTransactionModalBottomSheetViewModel.Event.OnDialogOpened)
    }

    Column(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    start = 16.dp,
                    end = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.create_transaction),
                style = LocalSpendlessTypography.current.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { onDismissRequest() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        }

        // region transaction selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 4.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
                .background(
                    color = LocalSpendlessColorsPalette.current.primaryContainer.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(
                modifier =  Modifier
                    .fillMaxWidth()
                    .padding(all = 4.dp)
            ) {
                state.transactionTypes.map { transactionType ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = {
                                    uiEvent(
                                        CreateTransactionModalBottomSheetViewModel.Event.OnTransactionTypeChanged(
                                            transactionType
                                        )
                                    )
                                }
                            )
                            .background(
                                color = if (transactionType.isSelected) LocalSpendlessColorsPalette.current.surfaceContainerLowest else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(
                                vertical = 8.dp,
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 4.dp),
                            painter = painterResource(transactionType.icon),
                            contentDescription = null,
                            tint = LocalSpendlessColorsPalette.current.primary,
                        )
                        Text(
                            text = stringResource(transactionType.stringId),
                            style = LocalSpendlessTypography.current.titleMedium.copy(color = LocalSpendlessColorsPalette.current.primary),
                        )
                    }
                }
            }
        }
        // endregion

        // region transaction info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 60.dp,
                    bottom = 62.dp,
                    start = 56.dp,
                    end = 56.dp
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CustomTextField(
                value = state.transceiverText,
                onValueChange = {
                    uiEvent(CreateTransactionModalBottomSheetViewModel.Event.OnTransceiverTextChanged(it))
                },
                label = if(state.transactionTypes.first().isSelected) stringResource(R.string.receiver) else stringResource(
                    R.string.sender),
                textStyle = LocalSpendlessTypography.current.titleMedium.copy(
                    color = LocalSpendlessColorsPalette.current.onSurface,
                    textAlign = TextAlign.Center,
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text =
                    if(state.transactionTypes.first().isSelected && state.user?.expensesFormat == ExpenseFormat.LESS.name)
                        "-${state.user.currency}"
                    else if(state.transactionTypes.first().isSelected && state.user?.expensesFormat == ExpenseFormat.PARENTHESIS.name)
                        "(${state.user.currency}"
                    else state.user?.currency ?: "€",
                    style = LocalSpendlessTypography.current.displayMedium.copy(
                        color = if(state.transactionTypes.first().isSelected) LocalSpendlessColorsPalette.current.error else LocalSpendlessColorsPalette.current.onSuccess
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                BasicTextField(
                    modifier = Modifier
                        .width(IntrinsicSize.Min),
                    value = amountString,
                    onValueChange = { newValue ->
                        val thousandSeparatorValue = ThousandSeparator.entries.first { it.name == state.user?.thousandSeparator }
                        val decimalSeparator = DecimalSeparator.entries.first { it.name == state.user?.decimalSeparator }

                        newValue.text.lastOrNull()?.let {
                            if(newValue.text.last() == thousandSeparatorValue.symbol.single()
                                || newValue.text.last() == decimalSeparator.symbol.single() && newValue.text.length == 1) return@BasicTextField
                        }

                        if(newValue.text.count { it == decimalSeparator.symbol.single() } > 1) return@BasicTextField

                        if(newValue.text.substringAfter(decimalSeparator.symbol, EMPTY_STRING).length > MAX_DECIMALS) return@BasicTextField

                        val value = newValue.text
                            .replace(thousandSeparatorValue.symbol, EMPTY_STRING)
                            .replace(decimalSeparator.symbol, ".")
                        val numericValue = value.toBigDecimalOrNull()

                        val formatter = DecimalFormat(DECIMAL_FORMAT_PATTERN)
                        formatter.decimalFormatSymbols = DecimalFormatSymbols(Locale.US).run {
                            this.groupingSeparator = thousandSeparatorValue.symbol.single()
                            this.decimalSeparator = decimalSeparator.symbol.single()
                            this
                        }

                        if(numericValue == null || numericValue < BigDecimal(MAX_INPUT_VALUE)) {
                            val formattedValue = when {
                                value.isEmpty() -> EMPTY_STRING
                                numericValue != null && numericValue >= BigDecimal(
                                    MIN_INPUT_VALUE_TO_REPLACE
                                ) -> {
                                    if(newValue.text.last() == decimalSeparator.symbol.single()) {
                                        "${formatter.format(numericValue)}${decimalSeparator.symbol}"
                                    } else {
                                        formatter.format(numericValue)
                                    }
                                }
                                else -> newValue.text.replace(thousandSeparatorValue.symbol, EMPTY_STRING)
                            }

                            amountString = TextFieldValue(
                                text = formattedValue,
                                selection = TextRange(formattedValue.length)
                            )

                            if(amountString.text.isNotEmpty()) {
                                uiEvent(
                                    CreateTransactionModalBottomSheetViewModel.Event.OnAmountTextChanged(
                                        numericValue?.toDouble() ?: 0.0
                                    )
                                )
                            }
                        }
                    },
                    cursorBrush = SolidColor(LocalSpendlessColorsPalette.current.primary),
                    textStyle = LocalSpendlessTypography.current.displayMedium.copy(
                        color = LocalSpendlessColorsPalette.current.onSurface,
                    ),
                    decorationBox = { innerTextField ->
                        if (amountString.text.isEmpty()) {
                            Text(
                                text = "00.00",
                                style = LocalSpendlessTypography.current.displayMedium.copy(
                                    color = LocalSpendlessColorsPalette.current.onSurface.copy(0.4f),
                                )
                            )
                        }
                        innerTextField()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.width(4.dp))

                if(state.transactionTypes.first().isSelected && state.user?.expensesFormat == ExpenseFormat.PARENTHESIS.name) {
                    Text(
                        text = ")",
                        style = LocalSpendlessTypography.current.displayMedium.copy(
                            color = LocalSpendlessColorsPalette.current.error
                        )
                    )

                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = state.note,
                onValueChange = {
                    uiEvent(CreateTransactionModalBottomSheetViewModel.Event.OnNoteTextChanged(it))
                },
                label = stringResource(R.string.add_note),
                textStyle = LocalSpendlessTypography.current.bodyMedium.copy(
                    color = LocalSpendlessColorsPalette.current.onSurface,
                    textAlign = TextAlign.Center,
                )
            )
        }

        if(state.transactionTypes.first().isSelected) {
            val selectedCategory = state.categories.first { it.isSelected }

            CustomDropdownMenu(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                labelIcon = {
                    CategoryIcon(text = selectedCategory.icon)
                },
                labelText = {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = stringResource(selectedCategory.stringId),
                        style = LocalSpendlessTypography.current.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                },
                content = {
                    state.categories.map { category ->
                        CustomDropDownMenuItem(
                            trailingIcon = {
                                CategoryIcon(text = category.icon)
                                Spacer(modifier = Modifier.width(8.dp))
                            },
                            text = stringResource(category.stringId),
                            onClick = {
                                categoryDropdownMenuExpanded = false
                                uiEvent(
                                    CreateTransactionModalBottomSheetViewModel.Event.OnCategoryChanged(
                                        category
                                    )
                                )
                            },
                            isSelected = category.isSelected,
                            labelModifier = Modifier
                                .padding(all = 4.dp)
                        )
                    }
                },
                expanded = categoryDropdownMenuExpanded,
                onDropDownMenuExpanded = { categoryDropdownMenuExpanded = !categoryDropdownMenuExpanded }
            )

            Spacer(modifier = Modifier.height(8.dp))

            val recurringFrequencySelected = state.recurringFrequencyItems.first { it.isSelected }

            CustomDropdownMenu(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                labelIcon = {
                    CategoryIcon(
                        text = "\uD83D\uDD04",
                        backgroundColor = LocalSpendlessColorsPalette.current.tertiaryContainer,
                    )
                },
                labelText = {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = "${stringResource(recurringFrequencySelected.stringId)} ${recurringFrequencySelected.dayValue}",
                        style = LocalSpendlessTypography.current.labelMedium.copy(
                            color = LocalSpendlessColorsPalette.current.onSurface,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                },
                content = {
                    state.recurringFrequencyItems.map { recurringFrequency ->
                        CustomDropDownMenuItem(
                            onClick = {
                                uiEvent(
                                    CreateTransactionModalBottomSheetViewModel.Event.OnRecurringFrequencyChanged(
                                        recurringFrequency
                                    )
                                )
                                recurringFrequencyMenuExpanded = false
                            },
                            text = "${stringResource(recurringFrequency.stringId)} ${recurringFrequency.dayValue}",
                            isSelected = recurringFrequency.isSelected,
                            labelModifier = Modifier
                                .padding(
                                    start = 52.dp,
                                    end = 12.dp,
                                    top = 12.dp,
                                    bottom = 12.dp
                                )
                        )
                    }
                },
                expanded = recurringFrequencyMenuExpanded,
                onDropDownMenuExpanded = { recurringFrequencyMenuExpanded = !recurringFrequencyMenuExpanded }
            )
        }
        // endregion

        // region create button
        SpendLessButton(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                ),
            enabled = state.isCreateTransactionButtonEnabled,
            onClick = {
                uiEvent(CreateTransactionModalBottomSheetViewModel.Event.OnCreateTransactionButtonClicked)
                onDismissRequest()
            },
            text = stringResource(R.string.create)
        )

        Spacer(modifier = Modifier
            .weight(1f))
        // endregion
    }
}

@Preview(showBackground = false)
@Composable
private fun CreateTransactionModalBottomSheetPreview() {
    CreateTransactionModalBottomSheet(
        state = CreateTransactionModalBottomSheetViewModel.State(),
        onDismissRequest = {},
        uiEvent = {}
    )
}