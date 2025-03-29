package com.icdominguez.spendless.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.icdominguez.spendless.core.Commons.IS_LAUNCHED_FROM_WIDGET
import com.icdominguez.spendless.data.googletrusted.GoogleTrustedTimeManager
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.navigation.Navigation
import com.icdominguez.spendless.presentation.designsystem.theme.SpendLessTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getUserLoggedInUseCAse: GetUserLoggedInUseCase

    @Inject
    lateinit var googleTrustedTimeManager: GoogleTrustedTimeManager

    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { !isReady }

        lifecycleScope.launch {
            googleTrustedTimeManager.initialize()
            val result = getUserLoggedInUseCAse()

            setContent {
                val isLaunchedFromWidget = intent.getBooleanExtra(IS_LAUNCHED_FROM_WIDGET, false)

                SpendLessTheme {
                    Navigation(
                        isUserLoggedIn = result != null,
                        isLaunchedFromWidget = isLaunchedFromWidget,
                    )
                }
            }

            isReady = true
        }
    }
}