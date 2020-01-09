package biz.belcorp.consultoras.common.sync;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import biz.belcorp.consultoras.sync.SyncUtil;
import biz.belcorp.library.log.BelcorpLogger;

public class SyncReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        BelcorpLogger.d("SyncReceiver onReceive ","");

        Bundle extras = intent.getExtras();
        if (null != extras) {

            String status = extras.getString(SyncUtil.STATUS, SyncUtil.STOPPING);

            BelcorpLogger.d("SyncReceiver onReceive  - " + status ,"");
            switch (status){
                case SyncUtil.RUNNING:
                    EventBus.getDefault().post(new SyncEvent(true));
                    break;
                case SyncUtil.STOPPING:
                    EventBus.getDefault().post(new SyncEvent(false));
                    break;
                default:
                    break;
            }

        }
    }

}
