package biz.belcorp.consultoras.feature.vinculacion

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.domain.entity.CreditAgreement
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.feature.terms.di.TermsComponent
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.util.DeviceUtil
import kotlinx.android.synthetic.main.fragment_vinculacion.*
import java.util.regex.Pattern
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.widget.Toast
import biz.belcorp.library.log.BelcorpLogger


class VinculacionFragment : BaseFragment(), VinculacionView {

    @Inject
    lateinit var presenter: VinculacionPresenter

    private var vinculacionListener: VinculacionListener? = null

    private lateinit var user: User

    /** */


    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    /** */

    override fun onInjectView(): Boolean {
        getComponent(TermsComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
        presenter.data()
    }

    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is VinculacionListener) {
            this.vinculacionListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vinculacion, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter?.initScreenTrack()
    }

    /** */

    private fun init() {
        btnAccept.setOnClickListener {

            val request = CreditAgreement().apply {
                aceptado = true
                ip = ""
                val permissionCheck = ContextCompat.checkSelfPermission(context()!!,
                    android.Manifest.permission.READ_PHONE_STATE)
                imei = if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                    DeviceUtil.getUniqueIMEIId(context())
                else ""
                so = "Android (Linux; version " + DeviceUtil.getVersionCode()+
                    "; sdk "+ DeviceUtil.getVersionSDK() + "; " + DeviceUtil.getModel() + ")"
                deviceID = DeviceUtil.getId(context())
            }


            presenter.applyCreditAgreement(request)
        }

        tvwLink.setOnClickListener {
            presenter.getPdfCreditAgreement()
        }
    }

    override fun setData(user: User) {

        this.user = user

        var alias = user.alias?:""

        if (alias.isEmpty()) {
            val parts = user.consultantName?.split(Pattern.quote(" ").toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            if (parts?.size?:0 > 0)
                alias = parts!![0]
        }

        tvwMessage.text = String.format(getString(R.string.credit_application_description),alias)
    }

    override fun initScreenTrack(model: LoginModel) {
        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TEMS)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(model)

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties)
    }

    override fun onCreditAgreementAccept() {

        user?.let {
            if (it.isAceptaTerminosCondiciones) {
                vinculacionListener?.onHome()
            } else {
                vinculacionListener?.onTerms()
            }
        }

    }

    override fun onUrlCreditAgreement(url: String) {
        openPdf(url)
    }

    override fun onError(errorModel: ErrorModel) {
        errorModel?.let {
            processGeneralError(errorModel)
        }
    }

    override fun onError(message: String) {
        processGeneralError(ErrorModel(0, message, null))
    }

    /** */

    private fun openPdf(url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(activity, R.string.legal_activity_not_found, Toast.LENGTH_SHORT).show()
            BelcorpLogger.w("openPdfTerms", ex)
        }

    }

    /** */

    internal interface VinculacionListener {

        fun onHome()
        fun onTerms()
    }

    companion object {

        fun newInstance(): VinculacionFragment {
            return VinculacionFragment()
        }
    }
}
