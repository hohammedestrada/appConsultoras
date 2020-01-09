package biz.belcorp.consultoras.feature.galery

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.item_gallery_vertical_list.view.*

class GalleryVerticalListAdapter(l: VerticalGaleryListener) : RecyclerView.Adapter<GalleryVerticalListAdapter.ViewHolder>(), GalleryHorizontalListAdapter.HorizontalGaleryListener {

    private var context: Context? = null
    private var sectionGalleries: ArrayList<SectionGalleryItemModel>? = null
    private var filteredSectionGalleries: ArrayList<SectionGalleryItemModel>? = null
    private var listenerVertical: VerticalGaleryListener? = l

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (context == null) context = parent.context

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gallery_vertical_list, parent, false))
    }

    override fun getItemCount(): Int = filteredSectionGalleries?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        filteredSectionGalleries?.let { p ->
            holder.bind(p[position])
        }
    }

    fun setData(list: ArrayList<SectionGalleryItemModel>) {
        if (sectionGalleries == null) sectionGalleries = ArrayList<SectionGalleryItemModel>()
        if (filteredSectionGalleries == null) filteredSectionGalleries = ArrayList<SectionGalleryItemModel>()

        sectionGalleries?.clear()
        filteredSectionGalleries?.clear()

        sectionGalleries?.addAll(list)
        filteredSectionGalleries?.addAll(list)
        notifyDataSetChanged()
    }

    fun filter(filters: ArrayList<FiltroGaleriaModel>) {
        if (filters.isEmpty()) {
            sectionGalleries?.let {
                filteredSectionGalleries?.clear()
                filteredSectionGalleries?.addAll(it)

                notifyDataSetChanged()
            }
            return
        }

        if (filteredSectionGalleries == null) filteredSectionGalleries = ArrayList<SectionGalleryItemModel>()
        filteredSectionGalleries?.clear()

        sectionGalleries?.let { pgs ->
            for (filter in filters) {
                for (page in pgs) {
                    if (filter.Codigo.equals(page.tab?.Codigo)) {
                        filteredSectionGalleries?.add(page)
                    }
                }
            }

            notifyDataSetChanged()
        }
    }

    fun clean(){
        sectionGalleries?.let {
            filteredSectionGalleries?.clear()
            filteredSectionGalleries?.addAll(it)

            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(sectionGallery: SectionGalleryItemModel) {
            itemView.apply {
                txt.text = sectionGallery.tab?.Descripcion ?: ""

                rcvw.adapter = GalleryHorizontalListAdapter(this@GalleryVerticalListAdapter)
                rcvw.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                (rcvw.adapter as GalleryHorizontalListAdapter).setData(sectionGallery.files)
            }
        }
    }

    override fun onItemSelected(position: Int, items: ArrayList<ListadoImagenModel>?) {
        listenerVertical?.let {
            it.onItemSelected(position, items)
        }
    }

    interface VerticalGaleryListener {
        fun onItemSelected(position: Int, items: ArrayList<ListadoImagenModel>?)
    }
}
