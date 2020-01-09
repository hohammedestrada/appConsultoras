package biz.belcorp.consultoras.common.recordatory;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.feature.history.DebtPaymentHistoryActivity;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.log.BelcorpLogger;

public class RecordatoryJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters job) {

        BelcorpLogger.i("RecordatoryJobService", "onStartJob");

        Bundle extras = job.getExtras();

        if (extras != null) {
            String message = extras.getString(GlobalConstant.CLIENT_MESSAGE_RECORDATORY, "");
            Integer clienteId = extras.getInt(GlobalConstant.CLIENTE_ID, -1);
            Integer clienteLocalID = extras.getInt(GlobalConstant.CLIENTE_LOCAL_ID, -1);
            String clientName = extras.getString(GlobalConstant.CLIENT_NAME, "");
            String totalDebt = extras.getString(GlobalConstant.CLIENT_TOTAL_DEBT, "0");

            Intent intent = new Intent(this, DebtPaymentHistoryActivity.class);
            intent.putExtra(GlobalConstant.FROM_REMINDER, true);
            intent.putExtra(GlobalConstant.CLIENTE_ID, clienteId);
            intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clienteLocalID);
            intent.putExtra(GlobalConstant.CLIENT_NAME, clientName);
            intent.putExtra(GlobalConstant.CLIENT_TOTAL_DEBT, totalDebt);

            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Recordatorio")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setContentText(message);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                builder.setColor(ContextCompat.getColor(this, R.color.primary));

            Notification notification = builder.build();
            mNotificationManager.notify(clienteLocalID, notification);
        }

        //

        new Thread(() -> completeJob(job)).start();

        return true;
    }

    public void completeJob(final JobParameters parameters) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            BelcorpLogger.w("completeJob", e);
            Thread.currentThread().interrupt();
        } finally {
            jobFinished(parameters, false);
        }
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        BelcorpLogger.i("RecordatoryJobService", "onStopJob");

        return false;
    }
}
