package biz.belcorp.consultoras.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import biz.belcorp.consultoras.BuildConfig

object OtherAppUtil {

    const val MQVIRTUAL_PACKAGE_NAME = BuildConfig.APP_MQVIRTUAL_PACKAGE
    const val URL_PLAY_STORE_RATE_WEB = "http://play.google.com/store/apps/details?id="
    const val URL_PLAY_STORE_RATE = "market://details?id="

    fun isMaquilladorInstalled(context: Context): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(MQVIRTUAL_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun openMaquilladorPlayStore(context: Context) {
        context.startActivity(getMaquilladorPlayStoreIntent())

    }

    fun getMaquilladorPlayStoreIntent() : Intent{
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try
        {
            intent.data = Uri.parse("market://details?id=$MQVIRTUAL_PACKAGE_NAME")
        } catch (anfe: android.content.ActivityNotFoundException) {
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$MQVIRTUAL_PACKAGE_NAME")
        }
        val chooserIntent = Intent.createChooser(intent, "Abrir con...")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return chooserIntent
    }

    fun openRatePlayStore(context: Context){
        val uri = Uri.parse(URL_PLAY_STORE_RATE + context.packageName.replace(".${BuildConfig.BUILD_TYPE}",""))
        val goToMarket =  Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(goToMarket)
        } catch (e : ActivityNotFoundException) {
            context.startActivity( Intent(Intent.ACTION_VIEW,
                Uri.parse(URL_PLAY_STORE_RATE_WEB + context.packageName.replace(".${BuildConfig.BUILD_TYPE}",""))))
        }
    }

}
