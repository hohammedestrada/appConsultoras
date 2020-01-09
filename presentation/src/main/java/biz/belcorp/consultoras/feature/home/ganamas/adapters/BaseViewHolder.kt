package biz.belcorp.consultoras.feature.home.ganamas.adapters

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(position: Int, item: T)
}
