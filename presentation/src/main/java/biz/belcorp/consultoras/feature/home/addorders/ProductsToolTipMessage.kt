package biz.belcorp.consultoras.feature.home.addorders

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.tooltip_duoperfecto_agregado_uno.*
import kotlinx.android.synthetic.main.tooltip_duoperfecto_felicidades.*
import kotlinx.android.synthetic.main.tooltip_duoperfecto_mensaje.*

class ProductsToolTipMessage : BottomSheetDialogFragment() {
    var listener: DeletePerfectDuo? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when (arguments!!.getInt("type")) {
            1 -> inflater.inflate(R.layout.tooltip_duoperfecto_agregado_uno, container, false)
            2 -> inflater.inflate(R.layout.tooltip_duoperfecto_felicidades, container, false)
            else -> inflater.inflate(R.layout.tooltip_duoperfecto_mensaje, container, false)
        }

    }

    override fun getTheme(): Int {
        return R.style.DelOrdersDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            var type = it.getInt("type")
            var mensaje  = it.getString(GlobalConstant.TOOL_TIPS_MESSAGE)

            when (type) {
                1->{
                    tvw_duo_agregaste_uno.text = mensaje
                    btn_agregaste_uno_duo.setOnClickListener {
                        dismiss()
                    }
                }
                2->{
                    tvw_completaste_duo.text = mensaje

                    btn_felicidades_agregaste_duo.setOnClickListener {
                        dismiss()
                    }
                }
                3 -> {
                    group_btn_eliminar.visibility = View.VISIBLE
                    btn_msj_ok_duo.visibility = View.GONE
                    tvw_message.text = GlobalConstant.ELIMINACION_DUO_PERFECTO

                    btn_eliminar_duo_no.setOnClickListener {
                        dismiss()
                    }
                    lnr_delete_duo.setOnClickListener {
                        dismiss()
                        listener?.onClickDeletePerfect()
                    }

                }
                4 -> {
                    group_btn_eliminar.visibility = View.GONE
                    btn_msj_ok_duo.visibility = View.VISIBLE
                    tvw_message.text = mensaje
                    btn_msj_ok_duo.setOnClickListener {
                        dismiss()
                    }
                }
            }
        }

    }

    companion object {
        fun newInstance(): ProductsToolTipMessage {
            return ProductsToolTipMessage()
        }
    }

    interface DeletePerfectDuo {
        fun onClickDeletePerfect()
    }

}
