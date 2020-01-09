package biz.belcorp.consultoras.feature.notifications.list

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.fcm.FBEventBusEntity
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.Notificacion
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.feature.notifications.di.NotificationsComponent
import biz.belcorp.consultoras.feature.notifications.redirect.NotificationsActivity
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.FromOpenActivityType
import kotlinx.android.synthetic.main.fragment_notification_list.*
import javax.inject.Inject
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



class NotificationListFragment: BaseFragment(), NotificationListView, NotificationListAdapter.NotificationListener {

    @Inject
    lateinit var presenter: NotificationListPresenter
    private lateinit var notificationListAdapter: NotificationListAdapter
    private var user: User? = null

    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_list, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(NotificationsComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    // BaseFragment Overrides
    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    // NotificationsView Overrides
    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_NOTIFICATION_LIST, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_NOTIFICATION_LIST, model)
    }

    override fun statusChanged(notificacion: Notificacion) {
        notificationListAdapter.changeNotification(notificacion)

        val intent = Intent( context, NotificationsActivity::class.java)
        intent.action = java.lang.Long.toString(System.currentTimeMillis())
        intent.putExtra(GlobalConstant.FROM_OPEN_ACTIVITY, FromOpenActivityType.ACTIVITY)
        intent.putExtra("notification_code", notificacion.codigo)
        context?.startActivity(intent)

    }

    // Notification Listener
    override fun onOpenNotification(item: Notificacion) {
        Tracker.Notificaciones.selectNotificacion()
        item.notificationId?.let {
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(it)
        }
        item.estado = 1
        presenter.changeNotificationStatus(item, false)
    }


    // Private Functions
    @SuppressLint("SetTextI18n")
    private fun init() {
        showLoading()
        remoteConfig()
    }

    private fun remoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build()
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setConfigSettings(configSettings)
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults)
        fetchRemoteConfig()
    }

    private var dias: Int = 0

    private fun fetchRemoteConfig() {
        var cacheExpiration: Long = 3600

        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(activity!!, { task ->

            if (task.isSuccessful()) {
                mFirebaseRemoteConfig.activateFetched()
            }

            dias = mFirebaseRemoteConfig.getString(BuildConfig.REMOTE_CONFIG_NOTIFICATION_DAYS).toInt()
            presenter.getUser()
        })
    }

    override fun setUser(user: User?) {
        this.user = user
        presenter.getNotificaciones(dias)
    }

    override fun setNotificaciones(t: List<Notificacion?>) {
        notificationListAdapter = NotificationListAdapter(this)
        rvwNotification.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvwNotification.adapter = notificationListAdapter

        if(t.isEmpty()){
            lnlEmpty.visibility = View.VISIBLE
        }else{
            lnlEmpty.visibility = View.GONE
            notificationListAdapter.setList(t)
        }

        hideLoading()
    }


    // Public Functions
    fun trackBackPressed() {
        presenter.trackBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FBEventBusEntity) {
        presenter.getNotificaciones(dias)
        presenter.updateNotificationStatus(false)
    }

}
