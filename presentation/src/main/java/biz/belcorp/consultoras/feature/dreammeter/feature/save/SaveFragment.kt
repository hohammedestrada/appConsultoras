package biz.belcorp.consultoras.feature.dreammeter.feature.save

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.feature.dreammeter.DreamMeterActivity
import biz.belcorp.consultoras.feature.dreammeter.di.DreamMeterComponent
import biz.belcorp.consultoras.util.CountryUtil
import biz.belcorp.mobile.components.design.button.Button
import biz.belcorp.mobile.components.design.edittext.EditText
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import kotlinx.android.synthetic.main.fragment_save_dream.*
import javax.inject.Inject

class SaveFragment : BaseFragment(), SaveView {

    @Inject
    lateinit var presenter: SavePresenter

    private var listener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_save_dream, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(DreamMeterComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        init()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DreamMeterActivity) {
            listener = context
        }
    }

    private fun init() {
        loadGif(intArrayOf(R.drawable.ic_gif_1, R.drawable.ic_gif_2, R.drawable.ic_gif_3), 1000)
        presenter.getConfiguration()
        presenter.getDreamMeter(getExtraDreamMeter(), true)
    }

    override fun setupListener() {
        edtDreamName.listener = object : EditText.Listener {
            override fun onTextChanged(s: CharSequence?, count: Int?) {
                presenter.onEdtDreamNameAndDreamAmountTextChanged(s.toString(), edtDreamAmount.text.toString())
            }
        }

        edtDreamAmount.listener = object : EditText.Listener {
            override fun onTextChanged(s: CharSequence?, count: Int?) {
                presenter.onEdtDreamNameAndDreamAmountTextChanged(edtDreamName.text.toString(), s.toString())
            }
        }

        btnEmpezar.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                presenter.saveDreamMeter(getExtraDreamMeter(), edtDreamName.text.toString(), edtDreamAmount.text.toString())
            }
        }
    }

    override fun getExtraDreamMeter(): DreamMeter? {
        return arguments?.get(ARG_DREAM) as DreamMeter?
    }

    override fun showDreamMeter(dreamMeter: DreamMeter?) {
        edtDreamName.hint = dreamMeter?.dreamExample
        edtDreamAmount.hint = dreamMeter?.amountExample

        ctlMain.visibility = View.VISIBLE
    }

    override fun showDream(dreamMeter: DreamMeter?) {
        edtDreamName.text = dreamMeter?.consultantDream?.description
        edtDreamAmount.text = dreamMeter?.consultantDream?.dreamAmount.toString()

        setupListener()
    }

    override fun goToDetail(dreamMeter: DreamMeter, isReplace: Boolean) {
        listener?.goToDetail(dreamMeter, isReplace)
    }

    override fun enableButton() {
        btnEmpezar.addBackgroundColor(Color.BLACK)
        btnEmpezar.isDisable(false)
    }

    override fun disableButton() {
        btnEmpezar.addBackgroundColor(Color.parseColor("#909094"))
        btnEmpezar.isDisable(true)
    }

    override fun onErrorLoad() {
        activity?.finish()
    }


    override fun onGetConfiguration(user: User) {
        edtDreamAmount.textPrefix = user.countryMoneySymbol
    }

    override fun showMessage(message: String?) {
        BottomDialog.Builder(context())
            .setTitle(getString(R.string.revisa_la_ganancia_ingresada))
            .setTitleBold()
            .setContent(message ?: "")
            .setCancelable(false)
            .setRecomendacion(getString(R.string.ingresa_una_ganancia_menor))
            .setNeutralBackgroundColor(R.color.black)
            .setNeutralText(R.string.button_aceptar)
            .onNeutral(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog) {
                    edtDreamAmount.text = ""
                    edtDreamAmount.requestFocus()
                }
            })
            .show()
    }

    override fun setDreamMeter(dreamMeter: DreamMeter?) {
        arguments = Bundle().apply {
            putSerializable(ARG_DREAM, dreamMeter)
        }
    }

    override fun showError(e: Exception?) {
        when (e) {
            is NetworkErrorException -> showNetworkErrorWithListener(messageListener)
            else -> showErrorMessageWithListener(getString(R.string.ocurrio_un_error), messageListener)
        }
    }

    private val messageListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {
            activity?.finish()
        }

        override fun cancelar() {
        }
    }

    override fun context(): Context {
        return requireContext()
    }

    override fun setCanBack(canBack: Boolean) {
        listener?.setCanBack(canBack)
    }

    companion object {
        private const val ARG_DREAM = "ARG_DREAM"

        @JvmStatic
        fun newInstance(dreamMeter: DreamMeter?) = SaveFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_DREAM, dreamMeter)
            }
        }
    }

    interface Listener {
        fun goToDetail(dreamMeter: DreamMeter, isReplace: Boolean)
        fun setCanBack(isCanBack: Boolean)
    }

    private fun loadGif(images: IntArray, timeSleep: Long) = Thread(Runnable {
        var currentPosition = 0
        while (true) {
            val image = images[currentPosition]
            if (currentPosition == images.size - 1) {
                currentPosition = 0
            } else {
                currentPosition++
            }
            activity?.runOnUiThread {
                ivwRegalo?.setImageResource(image)

            }
            Thread.sleep(timeSleep)
        }
    }).start()

}
