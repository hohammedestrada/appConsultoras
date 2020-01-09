package biz.belcorp.consultoras.common.component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.SearchFilter
import biz.belcorp.consultoras.domain.entity.SearchFilterChild
import kotlinx.android.synthetic.main.view_filter.view.*

class FilterLayout : LinearLayout {

    var listener: FilterLayout.FilterListener? = null
    internal var adapter: FilterAdapter? = null
    private var filterList: MutableList<SearchFilter?> = mutableListOf()
    lateinit var viewInflated: View
    lateinit var onFilterClick: OnFilterClick

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    fun init(context: Context, attributeSet: AttributeSet?) {
        viewInflated = View.inflate(getContext(), R.layout.view_filter, this)
        startListeners()
    }

    // Private Functions
    private fun startListeners() {

        lnl_close.setOnClickListener {
            adapter?.setFilterList(copyFilterList(filterList))
            listener?.onClose()
        }
        btn_aplicar.setOnClickListener {
            listener?.onApply()
        }
        btn_limpiar.setOnClickListener {
            onFilterClick.onFilterClear(btn_limpiar.text.toString())
            adapter?.cleanFilterList()
        }
    }


    fun setFilters(filters: List<SearchFilter?>) {
        // Copia la Lista y la Lista dentro de ella...
        filterList = copyFilterList(filters)
        adapter = FilterAdapter(context, filters.toMutableList())
        elv_filtros.setAdapter(adapter)
        for (i in 0 until filters.size) {
            elv_filtros.expandGroup(i)
        }


    }

    fun setResultsSize(total: Int) {
        txt_total.text = "$total resultados"
    }

    fun getFilters(): List<SearchFilter?>? {
        return adapter?.getFilterList()
    }

    private fun copyFilterList(filters: List<SearchFilter?>): MutableList<SearchFilter?> {
        val copy: MutableList<SearchFilter?> = mutableListOf()
        filters.forEach { filter ->
            val copied = filter?.copy()
            val copyChilds: MutableList<SearchFilterChild?> = mutableListOf()
            filter?.opciones?.forEach {
                copyChilds.add(it?.copy())

            }
            copied?.opciones = copyChilds
            copy.add(copied)
        }
        return copy
    }

    // Listeners
    interface FilterListener {
        fun onApply()
        fun onClose()
    }

    // Adapatador para la lista de filtros
    inner class FilterAdapter(var context: Context, private var filters: MutableList<SearchFilter?>) : BaseExpandableListAdapter() {

        override fun getGroup(groupPosition: Int): SearchFilter {
            return filters[groupPosition]!!
        }

        override fun isChildSelectable(p0: Int, p1: Int): Boolean {
            return true
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, p3: ViewGroup?): View? {
            var convertView = convertView
            if (convertView == null) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.item_filter_group, null)
            }

            val txtGroup = convertView?.findViewById<TextView>(R.id.txt_group)
            val imgIndicator = convertView?.findViewById<ImageView>(R.id.img_indicator)


            txtGroup?.text = getGroup(groupPosition).nombreGrupo

            if (isExpanded) {
                imgIndicator?.setImageResource(R.drawable.ic_arrow_up)
            } else {
                imgIndicator?.setImageResource(R.drawable.ic_arrow_down)
            }

            if (elv_filtros.height < lltFiltros.height) {
                bottom_line.visibility = View.GONE
            } else {
                bottom_line.visibility = View.VISIBLE
            }

            return convertView
        }

        override fun getChildrenCount(groupPosition: Int): Int {
            return filters[groupPosition]?.opciones?.size ?: 0
        }

        override fun getChild(groupPosition: Int, childPosition: Int): SearchFilterChild? {
            return filters[groupPosition]?.opciones!![childPosition]
        }

        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        @SuppressLint("SetTextI18n")
        override fun getChildView(groupPosition: Int, childPosition: Int, p2: Boolean, convertView: View?, p4: ViewGroup?): View? {

            var convertView = convertView

            if (convertView == null) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.item_filter_child, null)
            }

            val item = getChild(groupPosition, childPosition)
            item?.let { filter ->

                val chkChild = convertView?.findViewById<CheckBox>(R.id.chk_child)
                chkChild?.setOnCheckedChangeListener(null)
                chkChild?.text = filter.nombreFiltro
                chkChild?.isChecked = filter.marcado ?: false

                chkChild?.setOnClickListener {
                    filter.marcado = chkChild.isChecked
                }

                chkChild?.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        onFilterClick.onFilterSelected(filter.nombreFiltro
                            ?: "", getGroup(groupPosition).nombreGrupo ?: "")
                    }
                }

            } ?: kotlin.run { convertView?.visibility = View.GONE }

            return convertView
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return childPosition.toLong()
        }

        override fun getGroupCount(): Int {
            return filters.size
        }

        fun getFilterList(): List<SearchFilter?>? {
            return filters
        }

        fun setFilterList(filters: MutableList<SearchFilter?>) {
            this.filters = filters
            notifyDataSetChanged()
        }

        fun cleanFilterList() {
            this.filters.forEach { filter ->
                filter?.opciones?.forEach {
                    it?.marcado = false
                }
            }
            notifyDataSetChanged()
        }

    }

    interface OnFilterClick {
        fun onFilterSelected(filterName: String?, filterAction: String?)
        fun onFilterClear(buttonName: String)
    }

}
