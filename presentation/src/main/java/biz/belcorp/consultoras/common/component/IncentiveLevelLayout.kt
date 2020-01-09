package biz.belcorp.consultoras.common.component

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.SearchFilter
import biz.belcorp.consultoras.domain.entity.SearchFilterChild
import kotlinx.android.synthetic.main.item_chips.view.*
import kotlinx.android.synthetic.main.view_chips.view.*
import android.support.v7.widget.LinearLayoutManager



class IncentiveLevelLayout : LinearLayout {

    var chipsList = mutableListOf<SearchFilterChild>()
    var listener: IncentiveLevelLayout.ChipsListener? = null
    var adapter: ChipsAdapter? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    fun init(context: Context, attributeSet: AttributeSet?) {
        View.inflate(getContext(), R.layout.view_chips, this)
    }

    // Public Functions
    fun setFilters(filters: List<SearchFilter?>){

        chipsList.clear()
        filters.filterNotNull().forEach{ filters ->
            filters.opciones?.asSequence()?.filterNotNull()?.filter { it.marcado == true }?.toList()?.forEach{
                chipsList.add(it)
            }
        }
        adapter = IncentiveLevelLayout.ChipsAdapter(chipsList, listener)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcv_chips.layoutManager = layoutManager
        rcv_chips.adapter = adapter
    }

    // Listeners
    interface ChipsListener{
        fun onChipDeleted(chip:SearchFilterChild)
    }


    // Adaptador para los Chips
    class ChipsAdapter constructor(private var filterList : List<SearchFilterChild>, private val listener: ChipsListener?) : RecyclerView.Adapter<ChipsAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("SetTextI18n")
            fun bind(item: SearchFilterChild, listener: ChipsListener?) = with(itemView) {
                txt_chips.text = item.nombreFiltro
                lnl_delete.setOnClickListener{
                    listener?.onChipDeleted(item)
                }
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ChipsAdapter.ViewHolder {
            return ChipsAdapter.ViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_chips, viewGroup, false))
        }

        override fun onBindViewHolder(holder: ChipsAdapter.ViewHolder, position: Int) = holder.bind(filterList[position], listener)
        override fun getItemCount(): Int = filterList.size

    }

}
