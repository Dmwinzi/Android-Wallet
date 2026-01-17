package com.example.wallet.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferenceManager(private val context: Context) {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val CUSTOMER_NAME = stringPreferencesKey("customer_name")
        val CUSTOMER_ID = stringPreferencesKey("customer_id")
        val ACCOUNT_NO = stringPreferencesKey("account_no")
        val EMAIL = stringPreferencesKey("email")
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { it[IS_LOGGED_IN] ?: false }

    val customerNameFlow: Flow<String> = context.dataStore.data
        .map { it[CUSTOMER_NAME] ?: "" }

    val accountNumberFlow: Flow<String> = context.dataStore.data
        .map { it[ACCOUNT_NO] ?: "" }

    val customerIdFlow: Flow<String> = context.dataStore.data
        .map { it[CUSTOMER_ID] ?: "" }

    suspend fun saveLoginDetails(
        name: String,
        id: String,
        email: String,
        accountNo: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
            prefs[CUSTOMER_NAME] = name
            prefs[CUSTOMER_ID] = id
            prefs[EMAIL] = email
            prefs[ACCOUNT_NO] = accountNo
        }
    }

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
    }
}