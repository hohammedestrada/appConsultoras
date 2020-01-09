package biz.belcorp.consultoras.util.broadcast

import android.app.Activity
import android.content.Intent
import biz.belcorp.consultoras.feature.home.fest.FestActivity
import biz.belcorp.consultoras.util.anotation.FestPremioStateType

object FestBroadcast {

    fun sendAddUpdateToCart(activity: Activity) {
        val intent = Intent(FestActivity.BROADCAST_FEST_ACTION)
        intent.putExtra(FestActivity.BROADCAST_STATE_FEST_EXTRAS, FestPremioStateType.INSERT_UPDATE)
        activity.sendBroadcast(intent)
    }

    fun sendDeleteToCart(activity: Activity) {
        val intent = Intent(FestActivity.BROADCAST_FEST_ACTION)
        intent.putExtra(FestActivity.BROADCAST_STATE_FEST_EXTRAS, FestPremioStateType.DELETE)
        activity.sendBroadcast(intent)
    }
}
