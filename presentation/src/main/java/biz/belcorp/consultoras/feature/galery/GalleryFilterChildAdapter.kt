package biz.belcorp.consultoras.feature.galery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.item_gallery_filter_list.view.*

class GalleryFilterChildAdapter(var listener : sectionFilterListener) : RecyclerView.Adapter<GalleryFilterChildAdapter.ViewHolder>() {

    private var context: Context? = null
    private var items: ArrayList<FiltroGaleriaModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (context == null) context = parent.context

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gallery_filter_list, parent, false))
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items?.let { p ->
            holder.bind(p[position], position)
        }
    }

    fun setData(list: ArrayList<FiltroGaleriaModel>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FiltroGaleriaModel, position: Int) {
            itemView.apply {
                chx.text = item.Descripcion

                chx.setOnCheckedChangeListener { compoundButton, boolean ->
                    items?.get(position)?.EsSeleccionado = boolean

                    if(boolean) listener.onMarcarAnalytics(item.Descripcion)
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
                if (item.EsSeleccionado) {
                    selectedFilter.add(item)
                }
            }
        }

        return selectedFilter
    }

    fun clean() {
        items?.forEach {
            it.EsSeleccionado = false
        }

        notifyDataSetChanged()
    }

    interface sectionFilterListener{
        fun onMarcarAnalytics(label : String)
    }
}
