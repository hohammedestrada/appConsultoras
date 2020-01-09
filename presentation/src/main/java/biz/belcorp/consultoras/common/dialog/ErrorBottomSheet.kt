package biz.belcorp.consultoras.common.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.error_bottom_tooltip.*

class ErrorBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(texto: String): ErrorBottomSheet{
          return ErrorBottomSheet().apply {
              this.arguments = Bundle().apply {
                  putString(GlobalConstant.ERROR_BOTTONSHEET, texto)
              }
          }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.error_bottom_tooltip, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DelOrdersDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_tooltip_close.setOnClickListener {
            dismiss()
        }
        tvw_tooltip_limite.text =  arguments!!.getString(GlobalConstant.ERROR_BOTTONSHEET)
    }
}
