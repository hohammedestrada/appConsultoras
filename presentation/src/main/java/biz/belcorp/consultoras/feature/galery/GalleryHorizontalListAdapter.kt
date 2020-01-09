package biz.belcorp.consultoras.feature.galery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_gallery_horizontal_list.view.*

class GalleryHorizontalListAdapter(val listenerHorizontal: HorizontalGaleryListener) : RecyclerView.Adapter<GalleryHorizontalListAdapter.ViewHolder>() {

    private var context: Context? = null
    private var items: ArrayList<ListadoImagenModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (context == null) context = parent.context

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gallery_horizontal_list, parent, false))
    }

    override fun getItemCount(): Int{
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items?.let {
            holder.bind(position, it[position], it)
        }
    }

    fun setData(list: ArrayList<ListadoImagenModel>?) {
        items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position : Int,item : ListadoImagenModel, items: ArrayList<ListadoImagenModel>) {
            itemView.apply {
                Glide.with(context).load(item.urlImagenThumb).into(ivw_image)

                tvw_image_name.text = item.titulo

                vw_item.setOnClickListener {
                    listenerHorizontal.onItemSelected(position, items)
                }
            }
        }
    }

    interface HorizontalGaleryListener {
        fun onItemSelected(position : Int, items: ArrayList<ListadoImagenModel>?)
    }
}
