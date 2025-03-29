package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.model.Category
import com.icdominguez.spendless.presentation.model.Transaction
import java.time.LocalDateTime

@Composable
fun TransactionItem(
    transaction: Transaction,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .shadow(elevation = if(expanded) 4.dp else 0.dp, shape = RoundedCornerShape(16.dp))
            .background(
                color = if (expanded) LocalSpendlessColorsPalette.current.surfaceContainerLowest else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                enabled = transaction.description.isNotEmpty(),
                onClick = {
                    expanded = !expanded
                }
            )
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = if (transaction.category != null) LocalSpendlessColorsPalette.current.primaryFixed else LocalSpendlessColorsPalette.current.secondaryFixed.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    style = TextStyle(fontSize = 20.sp),
                    text = if (transaction.category != null) transaction.category.icon else "\uD83D\uDCB0"
                )

                if (transaction.description.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(
                                x = 2.dp,
                                y = 2.dp,
                            )
                            .background(
                                color = LocalSpendlessColorsPalette.current.surfaceContainerLowest,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(8.dp),
                            painter = painterResource(R.drawable.description_icon),
                            contentDescription = null,
                            tint = if (transaction.category != null) LocalSpendlessColorsPalette.current.primaryContainer else LocalSpendlessColorsPalette.current.secondaryFixedDim
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = transaction.transceiver,
                    style = LocalSpendlessTypography.current.labelMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = transaction.category?.stringId?.let { stringResource(it) } ?: stringResource(R.string.income),
                    style = LocalSpendlessTypography.current.bodyXSmall,
                )
            }

            Text(
                text = transaction.formatted,
                style = LocalSpendlessTypography.current.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.category != null) LocalSpendlessColorsPalette.current.onSurface else LocalSpendlessColorsPalette.current.onSuccess,
                )
            )
        }
        if (transaction.description.isNotEmpty() && expanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = 6.dp,
                            start = 52.dp,
                        ),
                    text = transaction.description,
                    style = LocalSpendlessTypography.current.bodySmall.copy(color = LocalSpendlessColorsPalette.current.onSurface)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionItemPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TransactionItem(
            transaction = Transaction(
                transceiver = "Amazon",
                amount = 7.500,
                date = LocalDateTime.now(),
                category = Category.HOME,
                formatted = "7 500"
            )
        )

        TransactionItem(
            transaction = Transaction(
                transceiver = "Pedro's bizum",
                amount =4.99,
                description = "Spotify monthly subscription",
                date = LocalDateTime.now(),
                formatted = "4,99"
            )
        )
    }
}