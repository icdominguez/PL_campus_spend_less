package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.model.Currency
import com.icdominguez.spendless.model.DecimalSeparator
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.model.ThousandSeparator
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun PreferencesComponentView(
    expenseText: String,
    onExpenseFormatSelected: (String) -> Unit,
    selectedExpenseFormat: String,
    selectedCurrency: Currency,
    onCurrencyChanged: (Currency) -> Unit,
    selectedDecimalSeparator: String,
    onDecimalSeparatorSelected: (String) -> Unit,
    selectedThousandSeparator: String,
    onThousandSeparatorSelected: (String) -> Unit
) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Column {
        // region Expenses format
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(16.dp),
                )
                .background(
                    color = LocalSpendlessColorsPalette.current.surfaceContainerLowest,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(
                    top = 24.dp,
                    bottom = 20.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = expenseText,
                style = LocalSpendlessTypography.current.headlineLarge,
            )
            Text(
                text = stringResource(R.string.spend_this_month),
                style = LocalSpendlessTypography.current.bodySmall,
            )
        }

        CustomSelector(
            modifier = Modifier
                .padding(top = 16.dp),
            label = stringResource(R.string.expenses_format),
            onItemSelected = { onExpenseFormatSelected(it) },
            selectedItem = selectedExpenseFormat,
            items = ExpenseFormat.entries.map { expenseFormat ->
                expenseFormat.getExpensesFormatsWithCurrency(selectedCurrency)
            }
        )
        // endregion

        // region Currency
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 4.dp,
                ),
            text = stringResource(R.string.currency),
            style = LocalSpendlessTypography.current.labelSmall.copy(fontWeight = FontWeight.Bold),
        )

        CustomDropdownMenu(
            labelIcon = {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 8.dp
                        ),
                    text = selectedCurrency.symbol,
                    style = LocalSpendlessTypography.current.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
            },
            labelText = {
                Text(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    text = selectedCurrency.description,
                    style = LocalSpendlessTypography.current.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
            },
            content = {
                Currency.entries.forEach { currency ->
                    CustomDropDownMenuItem(
                        trailingIcon = {
                            Text(
                                modifier = Modifier
                                    .padding(end = 8.dp),
                                text = currency.symbol,
                                style = LocalSpendlessTypography.current.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                            )
                        },
                        text = currency.description,
                        isSelected = currency == selectedCurrency,
                        onClick = {
                            onCurrencyChanged(currency)
                            isDropDownMenuExpanded = false
                        },
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
            expanded = isDropDownMenuExpanded,
            onDropDownMenuExpanded = { isDropDownMenuExpanded = !isDropDownMenuExpanded }
        )
        // endregion

        // region decimal separator
        CustomSelector(
            modifier = Modifier
                .padding(top = 16.dp),
            label = "Decimal separator",
            onItemSelected = { onDecimalSeparatorSelected(it) },
            selectedItem = selectedDecimalSeparator,
            items = DecimalSeparator.entries.map { it.getDecimalSeparatorExample() },
        )
        // endregion

        // region thousands separator
        CustomSelector(
            modifier = Modifier
                .padding(top = 16.dp),
            label = "Thousand separator",
            onItemSelected = { onThousandSeparatorSelected(it) },
            selectedItem = selectedThousandSeparator,
            items = ThousandSeparator.entries.map { it.getThousandSeparatorExample() },
        )
        // endregion
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferencesComponentViewPreview() {
    PreferencesComponentView(
        expenseText = "10.382,45",
        onExpenseFormatSelected = {},
        selectedExpenseFormat = "10.382,45",
        selectedCurrency = Currency.EUR,
        onCurrencyChanged = {},
        selectedDecimalSeparator = DecimalSeparator.DOT.getDecimalSeparatorExample(),
        onDecimalSeparatorSelected = {},
        selectedThousandSeparator = ThousandSeparator.COMMA.getThousandSeparatorExample(),
        onThousandSeparatorSelected = {}
    )
}