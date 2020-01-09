package biz.belcorp.consultoras.feature.home.addorders

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.fragment_tooltip_delete.*

class DelOrdersFragment : BottomSheetDialogFragment() {

    var listener: DeleteOrderFragmentListener? = null
    private lateinit var tvwProductNameText: Spanned

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tooltip_delete, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DelOrdersDialog;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvw_tooltip_name.text = tvwProductNameText
        btn_tooltip_no.setOnClickListener { dismiss() }
        btn_tooltip_detele.setOnClickListener {
            dismiss()
            listener?.onClickDeleteOrder()
        }
    }

    fun setOrderInfo(tvwProductNameText: String) {
        this.tvwProductNameText = Html.fromHtml(String.format("<b>%s</b><br />¿Deseas continuar?", tvwProductNameText))
    }

    interface DeleteOrderFragmentListener {
        fun onClickDeleteOrder()
    }

    companion object {
        fun newInstance(): DelOrdersFragment {
            return DelOrdersFragment()
        }
    }

}
