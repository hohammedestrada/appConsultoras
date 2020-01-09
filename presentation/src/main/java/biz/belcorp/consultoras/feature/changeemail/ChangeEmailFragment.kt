package biz.belcorp.consultoras.feature.changeemail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.changeemail.di.ChangeEmailComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ToastUtil
import biz.belcorp.consultoras.util.validarFormatoMail
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.KeyboardUtil
import kotlinx.android.synthetic.main.fragment_change_email.*
import javax.inject.Inject

/**
 * @author andres.escobar on 3/08/2017.
 */
/** */

class ChangeEmailFragment : BaseFragment(), ChangeEmailView {

    @Inject
    lateinit var presenter: ChangeEmailPresenter

    private var listener: ChangeEmailFragmentListener? = null

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
        this.presenter.attachView(this)
        init()
    }

    private fun init() {

        tvwCancelar.paintFlags = tvwCancelar.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        btnChangeEmail.setOnClickListener {
            hideKeyboard()
            if (edtNewEmail.text.toString().isEmpty()) {
                tvwErrorMessage.visibility = View.VISIBLE
                tvwErrorMessage.text = getString(R.string.my_profile_error_empty_email)
            } else if(!edtNewEmail.text.toString().validarFormatoMail()){
                tvwErrorMessage.visibility = View.VISIBLE
                tvwErrorMessage.text = getString(R.string.my_profile_error_invalid_email)
            }
            if (chkTerms.isChecked) {
                tvwErrorMessage.visibility = View.GONE
                presenter.enviarCorreo(edtNewEmail.text.toString())
            } else {
                ToastUtil.show(context!!, "Debe aceptar los términos y condiciones para continuar."
                    , Toast.LENGTH_SHORT)
            }
        }

        arguments?.get(ChangeEmailActivity.OLD_EMAIL)?.let {oldEmail->
            var old = oldEmail as String
            gestionView(old)

        }?:kotlin.run {
            arguments?.getString(ChangeEmailActivity.NEW_EMAIL)?.let {it->
                gestionView(it)
            }?: kotlin.run {
                arguments?.getString(ChangeEmailActivity.SAME_EMAIL)?.let {it->
                    gestionView(it)
                }
            }

        }



        tvwCancelar.setOnClickListener {
            listener?.onHome()
        }
        tvwErrorMessage.visibility = View.GONE
        presenter.getUrlTyC()
    }

    override fun setUrlTyc(url: String) {
        tvwTerms.setOnClickListener {
            openPdfTerms(url)
        }
    }
    fun gestionView(correo:String){
        if (correo.isEmpty()) {
            txtChangeEmailNewMessage.visibility = View.VISIBLE
            tvwOldEmail.visibility = View.GONE
            edtOldEmail.visibility = View.GONE
        } else {
            txtChangeEmailNewMessage.visibility = View.GONE
            tvwOldEmail.visibility = View.VISIBLE
            edtOldEmail.text = correo
        }
    }
    override fun onEmailUpdated() {
        listener?.goToVerifyEmail(edtNewEmail.text.toString())
    }

    override fun onError() {
        ToastUtil.show(context!!, "Ocurrio un error al enviar correo. " +
            "Inténtelo nuevamente.", Toast.LENGTH_SHORT)
    }

    override fun onError(message: String) {
        ToastUtil.show(context!!, message, Toast.LENGTH_SHORT)
    }

    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ChangeEmailFragmentListener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_email, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.initScreenTrack()
    }

    /** */

    private fun openPdfTerms(urlTerminos: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlTerminos))
            startActivity(browserIntent)
        } catch (ex: ActivityNotFoundException) {
            ToastUtil.show(activity!!, R.string.terms_activity_not_found, Toast.LENGTH_SHORT)
            BelcorpLogger.w("openPdfTerms", ex)
        }

    }

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_CHANGES, model)
    }

    private fun hideKeyboard() {
        var view = activity!!.currentFocus
        if (view == null) view = View(activity)
        KeyboardUtil.dismissKeyboard(activity!!, view)
    }


    /** */

    internal interface ChangeEmailFragmentListener {
        fun onHome()
        fun goToVerifyEmail(newEmail: String)
    }

    companion object {

        fun newInstance(): ChangeEmailFragment {
            return ChangeEmailFragment()
        }
    }

    /** */

}
