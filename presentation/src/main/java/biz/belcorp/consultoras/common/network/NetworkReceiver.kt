package biz.belcorp.consultoras.common.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

import org.greenrobot.eventbus.EventBus

import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.util.anotation.NetworkEventType

class NetworkReceiver : BroadcastReceiver() {

    var networkListener : NetworkListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        isNetworkAvailable(context)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (null == connectivity) {
            networkListener?.onChange(NetworkEventType.CONNECTION_NOT_AVAILABLE)
            EventBus.getDefault().post(NetworkEvent(NetworkEventType.CONNECTION_NOT_AVAILABLE))
            return false
        }
        val activeNetwork = connectivity.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isAvailable && activeNetwork.isConnected
                && activeNetwork.detailedState == NetworkInfo.DetailedState.CONNECTED) {

            val sessionManager = SessionManager.getInstance(context)
            if (sessionManager.isMultiOrder() == true) {
                networkListener?.onChange(NetworkEventType.MULTI_ORDER_AVAILABLE)
                EventBus.getDefault().post(NetworkEvent(NetworkEventType.MULTI_ORDER_AVAILABLE))
            } else {
                networkListener?.onChange(NetworkEventType.CONNECTION_AVAILABLE)
                EventBus.getDefault().post(NetworkEvent(NetworkEventType.CONNECTION_AVAILABLE))
            }
            return true
        } else {
            networkListener?.onChange(NetworkEventType.CONNECTION_NOT_AVAILABLE)
            EventBus.getDefault().post(NetworkEvent(NetworkEventType.CONNECTION_NOT_AVAILABLE))
            return false
        }
    }

    interface NetworkListener {
        fun onChange(result : Int)
    }
}
