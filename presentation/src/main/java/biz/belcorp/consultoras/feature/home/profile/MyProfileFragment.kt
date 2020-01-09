package biz.belcorp.consultoras.feature.home.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.*
import android.view.View
import android.text.style.ImageSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.component.CustomTypefaceSpan
import biz.belcorp.consultoras.common.dialog.CallCenterDialog
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.client.ClienteValidator
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.error.BooleanDtoModel
import biz.belcorp.consultoras.domain.entity.Country
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.feature.changeemail.ChangeEmailActivity
import biz.belcorp.consultoras.feature.home.profile.di.MyProfileComponent
import biz.belcorp.consultoras.feature.home.profile.password.ChangePasswordActivity
import biz.belcorp.consultoras.feature.sms.SMSActivity
import biz.belcorp.consultoras.util.*
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.log.BelcorpLogger
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_my_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import kotlinx.android.synthetic.main.tooltip_error.*
import permissions.dispatcher.*

@RuntimePermissions
open class MyProfileFragment : BaseFragment(), MyProfileView, CallCenterDialog.Listener {

    @Inject
    lateinit var presenter: MyProfilePresenter
    private var aceptaTerminosCondiciones: Boolean = false
    internal var listener: MyProfileFragmentListener? = null
    private lateinit var capturedImageUri: Uri
    internal lateinit var user: User
    var country: Country? = null
    var countryISO = ""
    private val deleteListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {
            presenter.deletePhoto(user)
        }

        override fun cancelar() {
            // Empty
        }
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(MyProfileComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    private fun init() {
        presenter.data(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is MyProfileFragmentListener) {
            this.listener = context
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.initScreenTrack()
        presenter.refreshData(0)
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SMS && resultCode == Activity.RESULT_OK && data != null) run {
            val result = data.getIntExtra(SMSActivity.RESULT_SMS, 0)
            if (result == 1) {
                presenter.refreshData(1)
            }
        }
        if (requestCode == REQUEST_EMAIL && resultCode == Activity.RESULT_OK) run {
                presenter.refreshData(1)
        }
    }

    override fun initScreenTrack(loginModel: LoginModel) {
        val bundle = Bundle()
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_MI_PERFIL)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(loginModel)
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties)
    }

    override fun saveUserCountry(country: Country) {
        this.country = country

        val tfBold = Typeface.createFromAsset(context?.assets, GlobalConstant.LATO_BOLD_SOURCE)

        val span = SpannableStringBuilder()
        span.append(getString(R.string.my_profile_call_center))

        if (country.telefono1 != null && country.telefono2 != null) {

            val spannablePhone1 = Spannable.Factory.getInstance().newSpannable(country.telefono1)
            spannablePhone1.setSpan(CustomTypefaceSpan(tfBold), 0, country.telefono1?.length ?: 0, 0)

            span.append(" ")
            span.append(spannablePhone1)

            val spannablePhone2 = Spannable.Factory.getInstance().newSpannable(country.telefono2)
            spannablePhone2.setSpan(CustomTypefaceSpan(tfBold), 0, country.telefono2?.length ?: 0, 0)

            span.append(" o al ")
            span.append(spannablePhone2)

        } else if (country.telefono1 != null) {
            val spannablePhone1 = Spannable.Factory.getInstance().newSpannable(country.telefono1)
            spannablePhone1.setSpan(CustomTypefaceSpan(tfBold), 0, country.telefono1?.length ?: 0, 0)

            span.append(" ")
            span.append(spannablePhone1)
        } else if (country.telefono2 != null) {
            val spannablePhone2 = Spannable.Factory.getInstance().newSpannable(country.telefono2)
            spannablePhone2.setSpan(CustomTypefaceSpan(tfBold), 0, country.telefono2?.length ?: 0, 0)

            span.append(" ")
            span.append(spannablePhone2)
        }

        tvwCallCenter.text = span
        tvwCallCenter.visibility = View.VISIBLE

        tvwCallCenter.setOnClickListener {

            val numbersList: ArrayList<String> = ArrayList()

            country.telefono1?.let { t ->
                if (country.telefono1?.contains("/") == true) {

                    val parts = country.telefono1?.split(Pattern.quote("/").toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()

                    parts?.forEach { p ->
                        numbersList.add(p)
                    }

                } else {
                    numbersList.add(t)
                }
            }

            country.telefono2?.let { t ->
                if (country.telefono2?.contains("/") == true) {

                    val parts = country.telefono2?.split(Pattern.quote("/").toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()

                    parts?.forEach { p ->
                        numbersList.add(p)
                    }

                } else {
                    numbersList.add(t)
                }
            }
            context?.let {
                CallCenterDialog.Builder(it, this)
                    .withList(numbersList)
                    .show()
            }
        }
    }

    override fun showUserData(newUser: User, update: Int) {
        user = newUser

        countryISO = user.countryISO ?: ""

        tvwUser.text = newUser.consultantName
        tvwCode.text = newUser.userCode
        tvwZone.text = newUser.zoneCode
        newUser.alias?.let {
            edtApodo.setText(it)
            edtApodo.setSelection(it.length)
        }
        edtEmail.setText(newUser.email)
        edtCelular.setText(if (newUser.mobile == null || newUser.mobile?.isEmpty() == true) "" else newUser.mobile)
        edtTelefono.setText(if (newUser.phone == null || newUser.phone?.isEmpty() == true) "" else newUser.phone)
        aceptaTerminosCondiciones = newUser.isAceptaTerminosCondiciones

        if (newUser.otherPhone == null || newUser.otherPhone?.isEmpty() == true) {
            lltNumeroAdicional.visibility = View.GONE
            lltAddNumero.visibility = View.VISIBLE
        } else {
            lltAddNumero.visibility = View.GONE
            lltNumeroAdicional.visibility = View.VISIBLE
            edtOtroNumero.setText(newUser.otherPhone)
        }


        user.isPuedeActualizar?.let {
            if (it) {
                user.isPuedeActualizarEmail?.let { t ->
                    if (t) {
                        lltCambiarEmail.visibility = View.VISIBLE
                        btnConfirmarCorreo.visibility = View.VISIBLE
                        imgstaConfirMail.visibility = View.VISIBLE
                    } else {
                        lltCambiarEmail.visibility = View.INVISIBLE
                        btnConfirmarCorreo.visibility = View.INVISIBLE
                        imgstaConfirMail.visibility = View.GONE
                    }
                }

                user.isPuedeActualizarCelular?.let { t ->
                    if (t) {
                        lltCambiarCelular.visibility = View.VISIBLE
                        btnConfirmarCelular.visibility = View.VISIBLE
                        imgstaConfirCel.visibility = View.VISIBLE
                    } else {
                        lltCambiarCelular.visibility = View.INVISIBLE
                        btnConfirmarCelular.visibility = View.INVISIBLE
                        imgstaConfirCel.visibility = View.GONE
                    }
                }
                user.mobile?.let { mobile ->
                    if (mobile.isNotEmpty() || !mobile.isBlank()) {
                        chkRecibirWhatsapp.isEnabled = true
                        chkRecibirWhatsapp.isClickable = true
                        tvwRecibirWhatsapp.setOnClickListener(null)
                    } else {
                        errorCheckWhatsapp()
                    }
                } ?: run {
                    errorCheckWhatsapp()
                }
                mostrarIconoConfirmacion()
                btnConfirmarCorreo.buttonClickListener = object : biz.belcorp.mobile.components.design.button.Button.OnClickListener {
                    override fun onClick(view: View) {

                        if (!edtEmail.text.isNullOrBlank()) {
                            if (edtEmail.text.toString().validarFormatoMail())
                                presenter.enviarCorreo(edtEmail.text.toString())
                            else {
                                showEmailError(getString(R.string.client_registration_validation_2))
                            }

                        } else {
                            showEmailError(getString(R.string.client_registration_validation_6))
                        }
                    }
                }

                tvwCallCenter.visibility = View.GONE

                btnConfirmarCelular.buttonClickListener = object : biz.belcorp.mobile.components.design.button.Button.OnClickListener {
                    override fun onClick(view: View) {
                        if (!edtCelular.text.isNullOrBlank()) {
                            startSMSActivity(true)
                        }
                        else{
                            showMobileError(getString(R.string.client_registration_validation_5))
                        }
                    }
                }

            } else {

                presenter.getCountry(user.countryISO)
                imgstaConfirCel.visibility = View.GONE
                imgstaConfirMail.visibility = View.GONE
                lltCambiarCelular.visibility = View.INVISIBLE
                btnConfirmarCelular.visibility = View.INVISIBLE
                lltCambiarEmail.visibility = View.INVISIBLE
                btnConfirmarCorreo.visibility = View.INVISIBLE
                lltAddNumero.visibility = View.GONE

                edtEmail.isEnabled = false
                edtEmail.isFocusable = false

                edtCelular.isEnabled = false
                edtCelular.isFocusable = false

                edtTelefono.isEnabled = false
                edtTelefono.isFocusable = false
            }

        }

        newUser.isCambioCorreoPendiente?.let {
            if (it) {
                mostrarMensajeConfirmacion(tvwErrorEmail, GlobalConstant.FALTA_CONFIRMAR_CORREO)
            }
        }

        chkRecibirWhatsapp.isChecked = newUser.isNotificacionesWhatsapp
        chkRecibirWhatsapp.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked != newUser.isNotificacionesWhatsapp) {
                activateSave()
            } else {
                context?.let {
                    tvwGuardar.setBackgroundColor(ContextCompat.getColor(it, R.color.red_grey))
                }
                tvwGuardar.isEnabled = false
            }

        }

        edtApodo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // EMPTY
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // EMPTY
            }

            override fun afterTextChanged(s: Editable) {
                if (user.alias != s.toString()) {
                    activateSave()
                }
            }
        })
        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // EMPTY
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // EMPTY
            }

            override fun afterTextChanged(s: Editable) {
                if (user.email != s.toString()) {
                    activateSave()
                }
            }
        })
        edtCelular.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // EMPTY
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // EMPTY
            }

            override fun afterTextChanged(s: Editable) {
                if (user.mobile != s.toString()) {
                    activateSave()
                }
            }
        })
        edtTelefono.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // EMPTY
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // EMPTY
            }

            override fun afterTextChanged(s: Editable) {
                if (user.phone != s.toString()) {
                    activateSave()
                }
            }
        })
        edtOtroNumero.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // EMPTY
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (user.otherPhone != s.toString()) {
                    activateSave()
                }
            }

            override fun afterTextChanged(s: Editable) {
                // EMPTY
            }
        })

        if (newUser.photoProfile != null && newUser.photoProfile?.isNotEmpty() == true) {
            Glide.with(this).load(newUser.photoProfile).apply(RequestOptions.noTransformation()
                .placeholder(R.drawable.ic_contact_default)
                .error(R.drawable.ic_contact_default)
                .priority(Priority.HIGH))
                .into(ivwPhoto)
        } else {
            context?.let {
                ivwPhoto.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_contact_default))
            }
        }
        rlt_camera.setOnClickListener {
            optionsPhoto()
        }
        llt_change_password.setOnClickListener {
            changePassword()
        }
        lltAddNumero.setOnClickListener {
            addNumero()
        }
        llt_eliminar_numero.setOnClickListener {
            eliminarNumero()
        }
        tvwGuardar.setOnClickListener {
            guardarDatos()
        }
        lltCambiarCelular.setOnClickListener {
            cambiarCelular()
        }
        lltCambiarEmail.setOnClickListener {
            cambiarEmail()
        }

        if (update == 1) {
            context?.let {
                ToastUtil.show(it, R.string.my_profile_update, Toast.LENGTH_LONG)
            }
        }

    }

    private fun mostrarIconoConfirmacion() {
        if (user.email != null && !user.email.isNullOrBlank() && !user.email.isNullOrEmpty()) {
            user.isCambioCorreoPendiente?.let {
                if (it) {
                    imgstaConfirMail.setImageResource(R.drawable.ic_icono_pendiente_confirmacion)
                    mostrarMensajeConfirmacion(tvwErrorEmail, GlobalConstant.FALTA_CONFIRMAR_CORREO)
                } else {
                    imgstaConfirMail.setImageResource(R.drawable.ic_icono_confirmacion)
                    tvwErrorEmail.visibility = View.GONE
                }
            }
        } else {
            imgstaConfirMail.visibility = View.GONE
        }

        if(user.mobile!=null && !user.mobile.isNullOrBlank() && !user.mobile.isNullOrEmpty()){
            user.isCambioCelularPendiente?.let {
                if (it) {
                    imgstaConfirCel.setImageResource(R.drawable.ic_icono_pendiente_confirmacion)
                    mostrarMensajeConfirmacion(tvwErrorMobile, GlobalConstant.FALTA_CONFIRMAR_NUMERO)
                } else {
                    imgstaConfirCel.setImageResource(R.drawable.ic_icono_confirmacion)
                    tvwErrorMobile.visibility = View.GONE
                }
            }
        }
        else{
            imgstaConfirCel.visibility = View.GONE
        }

    }

    private fun errorCheckWhatsapp() {
        context?.let {
            chkRecibirWhatsapp.isEnabled = false
            chkRecibirWhatsapp.isClickable = false
            chkRecibirWhatsapp.isChecked = false
            tvwMensajeError.text = it.getString(R.string.my_profile_whatsapp_deshabilitado)
            setTagInfo(tvwRecibirWhatsapp.text.toString(), tvwRecibirWhatsapp)
            tvwRecibirWhatsapp.setOnClickListener {
                mensajeErrorWhatsapp.visibility = if (mensajeErrorWhatsapp.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }


    }

    override fun saveUserData(status: Boolean?) {
        tvwGuardar.setBackgroundColor(Color.parseColor("#cdc7c8"))
        tvwGuardar.isEnabled = false
        chkTermsAccept.isChecked = false

        context?.let {
            ToastUtil.show(it, getString(R.string.my_profile_update), Toast.LENGTH_SHORT)
        }
        init()
    }

    override fun saveUpload(status: Boolean?) {
        Toast.makeText(context(), getString(R.string.my_profile_image_upload), Toast.LENGTH_SHORT).show()
        init()
    }

    override fun saveDelete(status: Boolean?) {
        Toast.makeText(context(), getString(R.string.my_profile_image_delete), Toast.LENGTH_SHORT).show()
        init()
    }

    override fun savePassword() {
        // EMPTY
    }

    override fun onError(errorModel: ErrorModel) {
        processGeneralError(errorModel)
    }

    override fun onError(errorModel: BooleanDtoModel) {
        showError("ERROR", errorModel.message)
    }

    override fun onCall(number: String) {
        CommunicationUtils.llamar(activity, number)

    }


    fun optionsPhoto() {
        activity?.run{
            val view = this.layoutInflater.inflate(R.layout.custom_dialog_profile, null)

            val dialog = Dialog(this, R.style.FullScreenDialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(view)

            val lltCamara = view?.findViewById<LinearLayout>(R.id.llt_camara)
            val lltGallery = view?.findViewById<LinearLayout>(R.id.llt_gallery)
            val lltPhotoDelete = view?.findViewById<LinearLayout>(R.id.llt_photo_delete)
            val ivwClose = view?.findViewById<ImageView>(R.id.ivw_close)

            lltCamara?.setOnClickListener {
                dialog.dismiss()
                takePhotoWithPermissionCheck()
            }

            lltGallery?.setOnClickListener {
                dialog.dismiss()
                pickFromGalleryWithPermissionCheck()
            }

            lltPhotoDelete?.setOnClickListener {
                dialog.dismiss()
                deletePhoto()
            }

            ivwClose?.setOnClickListener { dialog.dismiss() }

            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            dialog.show()
        }

    }

    fun changePassword() {
        val intent = Intent(activity, ChangePasswordActivity::class.java)
        startActivity(intent)
    }

    fun addNumero() {
        if (lltAddNumero.isShown) {
            lltAddNumero.visibility = View.GONE
            lltNumeroAdicional.visibility = View.VISIBLE
        }
    }

    fun eliminarNumero() {
        if (lltNumeroAdicional.isShown) {
            lltNumeroAdicional.visibility = View.GONE
            lltAddNumero.visibility = View.VISIBLE
            edtOtroNumero.setText("")
        }
    }

    fun guardarDatos() {
        if (edtCelular.isEnabled) {
            if (!validate(edtCelular.text.toString())) return
        }

        disableAllErrors()
        hideKeyboard()
        val name = edtApodo.text.toString().trim { it <= ' ' }
        val email = edtEmail.text.toString().trim { it <= ' ' }
        val mobile = edtCelular.text.toString().trim { it <= ' ' }
        val phone = edtTelefono.text.toString().trim { it <= ' ' }
        val otherMobile = edtOtroNumero.text.toString().trim { it <= ' ' }

        if ("" == name && this.user.countryISO != biz.belcorp.library.annotation.Country.CO) {
            val message = getString(R.string.client_registration_validation_0)
            showNameError(message)
            return
        }
        if (this.user.countryISO != biz.belcorp.library.annotation.Country.CO) {
            if ("" == email) {
                val message = getString(R.string.client_registration_validation_6)
                showEmailError(message)
                return
            } else if (!email.matches(("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex())) {
                val message = getString(R.string.client_registration_validation_2)
                showEmailError(message)
                return
            }
        }

        if ("" == mobile && "" == phone
            && this.user.countryISO != biz.belcorp.library.annotation.Country.CO) {
            val message = getString(R.string.client_registration_validation_1)
            showMobileError(message)
            showPhoneError(message)
            Toast.makeText(activity, getString(R.string.client_registration_validation_5), Toast.LENGTH_LONG).show()
            return
        }

        if (!chkTermsAccept.isChecked) {
            Toast.makeText(activity, getString(R.string.terms_error_accept), Toast.LENGTH_SHORT).show()
            return
        }

        val user = User()
        user.alias = name
        user.email = email
        user.mobile = mobile
        user.phone = phone
        user.otherPhone = otherMobile
        user.isAceptaTerminosCondiciones = aceptaTerminosCondiciones
        user.isNotificacionesWhatsapp = chkRecibirWhatsapp.isChecked
        user.isActivaNotificaconesWhatsapp = lnrCheckWhatsapp.visibility == View.VISIBLE

        presenter.saveData(user)
    }

    private fun cambiarCelular() {

        startSMSActivity()

    }

    private fun mostrarMensajeConfirmacion(textView: TextView, mensaje: String) {
        textView.visibility = View.VISIBLE
        textView.compoundDrawablePadding = 0
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        textView.text = mensaje
    }

    private fun startSMSActivity(directo: Boolean = false) {
        val intent = Intent(activity, SMSActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(SMSActivity.EXTRA_SMS_DIRECTO, directo)
        bundle.putString(SMSActivity.EXTRA_PHONE_NUMBER, user.mobile)
        bundle.putString(SMSActivity.EXTRA_COUNTRY_ISO, user.countryISO)
        bundle.putString(SMSActivity.EXTRA_CAMPAING, user.campaing)
        intent.putExtras(bundle)

        activity?.startActivityForResult(intent, REQUEST_SMS)
    }

    private fun cambiarEmail() {
        val intent = Intent(activity, ChangeEmailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(ChangeEmailActivity.OLD_EMAIL, edtEmail.text.toString())
        intent.putExtras(bundle)
        startActivity(intent)
        (activity as MyProfileActivity).setChangeEmail(true)
    }

    private fun showNameError(message: String) {
        tvwErrorApodo.text = message
        tvwErrorApodo.visibility = View.VISIBLE
        edtApodo.background = context?.let { ContextCompat.getDrawable(it, R.drawable.edt_bg_white_error_selector) }
    }

    private fun showEmailError(message: String) {
        tvwErrorEmail.text = message
        tvwErrorEmail.visibility = View.VISIBLE
        edtEmail.background = context?.let {ContextCompat.getDrawable(it, R.drawable.edt_bg_white_error_selector)}
    }

    private fun showMobileError(message: String) {
        tvwErrorMobile.text = message
        tvwErrorMobile.visibility = View.VISIBLE
        edtCelular.background = context?.let {ContextCompat.getDrawable(it, R.drawable.edt_bg_white_error_selector)}
    }

    private fun showPhoneError(message: String) {
        tvwErrorPhone.text = message
        tvwErrorPhone.visibility = View.VISIBLE
        edtTelefono.background = context?.let {ContextCompat.getDrawable(it, R.drawable.edt_bg_white_error_selector)}
    }

    fun disableAllErrors() {
        tvwErrorApodo.visibility = View.GONE
        tvwErrorEmail.visibility = View.GONE
        tvwErrorMobile.visibility = View.GONE
        tvwErrorPhone.visibility = View.GONE

        context?.let {
            edtApodo.background = ContextCompat.getDrawable(it, R.drawable.edt_bg_white_selector)
            edtEmail.background = ContextCompat.getDrawable(it, R.drawable.edt_bg_white_selector)
            edtCelular.background = ContextCompat.getDrawable(it, R.drawable.edt_bg_white_selector)
            edtTelefono.background = ContextCompat.getDrawable(it, R.drawable.edt_bg_white_selector)
        }
    }

    fun activateSave() {
        activity?.let {
            tvwGuardar.setBackgroundColor(ContextCompat.getColor(it, R.color.primary))
            tvwGuardar.isEnabled = true
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity?.currentFocus
        if (view == null) view = View(activity)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun takePhoto() {
        val folder = File(Environment.getExternalStorageDirectory().toString() + "/" + activity?.packageName + "/" + getString(R.string.my_profile_directory))

        if (!folder.exists()) {
            folder.mkdirs()
        }

        val cal = Calendar.getInstance()
        val file = File(folder, cal.timeInMillis.toString() + ".jpg")

        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (ex: Exception) {
                BelcorpLogger.w("createNewFile", ex)
            }

        } else {
            file.delete()
            try {
                file.createNewFile()
            } catch (ex: Exception) {
                BelcorpLogger.w("takePhoto", ex)
            }
        }

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        activity?.let {
            capturedImageUri = FileProvider.getUriForFile(it, GlobalConstant.PROVIDER_FILE, file)
        }

        listener?.setImageUri(capturedImageUri)

        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (capturedImageUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        }
        activity?.startActivityForResult(intent, REQUEST_CAMERA)
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        activity?.startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onShowRationalePickFromGallery(request: PermissionRequest){
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_write_rationale)
                .setPositiveButton(R.string.button_aceptar) { _, _ -> request.proceed() }
                .setNegativeButton(R.string.button_cancelar) { _, _ -> request.cancel() }
                .setCancelable(false)
                .show()
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onShowRationaleTakePhoto(request: PermissionRequest){
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.button_aceptar) { _, _ -> request.proceed() }
                .setNegativeButton(R.string.button_cancelar) { _, _ -> request.cancel() }
                .setCancelable(false)
                .show()
        }
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onNeverAskAgainPickFromGallery(){
        context?.let {
            Toast.makeText(it, R.string.permission_write_neverask, Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_write_denied)
                .setPositiveButton(R.string.button_go_to_settings) { _, _ ->
                    CommunicationUtils.goToSettingsForResult(this, REQUEST_SELECT_PICTURE)
                }
                .setNegativeButton(R.string.button_cancelar) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgainTakePhoto(){
        context?.let {
            Toast.makeText(it, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_camera_denied)
                .setPositiveButton(R.string.button_go_to_settings) { _, _ ->
                    CommunicationUtils.goToSettingsForResult(this, REQUEST_CAMERA)
                }
                .setNegativeButton(R.string.button_cancelar) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun deletePhoto() {
        try {
            val messageDialog = MessageDialog()
            messageDialog.isCancelable = false
            fragmentManager?.let {
                messageDialog
                    .setIcon(R.drawable.ic_alerta, 0)
                    .setStringTitle(R.string.my_profile_dialog_title)
                    .setStringMessage(R.string.my_profile_dialog_msg)
                    .setStringAceptar(R.string.button_aceptar)
                    .showCancel(true)
                    .showIcon(true)
                    .showClose(false)
                    .setListener(deleteListener)
                messageDialog.setStringCancelar(R.string.button_cancelar)
                messageDialog.show(it, "modalAceptar")
            }

        } catch (e: IllegalStateException) {
            BelcorpLogger.w("deletePhoto", e)
        }

    }

    private fun requestPermission(permission: String, rationale: String, requestCode: Int) {
        activity?.let {
            if (ActivityCompat.shouldShowRequestPermissionRationale(it, permission)) {
                showAlertDialog(getString(R.string.permission_title_rationale), rationale, DialogInterface.OnClickListener { dialog, which -> ActivityCompat.requestPermissions(it, arrayOf(permission), requestCode) }, getString(R.string.label_ok), null, getString(R.string.label_cancel))
            } else {
                ActivityCompat.requestPermissions(it, arrayOf(permission), requestCode)
            }
        }
    }

    private fun showAlertDialog(title: String?, message: String?,
                                onPositiveButtonClickListener: DialogInterface.OnClickListener?,
                                positiveText: String,
                                onNegativeButtonClickListener: DialogInterface.OnClickListener?,
                                negativeText: String) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(positiveText, onPositiveButtonClickListener)
            builder.setNegativeButton(negativeText, onNegativeButtonClickListener)
            builder.show()
        }
    }

    fun setPhoto(path: String) {
        val codeGenerate = user.countryISO + "_" + user.userCode + "_" + String.format(Locale.US, "%05d", Random().nextInt(100000)) + ".jpg"
        val file = File(path)
        val fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Builder().addFormDataPart("file-type", "profile").addFormDataPart("Archivo", codeGenerate, fileBody).build()
        presenter.uploadPhoto("multipart/form-data; boundary=" + body.boundary(), body)
    }

    override fun trackBackPressed(loginModel: LoginModel) {
        val analytics = Bundle()
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_MI_PERFIL)
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK)
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK)
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE)
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(loginModel)
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties)
    }

    override fun activateCheckWhatsapp(mostrar: Boolean) {

        lnrCheckWhatsapp?.visibility = if (mostrar) View.VISIBLE else View.GONE
    }

    fun trackBackPressed() {
        presenter.trackBackPressed()
    }


    internal interface MyProfileFragmentListener {

        fun setImageUri(uri: Uri?)
    }

    companion object {

        const val REQUEST_SELECT_PICTURE = 999
        const val REQUEST_CAMERA = 998
        const val REQUEST_SMS = 997
        const val REQUEST_EMAIL = 996

        fun newInstance(): MyProfileFragment {
            return MyProfileFragment()
        }
    }

    fun validate(number: String): Boolean {
        // Validacion del campo Requerido
        if (ClienteValidator.validateRequiredString(number)) {
            tvwErrorMobile.visibility = View.INVISIBLE
        } else {
            tvwErrorMobile.text = resources.getString(R.string.client_registration_validation_7)
            tvwErrorMobile.visibility = View.VISIBLE
            return false
        }
        // Validacion del primer numero
        if (ClienteValidator.validateStartNumber(number, countryISO)) {
            tvwErrorMobile.visibility = View.INVISIBLE
        } else {
            tvwErrorMobile.text = resources.getString(R.string.start_number_validation).replace("$", CountryUtil.getMobileStartNumberMap()[countryISO].toString())
            tvwErrorMobile.visibility = View.VISIBLE
            return false
        }
        // Validacion de la longitud del numero
        if (ClienteValidator.validateMobileLength(number, countryISO)) {
            tvwErrorMobile.visibility = View.INVISIBLE
        } else {
            tvwErrorMobile.text = resources.getString(R.string.length_validation).replace("$", CountryUtil.getMobileLengthMap()[countryISO].toString())
            tvwErrorMobile.visibility = View.VISIBLE
            return false
        }
        return true
    }

    override fun activeCellphone() {
        lltCambiarCelular.postDelayed({
            if (lltCambiarCelular.visibility == View.INVISIBLE || lltCambiarCelular.visibility == View.GONE) {
                edtCelular.isClickable = true
                edtCelular.isEnabled = true
                edtCelular.isFocusable = true
                edtCelular.isFocusableInTouchMode = true
            }
        }, 850)
    }

    override fun goToVerifyEmail() {
        val intent = Intent(activity, ChangeEmailActivity::class.java)
        var bundle = Bundle()
        bundle.putString(ChangeEmailActivity.SAME_EMAIL, user.email)
        intent.putExtras(bundle)
        activity?.startActivityForResult(intent, REQUEST_EMAIL)
    }

    private fun setTagInfo(mensaje: String, twv: TextView) {

        twv.gravity = Gravity.CENTER or Gravity.START

        val msg = mensaje.trim { it <= ' ' } + "   "

        val ss = SpannableString(msg)
        context?.let {c ->
            val drawable = ContextCompat.getDrawable(c, R.drawable.information)
            drawable?.let { draw ->
                val intrinsinctHeight = draw.intrinsicHeight
                val intrinsinctWidth = draw.intrinsicWidth

                val width = DeviceUtil.convertDpToPixel(2f, context).toInt()
                val height = DeviceUtil.convertDpToPixel(2f, context).toInt()

                val rect = Rect(0, 0, intrinsinctWidth - width, intrinsinctHeight - height)

                draw.bounds = rect

                val imageSpan = CenteredImageSpan(draw, ImageSpan.ALIGN_BASELINE)
                ss.setSpan(imageSpan, mensaje.trim { it <= ' ' }.length + 2, msg.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

                twv.text = ss
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
