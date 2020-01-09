package biz.belcorp.consultoras.common.fcm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.log.BelcorpLogger
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.lang.Exception

class RefreshReceiver(val context: Context) : BroadcastReceiver() {

    var remoteConfig = FirebaseRemoteConfig.getInstance()!!


    override fun onReceive(context: Context, intent: Intent) {
        try {
            BelcorpLogger.i("RefreshReceiver => Update")

            fetchConfig(context)
        } catch (e: Exception) {
            BelcorpLogger.d("Error => ${e.message}" )
        }
    }

    fun fetchConfig(context: Context) {
        val isDeveloperMode = remoteConfig.info.configSettings.isDeveloperModeEnabled
        val cacheExpiration: Long = if (isDeveloperMode) 0 else 3600

        remoteConfig.fetch(cacheExpiration).addOnCompleteListener {
            if (it.isSuccessful)  {
                //Toast.makeText(this, "Fetch Succeeded", Toast.LENGTH_SHORT).show()
                remoteConfig.activateFetched()

                val configSettings = FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build()
                remoteConfig = FirebaseRemoteConfig.getInstance()
                remoteConfig.setConfigSettings(configSettings)
                remoteConfig.setDefaults(R.xml.remote_config_defaults)

                fetchRemoteConfig(context)

            } else {
                //Toast.makeText(this, "Fetch Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRemoteConfig(context: Context) {
        var cacheExpiration: Long = 3600

        RestApi.clearCache(context)

        if (remoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        remoteConfig.fetch(cacheExpiration).addOnCompleteListener {
            if (it.isSuccessful) {
                remoteConfig.activateFetched()
            }

            val sessionManager = SessionManager.getInstance(context)
            sessionManager.saveApiCacheEnabled(remoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_API_CACHE_ENABLED))
            sessionManager.saveApiCacheOnlineTime(remoteConfig.getLong(BuildConfig.REMOTE_CONFIG_API_CACHE_ONLINE_TIME))
            sessionManager.saveApiCacheOfflineTime(remoteConfig.getLong(BuildConfig.REMOTE_CONFIG_API_CACHE_OFFLINE_TIME))
            sessionManager.saveHideViewsGridGanaMas(remoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_HIDE_VIEWS_GRID_GANA_MAS))
            sessionManager.saveExpandedSearchview(remoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_EXPANDED_SEARCHVIEW))

        }
    }


}
