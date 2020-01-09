package biz.belcorp.consultoras.feature.home.survey

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.survey.OptionSurveyModel
import biz.belcorp.consultoras.common.model.survey.ReasonModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.home.di.HomeComponent
import biz.belcorp.consultoras.util.FestivityAnimationUtil
import biz.belcorp.consultoras.util.anotation.SurveyReasonType
import biz.belcorp.consultoras.util.anotation.SurveyValidationType
import biz.belcorp.library.util.KeyboardUtil
import kotlinx.android.synthetic.main.fragment_cupon.*
import kotlinx.android.synthetic.main.survey_dialog_fragment.*
import javax.inject.Inject

class SurveyBottomDialogFragment : BottomSheetDialogFragment(), SurveyView, OptionSurveyAdapter.OnClickOptionSurvey, ReasonAdapter.OnClickReason {

    companion object {
        const val TAG = "SURVEY_BOTTOM_DIALOG_FRAGMENT"
        private var validationType: Int = 0
        private var campaign: String? = null
        private var componentType: Class<*>? = null

        fun newInstance(validationType: Int, campaign: String? = null, componentType: Class<*>? = null): SurveyBottomDialogFragment {
            this.validationType = validationType
            this.campaign = campaign
            this.componentType = componentType
            return SurveyBottomDialogFragment()
        }
    }

    @Inject
    lateinit var presenter: SurveyPresenter

    @Inject
    lateinit var optionSurveyAdapter: OptionSurveyAdapter

    @Inject
    lateinit var reasonAdapter: ReasonAdapter

    private var mBehavior: BottomSheetBehavior<FrameLayout>? = null

    private var isBlock = false

    var surveyDialogCallbacks: SurveyDialogCallbacks? = null

    var calificacionActual : String? = null

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        evaluateInject()
        presenter.view = this
        presenter.fillUser()
        presenter.campaign = campaign
        presenter.validationType = validationType
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            bottomSheet?.let {
                mBehavior = BottomSheetBehavior.from(it)
            }
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.survey_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickEvents()
        initRecycler()
        initBehaviorEvent()
        presenter.getSurvey()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    private fun initClickEvents() {
        otherReasonCheck.setOnCheckedChangeListener { _, p1 ->
            otherReasonEdit.text.clear()
            otherReasonEdit.visibility = if (p1) View.VISIBLE else View.INVISIBLE
        }

        backButton.setOnClickListener {
            dismissAllowingStateLoss()
        }

        continueButton.setOnClickListener {
            Tracker.Survey.onClickBotonConfirmar(calificacionActual, calificacionActual, presenter.anteriorCalificacionSeleccionada, presenter.user)
            actionContinue()
        }

        fltHoliday.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        presenter.sendAnswer(OptionSurveyModel().apply {
            encuestaID = presenter.encuestaID
            calificacionID = 0
            calificacion = "0"
            campaniaID = presenter.codigoCampania
            preguntaDescripcion = "0"
            motivoEncuesta = mutableListOf()
        }, false)
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        surveyDialogCallbacks?.onDismiss()
        super.onDismiss(dialog)
    }

    private fun initRecycler() {
        optionSurveyAdapter.onClickOptionSurvey = this
        optionRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        optionRecycler.adapter = optionSurveyAdapter

        reasonAdapter.onClickReason = this
        reasonRecycler.layoutManager = GridLayoutManager(context, 2)
        reasonRecycler.adapter = reasonAdapter
    }

    private fun initBehaviorEvent() {
        mBehavior?.let { behavior ->
            initialSurveyView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    initialSurveyView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    behavior.peekHeight = initialSurveyView.height
                }
            })
            behavior.isHideable = true
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // Empty
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        presenter.sendAnswer(OptionSurveyModel().apply {
                            encuestaID = presenter.encuestaID
                            calificacionID = 0
                            calificacion = "0"
                            campaniaID = presenter.codigoCampania
                            preguntaDescripcion = "0"
                            motivoEncuesta = mutableListOf()
                        }, false)
                        dismissAllowingStateLoss()
                    }

                    if (newState == BottomSheetBehavior.STATE_DRAGGING && isBlock) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            })
        }
    }

    override fun onClickOptionSurvey(optionSurveyModel: OptionSurveyModel) {
         calificacionActual = optionSurveyModel.calificacion ?: ""

        Tracker.Survey.trackClickTipoCalificacion(calificacionActual, presenter.anteriorCalificacionSeleccionada, presenter.user)
        Tracker.Survey.trackScreenTipoCalificacion(calificacionActual, presenter.user)

        presenter.anteriorCalificacionSeleccionada = optionSurveyModel.calificacion ?: ""

        if (validationType == SurveyValidationType.VALIDATION_SURVEY_MY_ORDERS) {
            optionSurveyModel.campaniaID = campaign?.toInt()
        }

        val filterListReason = optionSurveyModel.motivoEncuesta?.filter {
            it.tipoMotivo == SurveyReasonType.OPTION
        }?.toMutableList()

        if (filterListReason?.size == 0 && isBlock) {
            showLoading()
            presenter.sendAnswer(optionSurveyModel)
        }

        if (!isBlock) {
            presenter.sendAnswer(optionSurveyModel, filterListReason?.size == 0)
            showFullScreen()
        }

        questionText.text = optionSurveyModel.preguntaDescripcion
        otherReasonCheck.isChecked = false

        optionSurveyAdapter.optionList.forEach { it.isSelected = false }
        optionSurveyModel.isSelected = true
        optionSurveyAdapter.notifyItemRangeChanged(0, optionSurveyAdapter.itemCount)

        reasonAdapter.list = filterListReason
        reasonAdapter.notifyDataSetChanged()

        val isShowOther = optionSurveyModel.motivoEncuesta?.filter { it.tipoMotivo == SurveyReasonType.OTHER }?.size ?: 0 > 0
        otherReasonCheck.visibility = if (isShowOther) View.VISIBLE else View.INVISIBLE
    }

    override fun onClickReason(reason: ReasonModel, position: Int) {
        reason.isSelect = !reason.isSelect
        reasonAdapter.notifyItemChanged(position)
    }

    override fun showSurvey(surveyModel: List<OptionSurveyModel>, campaign: String?) {
        surveyDialogCallbacks?.onLoadSurvey()
        titleText?.text = String.format(getString(R.string.survey_title), campaign)
        optionSurveyAdapter?.optionList = surveyModel.toMutableList()
        optionSurveyAdapter?.notifyDataSetChanged()
        val animation = TranslateAnimation(0f, 0f,1000f, 0f)
        animation.duration = 1200
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                // Empty
            }

            override fun onAnimationEnd(p0: Animation?) {
                // Empty
            }

            override fun onAnimationStart(p0: Animation?) {
                initialSurveyView?.visibility = View.VISIBLE
            }
        })
        initialSurveyView?.startAnimation(animation)
    }

    override fun onSurveyIsResolved() {
        dismissAllowingStateLoss()
    }

    override fun onSendAnswerSurveySuccessful() {
        showThanksScreen()
    }

    override fun showLoading() {
        loadView?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadView?.visibility = View.GONE
    }

    override fun context(): Context? = activity?.applicationContext

    override fun onVersionError(required: Boolean, url: String?) {
        // Empty
    }

    private fun evaluateInject() {
        componentType?.let {
            val c = getComponent(it)
            when (c) {
                is HomeComponent -> c.inject(this)
                is ClientComponent -> c.inject(this)
            }
        }
    }

    private fun actionContinue() {
        KeyboardUtil.dismissKeyboard(context, otherReasonEdit)

        val optionSurveySelected = optionSurveyAdapter.optionSurveyModelSelected
        val filterList = optionSurveySelected?.motivoEncuesta?.filter { it.tipoMotivo == SurveyReasonType.OTHER }
        val isSendOptionOther = filterList?.size ?: 0 > 0


        val filterListOptionSelect = optionSurveySelected?.motivoEncuesta?.filter { it.tipoMotivo == SurveyReasonType.OPTION && it.isSelect }
        if (filterListOptionSelect?.size ?: 0 == 0 && !otherReasonCheck.isChecked) {
            Toast.makeText(activity, activity?.getString(R.string.survey_validation_reason), Toast.LENGTH_SHORT).show()
            return
        }


        if (isSendOptionOther && otherReasonCheck.isChecked) {
            if (otherReasonEdit.text.toString().isEmpty()) {
                Toast.makeText(activity, activity?.getString(R.string.survey_inconvenient_message), Toast.LENGTH_SHORT).show()
                return
            } else {
                val reason = optionSurveySelected?.motivoEncuesta?.filter { it.tipoMotivo == SurveyReasonType.OTHER }?.get(0)
                reason?.isSelect = true
                reason?.motivo = otherReasonEdit.text.toString()
            }
        }
        showLoading()
        if (validationType == SurveyValidationType.VALIDATION_SURVEY_MY_ORDERS) {
            optionSurveySelected?.campaniaID = campaign?.toInt()
        }
        presenter.sendAnswer(optionSurveySelected)
        Tracker.Survey.onClickBotonConfirmar2(presenter.motivosString, presenter.anteriorCalificacionSeleccionada, presenter.user)
    }

    private fun showThanksScreen() {
        backButton.visibility = View.INVISIBLE
        continueButton.visibility = View.INVISIBLE
        fltHoliday.visibility = View.VISIBLE
        fltHoliday.post {
            context?.let {
                FestivityAnimationUtil.getCommonConfetti(
                    ContextCompat.getColor(it, R.color.dorado),
                    ContextCompat.getColor(it, R.color.primary),
                    resources, fltHoliday)
            }
        }
        Handler().postDelayed({
            dismissAllowingStateLoss()
        }, 4500)
    }

    private fun showFullScreen() {
        mBehavior?.isHideable = false
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        mBehavior?.peekHeight = context?.resources?.displayMetrics?.heightPixels ?: 0

        shadowView.visibility = View.GONE
        parentView.setBackgroundColor(Color.WHITE)
        optionSurveyAdapter.isExpanded = true

        contentView.visibility = View.VISIBLE
        continueButton.visibility = View.VISIBLE
        indicatorView.visibility = View.INVISIBLE
        backButton.visibility = View.VISIBLE
        isBlock = true
    }

    @Throws(IllegalStateException::class)
    private fun <C> getComponent(componentType: Class<C>): C {
        return componentType.cast((activity as HasComponent<C>).component)
            ?: throw IllegalStateException(componentType.simpleName + " has not been initialized yet.")
    }

    interface SurveyDialogCallbacks {
        fun onDismiss()
        fun onLoadSurvey()
    }
}
