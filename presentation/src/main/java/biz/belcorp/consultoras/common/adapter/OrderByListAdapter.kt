package biz.belcorp.consultoras.common.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.SearchOrderByResponse
import java.util.ArrayList
import kotlinx.android.synthetic.main.item_text_simple.view.*

class OrderByListAdapter
constructor(private val listener: Listener) : RecyclerView.Adapter<OrderByListAdapter.ViewHolder>() {

    private val orderByList = ArrayList<SearchOrderByResponse>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SearchOrderByResponse, position: Int, listener: Listener) = with(itemView) {

            tvwItem.text = item.descripcion

            if (item.seleccionado == true) {
                ivwRight.visibility = View.VISIBLE
                tvwItem.setTypeface(null, Typeface.BOLD)
            } else {
                tvwItem.setTypeface(null, Typeface.NORMAL)
                ivwRight.visibility = View.GONE
            }

            rltItem.setOnClickListener{
                listener.onUpdateOption(item, position)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): OrderByListAdapter.ViewHolder {

        return OrderByListAdapter.ViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_text_simple, viewGroup, false))

    }

    override fun onBindViewHolder(holder: OrderByListAdapter.ViewHolder, position: Int) =
        holder.bind(orderByList[position], position, listener)

    override fun getItemCount(): Int = orderByList.size

    /** Public Functions */
    fun setList(list: List<SearchOrderByResponse?>?) {
        orderByList.clear()
        list?.filterNotNull()?.let { orderByList.addAll(it) }
        notifyDataSetChanged()
    }

    fun setSelected(position: Int){
        orderByList.forEach { it.seleccionado = false }
        orderByList[position].seleccionado = true
    }

    /** Listener */
    interface Listener {
        fun onUpdateOption(item: SearchOrderByResponse, position: Int)
    }

}
