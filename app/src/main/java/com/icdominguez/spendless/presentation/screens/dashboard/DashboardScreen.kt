package com.icdominguez.spendless.presentation.screens.dashboard

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.icdominguez.spendless.R
import com.icdominguez.spendless.core.formatTransaction
import com.icdominguez.spendless.core.toTodayYesterdayOrDate
import com.icdominguez.spendless.model.DecimalSeparator
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.model.ThousandSeparator
import com.icdominguez.spendless.presentation.designsystem.composables.CategoryIcon
import com.icdominguez.spendless.presentation.designsystem.composables.CustomFloatingActionButton
import com.icdominguez.spendless.presentation.designsystem.composables.CustomLoadingIndicator
import com.icdominguez.spendless.presentation.screens.transactions.export.ExportModalBottomSheetContent
import com.icdominguez.spendless.presentation.designsystem.composables.StickyHeader
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.screens.transactions.export.ExportModalBottomSheetViewModel
import com.icdominguez.spendless.presentation.screens.transactions.create.CreateTransactionModalBottomSheet
import com.icdominguez.spendless.presentation.screens.transactions.create.CreateTransactionModalBottomSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardScreenViewModel.State = DashboardScreenViewModel.State(),
    uiEvent: (DashboardScreenViewModel.Event) -> Unit = {},
    navigateToTransactions: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateToAuthentication: () -> Unit = {},
    isLaunchedFromWidget: Boolean = false,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val createTransactionModalBottomSheetViewModel = hiltViewModel<CreateTransactionModalBottomSheetViewModel>()
    val exportModalBottomSheetViewModel = hiltViewModel<ExportModalBottomSheetViewModel>()

    var showCreateTransactionModalBottomSheet by remember { mutableStateOf(false) }
    var showExportModalBottomSheet by remember { mutableStateOf(false) }

    val hasCheckedLaunchedFromWidget = rememberSaveable { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            LocalSpendlessColorsPalette.current.primary,
                            LocalSpendlessColorsPalette.current.onPrimaryFixed,
                        ),
                        center = Offset(
                            x = 0f,
                            y = 300f,
                        ),
                        radius = 1000f
                    )
                ),
        ) {
            if(state.isLoading) {
                CustomLoadingIndicator()
            } else {
                // region dashboard
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .weight(0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 8.dp,
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = state.user?.username ?: "",
                            style = LocalSpendlessTypography.current.titleLarge.copy(color = LocalSpendlessColorsPalette.current.background)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFF4A1F86),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        showExportModalBottomSheet = true
                                    }
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .padding(all = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FileDownload,
                                    contentDescription = null,
                                    tint = LocalSpendlessColorsPalette.current.background,
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFF4A1F86),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        navigateToSettings()
                                    }
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .padding(all = 8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = null,
                                    tint = LocalSpendlessColorsPalette.current.background,
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = state.accountBalance,
                                style = LocalSpendlessTypography.current.displayLarge.copy(color = LocalSpendlessColorsPalette.current.background),
                            )
                            Text(
                                modifier = Modifier
                                    .padding(top = 2.dp),
                                text = stringResource(R.string.account_balance),
                                style = LocalSpendlessTypography.current.bodySmall.copy(color = LocalSpendlessColorsPalette.current.background)
                            )
                        }
                    }

                    Column {
                        // region Most popular category
                        state.maxCategory?.let {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                    )
                                    .background(
                                        color = Color(0xFF51337B),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(
                                        start = 8.dp,
                                        top = 8.dp,
                                        bottom = 8.dp,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                CategoryIcon(
                                    text = it.icon,
                                    size = 56.dp
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                ) {
                                    Text(
                                        text = stringResource(it.stringId),
                                        style = LocalSpendlessTypography.current.titleLarge.copy(color = LocalSpendlessColorsPalette.current.onPrimary)
                                    )
                                    Text(
                                        text = stringResource(R.string.most_popular_category),
                                        style = LocalSpendlessTypography.current.bodyXSmall.copy(color = LocalSpendlessColorsPalette.current.onPrimary)
                                    )
                                }
                            }
                        }
                        // endregion

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                )
                        ) {
                            // region largest transaction
                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .background(
                                        color = LocalSpendlessColorsPalette.current.primaryFixed,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 14.dp,
                                    )
                                    .weight(2f)
                            ) {
                                if(state.transactions.none { it.category != null }) {
                                    Text(
                                        textAlign = TextAlign.Center,
                                        text = stringResource(R.string.largest_transaction),
                                        style = LocalSpendlessTypography.current.titleMedium.copy(color = LocalSpendlessColorsPalette.current.onSurface)
                                    )
                                } else {
                                    val biggestTransaction = state.transactions.filter { it.category != null }.maxByOrNull { it.amount }

                                    biggestTransaction?.let {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f),
                                            ) {
                                                Text(
                                                    text = it.transceiver,
                                                    style = LocalSpendlessTypography.current.titleLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                    ),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                )
                                                Text(
                                                    modifier = Modifier
                                                        .padding(top = 2.dp),
                                                    text = stringResource(R.string.largest_transaction),
                                                    style = LocalSpendlessTypography.current.bodyXSmall.copy(color = LocalSpendlessColorsPalette.current.onSurface),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                )
                                            }

                                            Column (
                                                modifier = Modifier
                                                    .padding(start = 24.dp)
                                            ) {
                                                Text(
                                                    text = it.formatted,
                                                    style = LocalSpendlessTypography.current.titleLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                    )
                                                )

                                                Text(
                                                    modifier = Modifier
                                                        .padding(top = 2.dp),
                                                    text = it.date.toLocalDate().toTodayYesterdayOrDate(LocalContext.current),
                                                    style = LocalSpendlessTypography.current.bodyXSmall.copy(color = LocalSpendlessColorsPalette.current.onSurface)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            // endregion
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = LocalSpendlessColorsPalette.current.secondaryFixed,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(
                                        start = 12.dp,
                                        top = 14.dp,
                                        bottom = 14.dp,
                                    )
                                    .weight(1f)
                            ) {
                                Column {
                                    Text(
                                        text = state.lastWeekBalance.formatTransaction(
                                            currency = state.user?.currency ?: "$",
                                            expenseFormat = ExpenseFormat.entries.firstOrNull { it.name == state.user?.expensesFormat } ?: ExpenseFormat.LESS,
                                            decimalSeparator = DecimalSeparator.entries.firstOrNull { it.name == state.user?.decimalSeparator }?.symbol ?: DecimalSeparator.COMMA.symbol,
                                            thousandSeparator = ThousandSeparator.entries.firstOrNull { it.name == state.user?.thousandSeparator }?.symbol ?: ThousandSeparator.SPACE.symbol,
                                            isExpense = false,
                                        ),
                                        style = LocalSpendlessTypography.current.titleLarge.copy(color = LocalSpendlessColorsPalette.current.onSurface)
                                    )

                                    Text(
                                        text = stringResource(R.string.previous_week),
                                        style = LocalSpendlessTypography.current.bodyXSmall.copy(color = LocalSpendlessColorsPalette.current.onSurface)
                                    )
                                }
                            }
                        }
                    }
                }
                // endregion
                // region transactions
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .clip(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                            )
                        )
                        .background(
                            color = LocalSpendlessColorsPalette.current.background,
                        )
                ) {
                    CustomFloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = paddingValues.calculateBottomPadding())
                            .zIndex(1f),
                        onClick = {
                            showCreateTransactionModalBottomSheet = true
                        }
                    )
                    if (state.transactions.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "\uD83D\uDCB8",
                                style = TextStyle(fontSize = 96.sp)
                            )

                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                text = stringResource(R.string.no_transactions),
                                style = LocalSpendlessTypography.current.titleLarge.copy(
                                    color = Color(0xFF000000),
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 16.dp,
                                    start = 12.dp,
                                    end = 12.dp,
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.latest_transaction),
                                    style = LocalSpendlessTypography.current.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = {
                                                navigateToTransactions()
                                            }
                                        ),
                                    text = stringResource(R.string.view_all),
                                    style = LocalSpendlessTypography.current.titleMedium.copy(color = LocalSpendlessColorsPalette.current.primary),
                                )
                            }

                            StickyHeader(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                list = state.transactions,
                            )
                        }
                    }
                }
                // endregion
            }

            if(showCreateTransactionModalBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier
                        .padding(top = paddingValues.calculateTopPadding()),
                    onDismissRequest = {
                        showCreateTransactionModalBottomSheet = false
                    },
                    sheetState = sheetState,
                    dragHandle = {},
                    content = {
                        CreateTransactionModalBottomSheet(
                            onDismissRequest = {
                                showCreateTransactionModalBottomSheet = false
                            },
                            state = createTransactionModalBottomSheetViewModel.state.collectAsStateWithLifecycle().value,
                            uiEvent = createTransactionModalBottomSheetViewModel::uiEvent
                        )
                    },
                    shape = RoundedCornerShape(
                        topStart = 28.dp,
                        topEnd = 28.dp,
                    )
                )
            }

            if(showExportModalBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier.fillMaxHeight(0.75f),
                    onDismissRequest = {
                        showExportModalBottomSheet = false
                    },
                    dragHandle = {},
                    content = {
                        ExportModalBottomSheetContent(
                            onDismiss = {
                                showExportModalBottomSheet = false
                            },
                            state = exportModalBottomSheetViewModel.state.collectAsStateWithLifecycle().value,
                            uiEvent = exportModalBottomSheetViewModel::uiEvent,
                            navigateToAuthenticationScreen = navigateToAuthentication
                        )
                    }
                )
            }

            if(state.shouldShowAuthenticationScreen) {
                navigateToAuthentication()
                uiEvent(DashboardScreenViewModel.Event.OnAuthenticationFinished)
            }

            if(!hasCheckedLaunchedFromWidget.value) {
                hasCheckedLaunchedFromWidget.value = true
                if(isLaunchedFromWidget) {
                    showCreateTransactionModalBottomSheet = true
                }
            }
        }
    }
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    DashboardScreen(state = DashboardScreenViewModel.State(lastWeekBalance = 50.0))
}