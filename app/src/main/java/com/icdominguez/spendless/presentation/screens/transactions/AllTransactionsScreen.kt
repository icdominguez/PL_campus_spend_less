package com.icdominguez.spendless.presentation.screens.transactions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.CustomFloatingActionButton
import com.icdominguez.spendless.presentation.screens.transactions.export.ExportModalBottomSheetContent
import com.icdominguez.spendless.presentation.designsystem.composables.StickyHeader
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.screens.transactions.export.ExportModalBottomSheetViewModel
import com.icdominguez.spendless.presentation.screens.transactions.create.CreateTransactionModalBottomSheet
import com.icdominguez.spendless.presentation.screens.transactions.create.CreateTransactionModalBottomSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsScreen(
    state: AllTransactionScreenViewModel.State = AllTransactionScreenViewModel.State(),
    uiEvent: (AllTransactionScreenViewModel.Event) -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTransactionModalBottomSheet by remember { mutableStateOf(false) }

    var showExportModalBottomSheet by remember { mutableStateOf(false) }

    val createTransactionViewModel = hiltViewModel<CreateTransactionModalBottomSheetViewModel>()
    val exportTransactionViewModel = hiltViewModel<ExportModalBottomSheetViewModel>()

    BackHandler {
        if(!state.shouldShowAuthenticationScreen) {
            navigateBack()
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 4.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }

                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = stringResource(R.string.all_transactions),
                        style = LocalSpendlessTypography.current.titleLarge.copy(
                            color = LocalSpendlessColorsPalette.current.onSurface
                        )
                    )

                    IconButton(
                        onClick = {
                            showExportModalBottomSheet = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FileDownload,
                            contentDescription = null,
                        )
                    }
                }

                StickyHeader(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    list = state.transactions,
                )
            }

            CustomFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                onClick = {
                    // TODO: Comprobar si tiene que ir a authenticacion
                    showTransactionModalBottomSheet = true
                },
            )

            if(showTransactionModalBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier
                        .padding(top = paddingValues.calculateTopPadding()),
                    onDismissRequest = {
                        showTransactionModalBottomSheet = false
                    },
                    sheetState = sheetState,
                    dragHandle = {},
                    content = {
                        CreateTransactionModalBottomSheet(
                            onDismissRequest = {
                                showTransactionModalBottomSheet = false
                            },
                            state = createTransactionViewModel.state.collectAsStateWithLifecycle().value,
                            uiEvent = createTransactionViewModel::uiEvent,
                        )
                    },
                    shape = RoundedCornerShape(
                        topStart = 28.dp,
                        topEnd = 28.dp,
                    )
                )
            }
        }
    }

    if(state.shouldShowAuthenticationScreen) {
        /*AuthenticationScreen(
            onPinEntered = {
                uiEvent(AllTransactionScreenViewModel.Event.OnPinEntered)
            }
        )*/
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
                    state = exportTransactionViewModel.state.collectAsStateWithLifecycle().value,
                    uiEvent = exportTransactionViewModel::uiEvent
                )
            }
        )
    }
}

@Preview
@Composable
private fun TransactionScreensPreview() {
    AllTransactionsScreen()
}