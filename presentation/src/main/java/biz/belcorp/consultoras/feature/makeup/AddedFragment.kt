package biz.belcorp.consultoras.feature.makeup

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.data.entity.ProductoMasivo
import kotlinx.android.synthetic.main.fragment_summary_added.view.*
import java.text.DecimalFormat

class AddedFragment : Fragment() {

    private lateinit var dataAccepted: List<ProductoMasivo>
    private var summaryAdapter : SummaryAdapter? = null
    private var moneySymbol : String? = null
    private var decimalFormat: DecimalFormat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_summary_added, container, false)

        summaryAdapter = SummaryAdapter()
        summaryAdapter?.setData(moneySymbol, decimalFormat)
        view.rvw_added.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rvw_added.adapter = summaryAdapter


        loadData(dataAccepted)

        return view
    }

    fun loadData(list: List<ProductoMasivo>){
        summaryAdapter?.clearData()
        summaryAdapter?.setList(list)
    }

    fun setData(dataAccepted : List<ProductoMasivo>, moneySymbol: String?, decimalFormat: DecimalFormat?) {
        this.dataAccepted = dataAccepted
        this.moneySymbol = moneySymbol
        this.decimalFormat = decimalFormat
    }

}
