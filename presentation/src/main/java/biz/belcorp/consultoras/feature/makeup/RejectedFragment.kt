package biz.belcorp.consultoras.feature.makeup


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.data.entity.ProductoMasivo
import kotlinx.android.synthetic.main.fragment_summary_rejected.view.*
import java.text.DecimalFormat

class RejectedFragment : Fragment() {

    private lateinit var dataRejected: List<ProductoMasivo>
    private var summaryAdapter : SummaryAdapter? = null
    private var moneySymbol : String? = ""
    private var decimalFormat: DecimalFormat? = DecimalFormat()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_summary_rejected, container, false)

        summaryAdapter = SummaryAdapter()
        summaryAdapter?.setData(moneySymbol, decimalFormat)
        view.rvw_rejected.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rvw_rejected.adapter = summaryAdapter

        loadData(dataRejected)

        return view
    }

    fun loadData(list: List<ProductoMasivo>){
        summaryAdapter?.clearData()
        summaryAdapter?.setList(list)
    }

    fun setData(dataRejected : List<ProductoMasivo>, moneySymbol: String?, decimalFormat: DecimalFormat?) {
        this.dataRejected = dataRejected
        this.moneySymbol = moneySymbol
        this.decimalFormat = decimalFormat
    }

}
