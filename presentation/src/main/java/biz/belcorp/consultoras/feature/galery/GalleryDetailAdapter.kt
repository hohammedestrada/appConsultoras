package biz.belcorp.consultoras.feature.galery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_detail_gallery_list.view.*

class GalleryDetailAdapter(var items: ArrayList<ListadoImagenModel>?) : RecyclerView.Adapter<GalleryDetailAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (context == null) context = parent.context

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_detail_gallery_list, parent, false))
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items?.let { l ->
            l[position].let { item ->
                holder.bind(item, position, items )
            }
        }
    }

    fun setData(list: ArrayList<ListadoImagenModel>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ListadoImagenModel, position : Int, items: ArrayList<ListadoImagenModel>?) {
            itemView.apply {
                Glide.with(context).load(item.urlImagenVisualiza).into(ivw)
            }
        }
    }
}
