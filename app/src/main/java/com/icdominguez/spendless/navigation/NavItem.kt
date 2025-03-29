package com.icdominguez.spendless.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    val baseRoute: String,
    val navArgs: List<NavArg> = emptyList()
) {
    val route = run {
        val argsKey = navArgs.map { "{${it.key}}" }
        listOf(baseRoute)
            .plus(argsKey)
            .joinToString("/")
    }

    // region Unauthorized
    data object Login : NavItem(baseRoute = "login")
    data object Registration: NavItem(baseRoute = "registration")
    data object Pin: NavItem(baseRoute = "pin")
    data object RepeatPin: NavItem(baseRoute = "repeat_pin")
    data object OnBoardingPreferences: NavItem(baseRoute = "onboarding_preferences")
    // endregion

    // region Authorized
    data object Authentication: NavItem(baseRoute = "authentication")
    data object Dashboard: NavItem(baseRoute = "dashboard")
    data object CreateTransaction: NavItem(baseRoute = "create_transaction")
    data object Transactions: NavItem(baseRoute = "transactions")
    data object Security: NavItem(baseRoute = "security")
    data object Settings: NavItem(baseRoute = "settings")
    data object Preferences: NavItem(baseRoute = "preferences")
    // endregion
}

enum class NavArg(
    val key: String,
    val navType: NavType<*>,
)
