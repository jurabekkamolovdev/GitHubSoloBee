package com.javohir.data.local.sharedPref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.local.sharedPref
 * Description: local data's
 */
class SharedPreference @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthTokenStore {

    companion object{
        private const val APP_PREFS = "app_prefs"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_ID = "user_id"
        private const val ONBOARDING_COMPLETED = "onboarding_completed"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)


  override suspend fun saveAccessToken(accessToken: String){
       withContext(Dispatchers.IO) {
           prefs.edit { putString(ACCESS_TOKEN, accessToken) }
       }
   }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        withContext(Dispatchers.IO) {
            prefs.edit(commit = true) {
                putString(ACCESS_TOKEN, accessToken)
                putString(REFRESH_TOKEN, refreshToken)
            }
        }
    }

   override suspend fun getAccessToken(): String?{
        return withContext(Dispatchers.IO) {
            prefs.getString(ACCESS_TOKEN,null)
        }
    }

  override suspend fun saveRefreshToken(refreshToken: String){
       withContext(Dispatchers.IO) {
           prefs.edit { putString(REFRESH_TOKEN,refreshToken) }
       }
   }

    override suspend fun getRefreshToken(): String?{
        return withContext(Dispatchers.IO){
            prefs.getString(REFRESH_TOKEN,null)
        }
    }

    suspend fun getUserId(): String?{
        return withContext(Dispatchers.IO){
            prefs.getString(USER_ID,null)
        }
    }

    suspend fun markOnboardingCompleted() {
        withContext(Dispatchers.IO) {
            prefs.edit { putBoolean(ONBOARDING_COMPLETED, true) }
        }
    }

    suspend fun hasCompletedOnboarding(): Boolean {
        return withContext(Dispatchers.IO) {
            prefs.getBoolean(ONBOARDING_COMPLETED, false)
        }
    }

    override suspend fun clearSession(){
      withContext(Dispatchers.IO){
        prefs.edit {
            remove(ACCESS_TOKEN)
            remove(REFRESH_TOKEN)
        }
      }
    }

    suspend fun resetAuthState() {
        withContext(Dispatchers.IO) {
            prefs.edit {
                remove(ACCESS_TOKEN)
                remove(REFRESH_TOKEN)
                remove(USER_ID)
                remove(ONBOARDING_COMPLETED)
            }
        }
    }
}