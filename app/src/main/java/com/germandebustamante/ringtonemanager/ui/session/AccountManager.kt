package com.germandebustamante.ringtonemanager.ui.session

import android.content.Context
import android.util.Log
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import com.germandebustamante.ringtonemanager.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.security.MessageDigest
import java.util.UUID

class AccountManager(
    private val context: Context,
) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun googleSignIn(onGoogleIdTokenReceived: (String) -> Unit) {
        try {
            val credentialOptions = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId((context.getString(R.string.default_web_client_id)))
                .setNonce(getHashedNonce())
                .build()

            val credentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(credentialOptions)
                .build()

            val result = credentialManager.getCredential(context, credentialRequest)
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(result.credential.data)

            onGoogleIdTokenReceived(googleIdTokenCredential.idToken)
        } catch (exception: Exception) {
            Log.e(TAG, "Error signing up", exception)
        }
    }

    suspend fun saveCredentials(email: String, password: String, onProcessFinished: () -> Unit) {
        try {
            credentialManager.createCredential(
                context = context,
                request = CreatePasswordRequest(
                    id = email,
                    password = password,
                )
            )
        } catch (exception: Exception) {
            Log.e(TAG, "Error signing up", exception)
        } finally {
            onProcessFinished()
        }
    }

    suspend fun getCredentials(
        onCredentialSignInSuccess: (credentialId: String, credentialPassword: String) -> Unit,
    ) {
        try {
            val credentialResponse = credentialManager.getCredential(
                context = context,
                request = GetCredentialRequest(
                    credentialOptions = listOf(GetPasswordOption())
                )
            )

            val credentialId = (credentialResponse.credential as? PasswordCredential)?.id
            val credentialPassword = (credentialResponse.credential as? PasswordCredential)?.password

            if (credentialId != null && credentialPassword != null) {
                onCredentialSignInSuccess(credentialId, credentialPassword)
            }
        } catch (exception: Exception) {
            logError("Error signing in", exception)
        }
    }


    private fun getHashedNonce(): String {
        val rawNonce = UUID.randomUUID().toString().toByteArray()
        val messageDigest = MessageDigest.getInstance(SHA_256)
        val nonceHash = messageDigest.digest(rawNonce)
        return nonceHash.fold("") { str, it -> str + HASHED_NONCE_FORMAT.format(it) }
    }

    private fun logError(message: String, exception: Throwable) {
        Log.e(TAG, message, exception)
    }

    companion object {
        private const val TAG = "SessionManager"
        private const val SHA_256 = "SHA-256"
        private const val HASHED_NONCE_FORMAT = "%02x"
    }
}