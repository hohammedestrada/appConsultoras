package biz.belcorp.consultoras.feature.splash

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.splash.di.SplashComponent
import biz.belcorp.consultoras.util.Constant
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.library.util.CountryUtil
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class SplashFragment : BaseFragment(), SplashService.Listener {

    @Inject
    internal lateinit var presenter: SplashPresenter

    private var countrySIM: String? = null
    private var listener: SplashFragmentListener? = null
    private var splashService: SplashService? = null
    var restartScreen = false

    var mBound = false
    private var mConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            // EMPTY
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as SplashService.LocalBinder
            splashService = binder.service
            mBound = true

            splashService?.presenter = presenter
            splashService?.listener = this@SplashFragment
            splashService?.activity = activity as BaseActivity
            presenter.getConfiguracionfestival()
            actionCheckVersion()
        }
    }

    internal interface SplashFragmentListener {
        fun onLogin()

        fun onFacebook()

        fun onTutorial()

        fun onHome()

        fun onHome(bundle: Bundle)

        fun onTerms(loginModel: LoginModel)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SplashFragmentListener) {
            this.listener = context
        }
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(SplashComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        countrySIM = CountryUtil.getCode(activity!!)
        if (countrySIM!!.isEmpty()) countrySIM = Constant.BRAND_COUNTRY_DEFAULT

        restartScreen = arguments?.getBoolean(GlobalConstant.RESTART_SCREEN)?.let { it } ?: false
        if(restartScreen){
            rltRestartScreen.visibility = View.VISIBLE
            fltSplash.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(activity, SplashService::class.java)
        activity?.startService(intent)
        activity?.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        // Unbind from the service
        if (mBound) {
            activity?.unbindService(mConnection)
            mBound = false
        }
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        splashService?.initScreenTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onSplashInitScreenTrack(model: User?) {
        Tracker.trackScreenUser(GlobalConstant.SCREEN_SPLASH, model)
    }

    fun actionCheckVersion(){
        if (!isAdded) return

        tvw_info.setText(R.string.splash_version)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val wrapDrawable = DrawableCompat.wrap(pgb_download.indeterminateDrawable)
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context!!, R.color.white))
            pgb_download.indeterminateDrawable = DrawableCompat.unwrap(wrapDrawable)
        }

        splashService?.checkVersion()
    }


    override fun onSplashShowMessageSubida() {
        if (!isAdded) return
        tvw_info.setText(R.string.splash_subida)
    }

    override fun onSplashShowMessageBackup() {
        if (!isAdded) return
        tvw_info.setText(R.string.splash_backup)
    }

    override fun onSplashShowMessageRestore() {
        if (!isAdded) return
        tvw_info.setText(R.string.splash_restore)
    }

    override fun onSplashFinished() {
        redirect()
    }

    override fun onSplashShowMessageData() {
        tvw_info.setText(R.string.splash_download)
    }

    override fun onError() {
        home(false)
    }

    override fun onSplashGoToHome(loginModel: LoginModel?) {
        if(restartScreen){
            loginModel?.let {
                showGanaMasConfetti(loginModel)
            }
        } else {
            listener?.onHome()
        }

    }

    override fun onSplashGoToTerms(loginModel: LoginModel) {
        listener?.onTerms(loginModel)
    }

    private fun redirect() {
        if (!isAdded) return

        splashService?.authModel?.let {
            if (!it.isLogged) {
                if (!it.isFacebook) {
                    login()
                } else {
                    facebook()
                }
            } else {
                home(false)
            }
        } ?: kotlin.run {
            login()
        }
    }

    fun login() {
        if (null != listener) {
            listener?.onLogin()
        }
    }

    fun facebook() {
        if (null != listener) {
            listener?.onFacebook()
        }
    }

    fun tutorial() {
        if (null != listener) {
            listener?.onTutorial()
        }
    }

    fun home(trackState: Boolean) {
        splashService?.getCurrentUser()
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    fun showGanaMasConfetti(loginModel: LoginModel) {
        fltSplash.visibility = View.GONE
        rltRestartScreen.visibility = View.GONE
        context?.let {
            FullScreenDialog.Builder(it)
                .withTitle("¡Felicidades "+ loginModel.primerNombre+"!")
                .withMessage("Ya estás suscrit@ a Gana+")
                .withIcon(R.drawable.ic_anima_por)
                .withAnimation(it.resources, FullScreenDialog.SIMPLE_ANIMATION
                    , ContextCompat.getColor(it, R.color.primary)
                    , ContextCompat.getColor(it, R.color.primary_dark))
                .setOnItemClick(object : FullScreenDialog.FullScreenDialogListener {
                    override fun onDismiss() {
                        goToHome()
                    }

                    override fun onClickAction(dialog: FullScreenDialog) {
                        goToHome()
                    }

                    override fun onClickAceptar(dialog: FullScreenDialog) {
                        goToHome()
                    }

                    override fun onBackPressed(dialog: FullScreenDialog) {
                        goToHome()
                    }

                    var home = true

                    private fun goToHome() {
                        if(home) {
                            home = false
                            val bundle = Bundle()
                            bundle.putString(HomeActivity.MENU_OPTION, MenuCodeTop.OFFERS)
                            listener?.onHome(bundle)
                        }
                    }

                })
                .show()
        }
    }
}
