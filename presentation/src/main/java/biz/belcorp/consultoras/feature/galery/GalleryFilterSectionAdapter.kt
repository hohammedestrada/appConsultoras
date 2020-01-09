package biz.belcorp.consultoras.feature.galery

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.item_gallery_filter_section_list.view.*

class GalleryFilterSectionAdapter(var listener : sectionListener) : RecyclerView.Adapter<GalleryFilterSectionAdapter.ViewHolder>() {

    private var context: Context? = null
    private var items: ArrayList<SectionGalleryFilterItemModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (context == null) context = parent.context

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gallery_filter_section_list, parent, false))
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items?.let { p ->
            holder.bind(p[position], position)
        }
    }

    fun setData(list: ArrayList<SectionGalleryFilterItemModel>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SectionGalleryFilterItemModel, position: Int) {
            itemView.apply {
                txtFilterSection.text = item.tab?.Descripcion ?: StringUtil.Empty

                rcwv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                rcwv.adapter = GalleryFilterChildAdapter(object : GalleryFilterChildAdapter.sectionFilterListener{
                    override fun onMarcarAnalytics(label: String) {
                        listener.onMarcarAnalytics(item.tab?.Descripcion?:StringUtil.Empty, label)
                    }
                })

                (rcwv.adapter as GalleryFilterChildAdapter).setData(item.children)

                head.setOnClickListener {
                    if(rcwv.visibility == View.VISIBLE){
                        rcwv.visibility = View.GONE
                    }else{
                        rcwv.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun selectedFilters(): ArrayList<FiltroGaleriaModel> {
        var selectedFilter = ArrayList<FiltroGaleriaModel>()
        selectedFilter.clear()

        items?.let {
            if (it.isEmpty()) {
                return selectedFilter
            }

            it.forEach { item ->
                item.children.forEach { subItem ->
                    if (subItem.EsSeleccionado) selectedFilter.add(subItem)
                }
            }
        }

        return selectedFilter
    }

    fun clean() {
        items?.forEach {item ->
            item.children.forEach {subItem ->
                subItem.EsSeleccionado = false
            }
        }

        notifyDataSetChanged()
    }

    interface sectionListener{
        fun onMarcarAnalytics(tipoFiltro : String, label: String)
    }
}
