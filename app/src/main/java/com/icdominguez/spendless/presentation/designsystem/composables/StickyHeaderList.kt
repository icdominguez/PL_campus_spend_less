package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.core.toTodayYesterdayOrDate
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.model.Transaction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickyHeader(
    modifier: Modifier = Modifier,
    list: List<Transaction>,
) {
    val orderedList = list.sortedByDescending { it.date }.groupBy { it.date.toLocalDate() }

    LazyColumn(
        modifier = modifier
    ) {
        orderedList.map { dateGroup ->
            stickyHeader {
                Row(
                    modifier = Modifier
                        .background(color = LocalSpendlessColorsPalette.current.background)
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = dateGroup.key.toTodayYesterdayOrDate(LocalContext.current),
                        style = LocalSpendlessTypography.current.bodyXSmall,
                    )
                }
            }

            items(dateGroup.value) {
                TransactionItem(transaction = it)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StickyHeaderPreview() {

}