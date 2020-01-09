package biz.belcorp.consultoras.common.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sap.cec.marketing.ymkt.mobile.configuration.Boot;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.fcm.di.DaggerFbMessagingComponent;
import biz.belcorp.consultoras.common.fcm.di.FbMessagingComponent;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ServiceModule;
import biz.belcorp.consultoras.domain.entity.Notificacion;
import biz.belcorp.consultoras.feature.notifications.list.FBMessagingPresenter;
import biz.belcorp.consultoras.feature.notifications.list.FBMessagingView;
import biz.belcorp.consultoras.feature.notifications.redirect.NotificationsActivity;
import biz.belcorp.consultoras.util.Constant;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.FromOpenActivityType;
import biz.belcorp.consultoras.util.anotation.NotificationCode;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.StringUtil;

public class FBMessagingService extends FirebaseMessagingService implements FBMessagingView {

    private static final String TAG = "FBMessagingService";

    private FbMessagingComponent component;
    private Notificacion notificacion;

    @Inject
    FBMessagingPresenter presenter;


    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    protected void initializeInjector() {
        this.component = DaggerFbMessagingComponent.builder()
            .appComponent(getAppComponent())
            .serviceModule(new ServiceModule(this))
            .build();
        component.inject(this);
        presenter.attachView(this);
    }

    protected AppComponent getAppComponent() {
        return ((ConsultorasApp) getApplication()).getAppComponent();
    }

    protected ServiceModule getServiceModule() {
        return new ServiceModule(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = "";
        String message = "";
        String trackingURL = "";

        if (null != remoteMessage.getNotification()) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        }

        String codeNotification = NotificationCode.HOME;

        try {
            JSONObject jsonObject = new JSONObject(message);
            codeNotification = jsonObject.optString("code");
            message = jsonObject.optString("message");
            title = jsonObject.optString("title");
        } catch (JSONException e) {
            BelcorpLogger.d(TAG, "Malformed JSON: \"" + message + "\"");
        }

        if (null == title || title.isEmpty()) {
            title = Constant.BRAND_FOCUS_NAME + " Conmigo";
        }

        Map data = remoteMessage.getData();
        if (data != null) trackingURL = (String) data.get("trackingURL");

        String format = message;
        int emoji = 0;
        if (null != message && !message.isEmpty()) {
            if (message.contains(NotificationCode.ORDER_EMOJI)) {
                emoji = 0x1F4DD;
                format = message.replace(NotificationCode.ORDER_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ORDER;
            } else if (message.contains(NotificationCode.ORDER_TWO_EMOJI)) {
                emoji = 0x1F614;
                format = message.replace(NotificationCode.ORDER_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ORDER;
            } else if (message.contains(NotificationCode.ORDER_THREE_EMOJI)) {
                emoji = 0x23F0;
                format = message.replace(NotificationCode.ORDER_THREE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ORDER;
            } /* else if (message.contains(NotificationCode.HOME_ANIM_50_EMOJI)) {
                emoji = 0x1F3C6;
                format = message.replace(NotificationCode.HOME_ANIM_50_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.HOME_ANIM_50;
            } */ else if (message.contains(NotificationCode.OFFERS_EMOJI)) {
                emoji = 0x1F389;
                format = message.replace(NotificationCode.OFFERS_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.OFFERS_TWO_EMOJI)) {
                emoji = 0x1F4B0;
                format = message.replace(NotificationCode.OFFERS_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.OFFERS_SHOWROOM_EMOJI)) {
                emoji = 0x1F6A8;
                format = message.replace(NotificationCode.OFFERS_SHOWROOM_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_SHOWROOM;
            } else if (message.contains(NotificationCode.OFFERS_SHOWROOM_TWO_EMOJI)) {
                emoji = 0x1F446;
                format = message.replace(NotificationCode.OFFERS_SHOWROOM_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_SHOWROOM;
            } else if (message.contains(NotificationCode.OFFERS_NEW_NEW_EMOJI)) {
                emoji = 0x2B50;
                format = message.replace(NotificationCode.OFFERS_NEW_NEW_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_NEW_NEW;
            } else if (message.contains(NotificationCode.OFFERS_FOR_YOU_EMOJI)) {
                emoji = 0x1F51D;
                format = message.replace(NotificationCode.OFFERS_FOR_YOU_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_FOR_YOU;
            } else if (message.contains(NotificationCode.OFFERS_ONLY_TODAY_EMOJI)) {
                emoji = 0x1F6A8;
                format = message.replace(NotificationCode.OFFERS_ONLY_TODAY_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_ONLY_TODAY;
            } else if (message.contains(NotificationCode.OFFERS_SALES_TOOLS_EMOJI)) {
                emoji = 0x1F4E3;
                format = message.replace(NotificationCode.OFFERS_SALES_TOOLS_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_SALES_TOOLS;
            } else if (message.contains(NotificationCode.OFFERS_INFORMATION_EMOJI)) {
                emoji = 0x1F64C;
                format = message.replace(NotificationCode.OFFERS_INFORMATION_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_INFORMATION;
            } else if (message.contains(NotificationCode.OFFERS_THREE_EMOJI)) {
                emoji = 0x1F64B;
                format = message.replace(NotificationCode.OFFERS_THREE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.OFFERS_FOUR_EMOJI)) {
                emoji = 0x1F382;
                format = message.replace(NotificationCode.OFFERS_FOUR_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.OFFERS_FIVE_EMOJI)) {
                emoji = 0x1F385;
                format = message.replace(NotificationCode.OFFERS_FIVE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.OFFERS_SIX_EMOJI)) {
                emoji = 0x1F384;
                format = message.replace(NotificationCode.OFFERS_SIX_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.OFFERS_SEVEN_EMOJI)) {
                emoji = 0x1F339;
                format = message.replace(NotificationCode.OFFERS_SEVEN_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.OFFERS_EIGHT_EMOJI)) {
                emoji = 0x1F451;
                format = message.replace(NotificationCode.OFFERS_EIGHT_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.OFFERS_NINE_EMOJI)) {
                emoji = 0x1F490;
                format = message.replace(NotificationCode.OFFERS_NINE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS;
            } else if (message.contains(NotificationCode.ACCOUNT_EMOJI)) {
                emoji = 0x1F4B5;
                format = message.replace(NotificationCode.ACCOUNT_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ACCOUNT;
            } else if (message.contains(NotificationCode.ACCOUNT_TWO_EMOJI)) {
                emoji = 0x1F4C6;
                format = message.replace(NotificationCode.ACCOUNT_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ACCOUNT;
            } else if (message.contains(NotificationCode.CATALOG_EMOJI)) {
                emoji = 0x1F4D5;
                format = message.replace(NotificationCode.CATALOG_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CATALOG;
            } else if (message.contains(NotificationCode.CATALOG_TWO_EMOJI)) {
                emoji = 0x261D;
                format = message.replace(NotificationCode.CATALOG_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CATALOG;
            } else if (message.contains(NotificationCode.ANNOUNCEMENT_EMOJI)) {
                emoji = 0x1F60B;
                format = message.replace(NotificationCode.ANNOUNCEMENT_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ANNOUNCEMENT;
            } else if (message.contains(NotificationCode.ACADEMY_EMOJI)) {
                emoji = 0x1F4A1;
                format = message.replace(NotificationCode.ACADEMY_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.MY_ACADEMY;
            } else if (message.contains(NotificationCode.ACADEMY_TWO_EMOJI)) {
                emoji = 0x1F600;
                format = message.replace(NotificationCode.ACADEMY_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
            } else if (message.contains(NotificationCode.ACADEMY_HOME_EMOJI)) {
                emoji = 0x1F4D6;
                format = message.replace(NotificationCode.ACADEMY_HOME_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.MY_ACADEMY;
            } else if (message.contains(NotificationCode.MY_PROFILE_ONE_EMOJI)) {
                emoji = 0x1F4E7;
                format = message.replace(NotificationCode.MY_PROFILE_ONE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.MY_PROFILE;
            } else if (message.contains(NotificationCode.MY_PROFILE_TWO_EMOJI)) {
                emoji = 0x1F4F1;
                format = message.replace(NotificationCode.MY_PROFILE_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.MY_PROFILE;
            } else if (message.contains(NotificationCode.MY_PROFILE_THREE_EMOJI)) {
                emoji = 0x1F478;
                format = message.replace(NotificationCode.MY_PROFILE_THREE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.MY_PROFILE;
            } else if (message.contains(NotificationCode.MY_PROFILE_FOUR_EMOJI)) {
                emoji = 0x1F471;
                format = message.replace(NotificationCode.MY_PROFILE_FOUR_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.MY_PROFILE;
            } else if (message.contains(NotificationCode.STOCKOUTS_EMOJI)) {
                emoji = 0x1F610;
                format = message.replace(NotificationCode.STOCKOUTS_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.STOCKOUTS;
            } else if (message.contains(NotificationCode.CHANGE_EMOJI)) {
                emoji = 0x1F514;
                format = message.replace(NotificationCode.CHANGE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CHANGE;
            } else if (message.contains(NotificationCode.ORDERS_FIC_EMOJI)) {
                emoji = 0x1F440;
                format = message.replace(NotificationCode.ORDERS_FIC_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ORDERS_FIC;
            } else if (message.contains(NotificationCode.MY_ORDER_ONE_EMOJI)) {
                emoji = 0x1F449;
                format = message.replace(NotificationCode.MY_ORDER_ONE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.MY_ORDER;
            } else if (message.contains(NotificationCode.MY_ORDER_TWO_EMOJI)) {
                emoji = 0x1F4E6;
                format = message.replace(NotificationCode.MY_ORDER_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.MY_ORDER;
            } else if (message.contains(NotificationCode.CLOSE_OUT_EMOJI)) {
                emoji = 0x23F3;
                format = message.replace(NotificationCode.CLOSE_OUT_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CLOSE_OUT;
            } else if (message.contains(NotificationCode.HOME_INCENTIVES_EMOJI)) {
                emoji = 0x1F381;
                format = message.replace(NotificationCode.HOME_INCENTIVES_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.HOME;
            } else if (message.contains(NotificationCode.HOME_INCENTIVES_TWO_EMOJI)) {
                emoji = 0x1F60D;
                format = message.replace(NotificationCode.HOME_INCENTIVES_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.HOME;
            } else if (message.contains(NotificationCode.HOME_INCENTIVES_THREE_EMOJI)) {
                emoji = 0x1F31F;
                format = message.replace(NotificationCode.HOME_INCENTIVES_THREE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.HOME;
            } else if (message.contains(NotificationCode.HOME_DEBTS_EMOJI)) {
                emoji = 0x1F45B;
                format = message.replace(NotificationCode.HOME_DEBTS_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.HOME;
            } else if (message.contains(NotificationCode.HOME_DEBTS_TWO_EMOJI)) {
                emoji = 0x1F44F;
                format = message.replace(NotificationCode.HOME_DEBTS_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.HOME;
            } else if (message.contains(NotificationCode.HOME_CLIENTS_EMOJI)) {
                emoji = 0x1F469;
                format = message.replace(NotificationCode.HOME_CLIENTS_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.HOME;
            } else if (message.contains(NotificationCode.HOME_CLIENTS_TWO_EMOJI)) {
                emoji = 0x1F468;
                format = message.replace(NotificationCode.HOME_CLIENTS_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.HOME;
            } else if (message.contains(NotificationCode.PAYONLINE_EMOJI)) {
                emoji = 0x1F4B3;
                format = message.replace(NotificationCode.PAYONLINE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ORDER;
            } else if (message.contains(NotificationCode.PAYONLINE_TWO_EMOJI)) {
                emoji = 0x1F38A;
                format = message.replace(NotificationCode.PAYONLINE_TWO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ORDER;
            } else if (message.contains(NotificationCode.ORDER_SCALE_EMOJI)) {
                emoji = 0x1F4AA;
                format = message.replace(NotificationCode.ORDER_SCALE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ORDER;
            } else if (message.contains(NotificationCode.THE_MOST_WINNING_EMOJI)) {
                emoji = 0x1F4B8;
                format = message.replace(NotificationCode.THE_MOST_WINNING_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_THE_MOST_WINNING;
            } else if (message.contains(NotificationCode.PERFECT_DUO_EMOJI)) {
                emoji = 0x1F49D;
                format = message.replace(NotificationCode.PERFECT_DUO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.OFFERS_PERFECT_DUO;
            }/*else if (message.contains(NotificationCode.DEFAULT_EMOJI)) {
                emoji = 0x1F646;
                format = message.replace(NotificationCode.DEFAULT_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.DEFAULT_EMOJI;
            }*/ else if (message.contains(NotificationCode.CAMINO_BRILLANTE_LANDING_EMOJI)) {
                emoji = 0x2728;
                format = message.replace(NotificationCode.CAMINO_BRILLANTE_LANDING_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CAMINO_BRILLANTE_LANDING;
            } else if (message.contains(NotificationCode.CAMINO_BRILLANTE_OFERTAS_EMOJI)) {
                emoji = 0x1F680;
                format = message.replace(NotificationCode.CAMINO_BRILLANTE_OFERTAS_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CAMINO_BRILLANTE_OFERTAS_ESPECIALES;
            } else if (message.contains(NotificationCode.CAMINO_BRILLANTE_MEDALLAS_EMOJI)) {
                emoji = 0x1F3C6;
                format = message.replace(NotificationCode.CAMINO_BRILLANTE_MEDALLAS_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CAMINO_BRILLANTE_MEDALLAS;
            } else if (message.contains(NotificationCode.ASESOR_DE_BELLEZA_EMOJI)) {
                emoji = 0x1F484;
                format = message.replace(NotificationCode.ASESOR_DE_BELLEZA_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ASESOR_DE_BELLEZA;
            } else if (message.contains(NotificationCode.ARMA_TU_PACK_EMOJI)) {
                emoji = 0x1F4A0;
                format = message.replace(NotificationCode.ARMA_TU_PACK_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ARMA_TU_PACK;
            } else if (message.contains(NotificationCode.TU_VOZ_ONLINE_EMOJI)){
                emoji = 0x1F4F2;
                format = message.replace(NotificationCode.TU_VOZ_ONLINE_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.TU_VOZ_ONLINE;
            } else if (message.contains(NotificationCode.CALENDARIO_EMOJI)){
                emoji = 0x1F4C5;
                format = message.replace(NotificationCode.CALENDARIO_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CALENDARIO_FACTURACION;
            } else if (message.contains(NotificationCode.CONFERENCIA_DIGITAL_EMOJI)){
                emoji = 0x1F4F9;
                format = message.replace(NotificationCode.CONFERENCIA_DIGITAL_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.CONFERENCIA_DIGITAL;
            } else if (message.contains(NotificationCode.DREAM_METER_LANDING_EMOJI)) {
                emoji = 0x1F320;
                format = message.replace(NotificationCode.DREAM_METER_LANDING_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.DREAM_METER_LANDING;
            } else if (message.contains(NotificationCode.FEST_LANDING_EMOJI)) {
                emoji = 0x1F386;
                format = message.replace(NotificationCode.FEST_LANDING_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.FEST_LANDING;
            } else if (message.contains(NotificationCode.SUB_CAMPAIGN_EMOJI)) {
                emoji = 0x1F4AB;
                format = message.replace(NotificationCode.SUB_CAMPAIGN_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.SUB_CAMPAIGN_LANDING;
            } else if (message.contains(NotificationCode.ESIKA_AHORA_EMOJI)){
                emoji = 0x1F4D1;
                format = message.replace(NotificationCode.ESIKA_AHORA_EMOJI, StringUtil.getEmojiByUnicode(emoji));
                codeNotification = NotificationCode.ESIKA_AHORA;
            }
        }

        int id = (int) System.currentTimeMillis();

        if (remoteMessage.getData() != null) {
            assert data != null;
            if (!data.containsKey("update_time")) {
                Intent intent = new Intent(this, NotificationsActivity.class);
                intent.setAction(Long.toString(System.currentTimeMillis()));
                intent.putExtra("notification_code", codeNotification);
                intent.putExtra("notification_id", id);
                intent.putExtra(GlobalConstant.TRACKING_URL, trackingURL);
                intent.putExtra(GlobalConstant.FROM_OPEN_ACTIVITY, FromOpenActivityType.NOTIFICATION);

                // Create the TaskStackBuilder and addFromHome the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(intent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder builder = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher))
                    .setStyle(new Notification.BigTextStyle().bigText(format))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContentText(format);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                    builder.setColor(ContextCompat.getColor(this, R.color.primary));


                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (null != manager) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        NotificationChannel channel = new NotificationChannel(BuildConfig.FLAVOR, BuildConfig.APP_NAME, NotificationManager.IMPORTANCE_HIGH);
                        manager.createNotificationChannel(channel);
                        builder.setChannelId(channel.getId());
                    }
                    Notification build = builder.build();
                    manager.notify(id, build);
                }

                Notificacion notificacion = new Notificacion();
                notificacion.setEstado(0);
                notificacion.setDescripcion(format.trim());
                notificacion.setFecha(new Date());
                notificacion.setCodigo(codeNotification);
                notificacion.setEmoji(emoji);
                notificacion.setNotificationId(id);

                presenter.saveNotificacion(notificacion, true);


            }
        }

        if (remoteMessage.getData() != null) {
            assert data != null;
            if (!data.containsKey("update_time")) return;
            sendBroadcast(new Intent(GlobalConstant.REFRESH_RC));
        }

    }

    @Override
    public void handleIntent(Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            if (null != extras) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("TAG");
                for (String key : extras.keySet()) {
                    Object obj = extras.get(key);
                    if (null != obj)
                        builder.addData(key, obj.toString());

                    if (key.equals(GlobalConstant.TRACKING_URL))
                        initHybris();
                }
                onMessageReceived(builder.build());
            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }

    private void initHybris() {
        try {
            Boot.setupHybrisMarketing(HybrisManager.getInstance().getConfiguration(getApplicationContext()));
        } catch (Exception e) {
            BelcorpLogger.d(TAG, "ERROR", e);
        }
    }


    @Override
    public void initScreenTrack(@NotNull LoginModel model) {

    }

    @Override
    public void trackBackPressed(@NotNull LoginModel model) {

    }

    @Override
    public void onNotificacionSaved(boolean t) {
        if (t) {
            BelcorpLogger.i("Notificación guardada en la BD");
            EventBus.getDefault().post(new FBEventBusEntity());
        } else {
            BelcorpLogger.i("Notificación no guardada en la BD");
        }
    }

    @Override
    public Context context() {
        return context();
    }

    @Override
    public void onVersionError(boolean required, String url) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

}
