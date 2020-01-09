package biz.belcorp.consultoras.feature.verifyemail

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.feature.changeemail.ChangeEmailActivity
import biz.belcorp.consultoras.feature.changeemail.di.ChangeEmailComponent
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.analytics.BelcorpAnalytics
import kotlinx.android.synthetic.main.fragment_verify_email.*
import javax.inject.Inject

/**
 * @author andres.escobar on 3/08/2017.
 */
/** */

class VerifyEmailFragment : BaseFragment(), VerifyEmailView {

    @Inject
    lateinit var presenter: VerifyEmailPresenter

    private var listener: VerifyEmailFragmentListener? = null

    override fun context(): Context? {
        return null
    }

    /** */

    override fun onInjectView(): Boolean {
        getComponent(ChangeEmailComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter!!.attachView(this)
        init()
    }

    private fun init() {
        tvwNewEmail.text = arguments?.getString(ChangeEmailActivity.NEW_EMAIL)
        tvwReenviar.setOnClickListener {
            presenter.enviarCorreo(tvwNewEmail.text.toString())
        }
        tvwCambiarCorreo.setOnClickListener {

            listener?.goToChangeEmail()
        }
    }

    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is VerifyEmailFragmentListener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_verify_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun applicationLabel(con: Context, info: ApplicationInfo): String {
        val p = con.packageManager
        return p.getApplicationLabel(info).toString()
    }

    override fun onResume() {
        super.onResume()
        if (presenter != null) presenter!!.initScreenTrack()
    }

    override fun onEmailUpdated() {
        Toast.makeText(context, "Email reenviado, " +
            "mire la bandeja de entrada de su correo..", Toast.LENGTH_SHORT).show()
    }

    override fun onError() {
        Toast.makeText(context, "Ocurrio un error al enviar correo. " +
            "Inténtelo nuevamente.", Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /** */

    override fun initScreenTrack(loginModel: LoginModel) {
        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TEMS)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(loginModel)

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties)
    }

    /** */

    internal interface VerifyEmailFragmentListener {
        fun onHome()
        fun goToChangeEmail()
    }

    companion object {

        fun newInstance(): VerifyEmailFragment {
            return VerifyEmailFragment()
        }
    }

    /** */

}
