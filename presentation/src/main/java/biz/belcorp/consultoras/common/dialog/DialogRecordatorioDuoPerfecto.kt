package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_recordatorio_duo_perfecto.*

class DialogRecordatorioDuoPerfecto(private var contexto: Context, private var cantidadAgregado: Int, private var listener: RecordatorioDuoListener) : Dialog(contexto, R.style.full_screen_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_recordatorio_duo_perfecto)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setGravity(Gravity.CENTER)

        if (cantidadAgregado == 1) {
            dialogCheckSelected.visibility = View.VISIBLE
            tvwDlgDuoPerfectoTitle.text = contexto.getText(R.string.dialog_duo_perfecto_falta)
        }

        dlgClose.setOnClickListener {
            dismiss()
        }
        tvwLoHareLuego.setOnClickListener {
            dismiss()
        }

        btnVerProductos.setOnClickListener {
            listener.onGoToProducts()
            dismiss()
        }

    }

    interface RecordatorioDuoListener{
        fun onGoToProducts()
    }

}
