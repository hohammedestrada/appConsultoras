package biz.belcorp.consultoras.feature.home.marquee

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import biz.belcorp.consultoras.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MarqueeAdapter(val items: List<MarqueeItem>, val context: Context) : RecyclerView.Adapter<MarqueeAdapter.ViewHolder>() {

    var onClickListener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.marquee_item, parent, false))
    }

    override fun getItemCount(): Int {
       return if (items.size == 1)  1 else (items.size * 2 + 1)
    }

    fun getRealItemsCount(): Int {
        return items.size
    }

    fun getMarqueeItem(position : Int) : MarqueeItem?{
        return items[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val newPosition =  if(items.size > 1) position % items.size else position


        holder.bind(newPosition,items[newPosition],onClickListener)
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(position:Int,item:MarqueeItem?,onClickListener: OnClickListener?){

            if(item?.image == null) {
                Glide.with(itemView.context).asBitmap().load(item?.urlImage).listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        (itemView as ImageView).setImageBitmap(resource)
                        return false
                    }

                }).submit()
            }else{
                (itemView as ImageView).setImageBitmap(item.image)
            }
            itemView.setOnClickListener {
                onClickListener?.onItemClick(position,item)
            }
        }
    }


    interface OnClickListener{
        fun onItemClick(position:Int,item: MarqueeItem?)
    }
}
