package com.icdominguez.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.icdominguez.spendless.presentation.designsystem.composables.RootView
import com.icdominguez.spendless.presentation.screens.authentication.AuthenticationScreen
import com.icdominguez.spendless.presentation.screens.authentication.AuthenticationScreenViewModel
import com.icdominguez.spendless.presentation.screens.unauthorized.login.LoginScreen
import com.icdominguez.spendless.presentation.screens.unauthorized.login.LoginScreenViewModel
import com.icdominguez.spendless.presentation.screens.unauthorized.pin.PinScreen
import com.icdominguez.spendless.presentation.screens.unauthorized.pin.PinScreenViewModel
import com.icdominguez.spendless.presentation.screens.unauthorized.registration.RegistrationScreen
import com.icdominguez.spendless.presentation.screens.unauthorized.registration.RegistrationScreenViewModel
import com.icdominguez.spendless.presentation.screens.unauthorized.repeatpin.RepeatPinScreen
import com.icdominguez.spendless.presentation.screens.unauthorized.repeatpin.RepeatPinScreenViewModel
import com.icdominguez.spendless.presentation.screens.dashboard.DashboardScreen
import com.icdominguez.spendless.presentation.screens.dashboard.DashboardScreenViewModel
import com.icdominguez.spendless.presentation.screens.onboarding_preferences.OnBoardingPreferencesScreen
import com.icdominguez.spendless.presentation.screens.onboarding_preferences.OnBoardingPreferencesScreenViewModel
import com.icdominguez.spendless.presentation.screens.settings.SettingsScreen
import com.icdominguez.spendless.presentation.screens.settings.SettingsScreenViewModel
import com.icdominguez.spendless.presentation.screens.settings.preferences.PreferencesScreen
import com.icdominguez.spendless.presentation.screens.settings.preferences.PreferencesScreenViewModel
import com.icdominguez.spendless.presentation.screens.settings.security.SecurityScreen
import com.icdominguez.spendless.presentation.screens.settings.security.SecurityScreenViewModel
import com.icdominguez.spendless.presentation.screens.transactions.AllTransactionScreenViewModel
import com.icdominguez.spendless.presentation.screens.transactions.AllTransactionsScreen
import com.icdominguez.spendless.presentation.screens.transactions.create.CreateTransactionModalBottomSheet
import com.icdominguez.spendless.presentation.screens.transactions.create.CreateTransactionModalBottomSheetViewModel

@Composable
fun Navigation(
    isUserLoggedIn: Boolean,
    isLaunchedFromWidget: Boolean = false,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if(isUserLoggedIn) Graph.AUTHORIZED else Graph.UNAUTHORIZED,
    ) {
        unauthorizedGraph(navController)
        authorizedGraph(navController, isLaunchedFromWidget)
    }
}

// region Unauthorized Graph
fun NavGraphBuilder.unauthorizedGraph(navController: NavController) {
    navigation(
        route = Graph.UNAUTHORIZED,
        startDestination = NavItem.Login.route
    ) {
        composable(route = NavItem.Login.route) {
            val viewmodel = hiltViewModel<LoginScreenViewModel>()

            LoginScreen(
                state = viewmodel.state.collectAsStateWithLifecycle().value,
                uiEvent = viewmodel::uiEvent,
                navigateToRegistrationScreen = {
                    navController.navigate(NavItem.Registration.route)
                },
                navigateToDashboard = {
                    navController.navigate(NavItem.Dashboard.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = NavItem.Registration.route) {
            val viewmodel = hiltViewModel<RegistrationScreenViewModel>()

            RegistrationScreen(
                state = viewmodel.state.collectAsStateWithLifecycle().value,
                uiEvent = viewmodel::uiEvent,
                navigateToPinScreen = {
                    navController.navigate(NavItem.Pin.route)
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NavItem.Pin.route) { backStackEntry ->
            val viewmodel = hiltViewModel<PinScreenViewModel>()

            PinScreen(
                state = viewmodel.state.collectAsStateWithLifecycle().value,
                uiEvent = viewmodel::uiEvent,
                navigateToRepeatPinScreen = {
                    navController.navigate(NavItem.RepeatPin.route)
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NavItem.RepeatPin.route) { backStackEntry ->
            val key = backStackEntry.id
            val viewmodel = hiltViewModel<RepeatPinScreenViewModel>(key = key)

            RepeatPinScreen(
                state = viewmodel.state.collectAsStateWithLifecycle().value,
                uiEvent = viewmodel::uiEvent,
                navigateToPreferences = {
                    navController.navigate(NavItem.OnBoardingPreferences.route)
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NavItem.OnBoardingPreferences.route) { backStackEntry ->
            val key = backStackEntry.id
            val viewmodel = hiltViewModel<OnBoardingPreferencesScreenViewModel>(key = key)

            OnBoardingPreferencesScreen(
                state = viewmodel.state.collectAsStateWithLifecycle().value,
                uiEvent = viewmodel::uiEvent,
                navigateBack = {
                    navController.popBackStack(NavItem.RepeatPin.route, true)
                },
                navigateToDashboard = {
                    navController.navigate("authorized_graph") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
// endregion


// region Authorized Graph
fun NavGraphBuilder.authorizedGraph(
    navController: NavController,
    isLaunchedFromWidget: Boolean = false,
) {
    navigation(
        route = Graph.AUTHORIZED,
        startDestination = NavItem.Dashboard.route
    ) {
        composable(route = NavItem.Authentication.route) {
            val viewmodel = hiltViewModel<AuthenticationScreenViewModel>()

            RootView(
                useLightStatusBarColors = true
            ) {
                AuthenticationScreen(
                    state = viewmodel.state.collectAsStateWithLifecycle().value,
                    uiEvent = viewmodel::uiEvent,
                    navigateToLogin = {
                        navController.navigate(NavItem.Login.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onPinEntered = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(route = NavItem.Dashboard.route) {
            val viewmodel = hiltViewModel<DashboardScreenViewModel>()
            val lifecycleOwner = LocalLifecycleOwner.current

            RootView(useLightStatusBarColors = false) {
                DashboardScreen(
                    state = viewmodel.state.collectAsStateWithLifecycle().value,
                    uiEvent = viewmodel::uiEvent,
                    navigateToTransactions = {
                        navController.navigate(NavItem.Transactions.route)
                    },
                    navigateToSettings = {
                        navController.navigate(NavItem.Settings.route)
                    },
                    navigateToAuthentication = {
                        navController.navigate(NavItem.Authentication.route)
                    },
                    isLaunchedFromWidget = isLaunchedFromWidget
                )

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            viewmodel.uiEvent(DashboardScreenViewModel.Event.OnNavigate)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
            }

        }

        composable(route = NavItem.Transactions.route) {
            val viewmodel = hiltViewModel<AllTransactionScreenViewModel>()

            RootView(useLightStatusBarColors = true) {
                AllTransactionsScreen(
                    state = viewmodel.state.collectAsStateWithLifecycle().value,
                    uiEvent = viewmodel::uiEvent,
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(route = NavItem.Settings.route) {
            val viewmodel = hiltViewModel<SettingsScreenViewModel>()

            RootView(useLightStatusBarColors = true) {
                SettingsScreen(
                    state = viewmodel.state.collectAsStateWithLifecycle().value,
                    uiEvent = viewmodel::uiEvent,
                    navigateToPreferencesScreen = {
                        navController.navigate(NavItem.Preferences.route)
                    },
                    navigateToSecurityScreen = {
                        navController.navigate(NavItem.Security.route)
                    },
                    navigateToLogin = {
                        navController.navigate(Graph.UNAUTHORIZED) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    navigateBack = {
                        navController.popBackStack()
                    },
                )
            }
        }

        composable(route = NavItem.Security.route) {
            val viewmodel = hiltViewModel<SecurityScreenViewModel>()

            SecurityScreen(
                state = viewmodel.state.collectAsStateWithLifecycle().value,
                uiEvent = viewmodel::uiEvent,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NavItem.Preferences.route) {
            val viewmodel = hiltViewModel<PreferencesScreenViewModel>()

            PreferencesScreen(
                state = viewmodel.state.collectAsStateWithLifecycle().value,
                uiEvent = viewmodel::uiEvent,
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToAuthentication = {
                    navController.navigate(NavItem.Authentication.route)
                },
            )
        }

        dialog(route = NavItem.CreateTransaction.route) {
            val viewmodel = hiltViewModel<CreateTransactionModalBottomSheetViewModel>()

            CreateTransactionModalBottomSheet(
                state = viewmodel.state.collectAsStateWithLifecycle().value,
                uiEvent = viewmodel::uiEvent,
                onDismissRequest = {}
            )
        }
    }
}
// endregion