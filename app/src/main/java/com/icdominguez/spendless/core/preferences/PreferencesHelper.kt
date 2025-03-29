package com.icdominguez.spendless.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.icdominguez.spendless.core.Commons.PREFERENCES_FILE
import com.icdominguez.spendless.core.EncryptionUtil
import java.security.SecureRandom

object PreferencesHelper {
    private const val KEY_ENCRYPTED_PASSPHRASE: String = "encrypted_passphrase"

    fun getPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFERENCES_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    private fun savePassphrase(context: Context) {
        val prefs = getPrefs(context)
        if (!prefs.contains(KEY_ENCRYPTED_PASSPHRASE)) {
            val newPassphrase = generateRandomPassphrase()
            val encryptedPassphrase = EncryptionUtil.encryptData(context, newPassphrase)

            prefs.edit()
                .putString(KEY_ENCRYPTED_PASSPHRASE, encryptedPassphrase)
                .apply()
        }
    }

    private fun generateRandomPassphrase(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun getDecryptedPassphrase(context: Context): ByteArray {
        val prefs = getPrefs(context)
        val encryptedPassphrase = prefs.getString(KEY_ENCRYPTED_PASSPHRASE, null)

        return if (encryptedPassphrase != null) {
            EncryptionUtil.decryptData(context, encryptedPassphrase).toByteArray()
        } else {
            savePassphrase(context)
            getDecryptedPassphrase(context)
        }
    }
}