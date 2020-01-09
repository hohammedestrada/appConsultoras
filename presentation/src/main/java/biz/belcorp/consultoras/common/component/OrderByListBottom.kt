package biz.belcorp.consultoras.common.component

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.adapter.OrderByListAdapter
import biz.belcorp.consultoras.domain.entity.SearchOrderByResponse
import kotlinx.android.synthetic.main.view_order_by_list_bottom.*

class OrderByListBottom : BottomSheetDialogFragment(), OrderByListAdapter.Listener {


    private var adapter: OrderByListAdapter? = null
    private var parametros: List<SearchOrderByResponse?>? = null
    var listener : Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_order_by_list_bottom, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OrderByListAdapter(this)
        adapter?.setList(parametros)
        rvwList.layoutManager = GridLayoutManager(context, 1)
        rvwList.isNestedScrollingEnabled = false
        rvwList.adapter = adapter

    }

    fun setData(list: List<SearchOrderByResponse?>) {
        parametros = list
    }

    fun setDefaultChecked() {
        parametros?.forEach {
            if(it?.valor == DEFAULT_VALUE) it.seleccionado = true
        }
    }

    /** */
    override fun onUpdateOption(item: SearchOrderByResponse, position: Int) {
        item.let {
            adapter?.setSelected(position)
            listener?.setSortMethod(item, position)
        }
    }

    /** Listener */
    interface Listener{
        fun setSortMethod(orderByItem: SearchOrderByResponse, position: Int)
    }

    companion object {
        fun newInstance(): OrderByListBottom {
            return OrderByListBottom()
        }
        const val DEFAULT_VALUE = "ORDEN-ASC"
    }

}
