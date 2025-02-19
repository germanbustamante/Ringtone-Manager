package com.germandebustamante.ringtonemanager.ui.session

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.germandebustamante.ringtonemanager.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class AccountManager(
    private val context: Context,
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun googleSignIn(onGoogleIdTokenReceived: (String) -> Unit) {
        try {
            val credentialOptions = GetSignInWithGoogleOption.Builder(context.getString(R.string.default_web_client_id))
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

    companion object {
        private const val TAG = "SessionManager"
    }
}