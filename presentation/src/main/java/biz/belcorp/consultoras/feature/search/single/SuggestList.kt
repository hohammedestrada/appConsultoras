package biz.belcorp.consultoras.feature.search.single

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.ProductCUV
import kotlinx.android.synthetic.main.view_suggest_list.view.*
import java.text.DecimalFormat

class SuggestList : LinearLayout, SuggestListAdapter.Listener {

    var listener: SuggestListener? = null

    private lateinit var listAdapter: SuggestListAdapter

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    fun init(context: Context, attributeSet: AttributeSet?) {
        View.inflate(getContext(), R.layout.view_suggest_list, this)
        rvwSuggest.isNestedScrollingEnabled = false

        listAdapter = SuggestListAdapter(getContext(), this)

        rvwSuggest.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
            false)
        rvwSuggest.adapter = listAdapter
    }


    fun setData(type: Int, list: Collection<ProductCUV?>?) {
        listAdapter.setList(type, list)
    }

    fun setAdded(cuv:String){
        listAdapter.setAdded(cuv)
    }

    fun setDecimalFormat(decimalFormat: DecimalFormat, moneySymbol: String) {
        listAdapter.setFormat(decimalFormat, moneySymbol)
    }

    fun onProductAdded(item: ProductCUV, position: Int) {
        listAdapter.updateItemList(item, position)
    }

    override fun onAddItem(type: Int, productCUV: ProductCUV) {
        listener?.addSuggestOffer(type, productCUV)
    }

    interface SuggestListener {
        fun addSuggestOffer(type: Int, productCUV: ProductCUV)
    }
}
