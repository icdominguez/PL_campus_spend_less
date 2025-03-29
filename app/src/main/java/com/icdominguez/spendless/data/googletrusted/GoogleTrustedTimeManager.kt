package com.icdominguez.spendless.data.googletrusted

import android.content.Context
import com.google.android.gms.time.TrustedTime
import com.google.android.gms.time.TrustedTimeClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GoogleTrustedTimeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var _trustedTimeClient: TrustedTimeClient? = null

    suspend fun initialize() {
        if (_trustedTimeClient == null) {
            _trustedTimeClient = createClient()
        }
    }

    fun getClient(): TrustedTimeClient {
        return _trustedTimeClient ?: throw IllegalStateException("TrustedTimeClient not initialized")
    }

    private suspend fun createClient(): TrustedTimeClient {
        return suspendCoroutine { continuation ->
            TrustedTime.createClient(context).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error"))
                }
            }
        }
    }
}