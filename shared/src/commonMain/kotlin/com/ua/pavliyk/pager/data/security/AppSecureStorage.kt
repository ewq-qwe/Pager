package com.ua.pavliyk.pager.data.security

import eu.anifantakis.lib.ksafe.KSafe

internal class AppSecureStorage(
    private val ksafe: KSafe,
) : SecureStorage {

    override suspend fun getAccessToken(): String = ksafe.get(ACCESS_TOKEN_KEY, "")

    override suspend fun setAccessToken(token: String) {
        ksafe.put(ACCESS_TOKEN_KEY, token)
    }

    override suspend fun getRefreshToken(): String = ksafe.get(REFRESH_TOKEN_KEY, "")

    override suspend fun setRefreshToken(token: String) {
        ksafe.put(REFRESH_TOKEN_KEY, token)
    }

    override suspend fun clearTokens() {
        ksafe.delete(ACCESS_TOKEN_KEY)
        ksafe.delete(REFRESH_TOKEN_KEY)
    }
}
