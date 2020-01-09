package biz.belcorp.consultoras.common.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import org.greenrobot.eventbus.EventBus


class SMSReceiver : BroadcastReceiver() {

    private val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    private val TAG = "SMSBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i(TAG, "Intent recieved: " + intent?.action)

        if (intent?.action === SMS_RECEIVED) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<Any>
                val messages = arrayOfNulls<SmsMessage>(pdus.size)
                for (i in pdus.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                }
                if (messages.size > -1) {
                    val address = messages[0]?.originatingAddress
                    if (address == "ESIKA" || address == "LBEL"){
                        EventBus.getDefault().post(SMSEvent(messages[0]?.serviceCenterAddress, messages[0]?.messageBody))
                    }
                }
            }
        }
    }

}
