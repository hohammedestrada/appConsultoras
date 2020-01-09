package biz.belcorp.consultoras;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.instacart.library.truetime.TrueTime;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.IOException;

import biz.belcorp.consultoras.common.network.NetworkReceiver;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.component.DaggerAppComponent;
import biz.belcorp.consultoras.di.module.AppModule;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.analytics.BelcorpAnalyticsConfig;
import biz.belcorp.library.exception.BelcorpCrash;
import biz.belcorp.library.exception.BelcorpCrashConfig;
import biz.belcorp.library.log.BelcorpLogConfig;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.mobile.analytics.core.Analytics;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class ConsultorasApp extends MultiDexApplication implements NetworkReceiver.NetworkListener {

    private static final String TAG = "ConsultorasApp";
    private AppComponent appComponent;
    private int datamiType = NetworkEventType.DATAMI_NOT_AVAILABLE;

    private static ConsultorasApp instance = null;

    /**********************************************************/

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null)
            instance = this;

        init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static ConsultorasApp getInstance() {
        return instance;
    }

    /**********************************************************/

    private void init() {
        initLog();
        initCrash();
        initializeInjector();
        initDB();
        initNetworkInspector();
        initFonts();
        initAnalytics();
        initChangeNetwork();
        initBelcorpAnalytics();
        initTrueTime();
    }

    private void initChangeNetwork() {
            NetworkReceiver networkReceiver = new NetworkReceiver();
            networkReceiver.setNetworkListener(this);
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void initLog() {
        BelcorpLogger.config(BelcorpLogConfig
            .newBuilder()
            .tag("APP_CONSULTORA")
            .header(true)
            .params(true)
            .saveDisk(this, true, false)
            .build());
    }

    private void initCrash() {
        if(BuildConfig.DEBUG) {
            BelcorpCrash.config(BelcorpCrashConfig
                .newBuilder()
                .showSendButton(false,
                    "app.consultoras@gmail.com"
                )
                .trackActivities(true)
                .build());
        } else {
            BelcorpCrash.config(BelcorpCrashConfig
                .newBuilder()
                .showSendButton(false,
                    "sdigitalconsultoras@gmail.com"
                )
                .trackActivities(true)
                .build());
        }

        BelcorpCrash.install(this);
    }

    private void initializeInjector() {
        this.appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();
    }

    private void initDB() {
        FlowManager
            .init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true)
                .build());
    }

    private void initNetworkInspector() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    private void initTrueTime(){
        // initialize also must be run on a background thread. If you run it on the main thread, you will getWithObservable a NetworkOnMainThreadException
        AsyncTask.execute(() -> {
            try{
                TrueTime.build().initialize();
            }catch (IOException e){
                BelcorpLogger.w("TrueTime", e.getMessage());
            }
        });
    }

    /**********************************************************/

    public AppComponent getAppComponent() {
        return this.appComponent;
    }

    /**********************************************************/

    private void initFonts() {

        ViewPump.init(ViewPump.builder()
            .addInterceptor(new CalligraphyInterceptor(
                new CalligraphyConfig.Builder()
                    .setDefaultFontPath(GlobalConstant.LATO_BOLD_SOURCE)
                    .setFontAttrId(R.attr.fontPath)
                    .build()))
            .build());

        ViewPump.init(ViewPump.builder()
            .addInterceptor(new CalligraphyInterceptor(
                new CalligraphyConfig.Builder()
                    .setDefaultFontPath(GlobalConstant.LATO_LIGHT_SOURCE)
                    .setFontAttrId(R.attr.fontPath)
                    .build()))
            .build());

        ViewPump.init(ViewPump.builder()
            .addInterceptor(new CalligraphyInterceptor(
                new CalligraphyConfig.Builder()
                    .setDefaultFontPath(GlobalConstant.LATO_REGULAR_SOURCE)
                    .setFontAttrId(R.attr.fontPath)
                    .build()))
            .build());

    }

    private void initAnalytics() {
        BelcorpAnalytics.config(BelcorpAnalyticsConfig
            .newBuilder()
            .containerID("GTM-NBJMJ3C")
            .isFirebase(true)
            .save(true)
            .build());

        BelcorpAnalytics.install(this);
    }

    private void initBelcorpAnalytics(){
        Analytics.init(this);
    }

    public int getDatamiType() {
        return datamiType;
    }

    public void setDatamiType(int datamiType) {
        this.datamiType = datamiType;
    }

    @Override
    public void onChange(int result) {
        datamiType = result;
    }
}
