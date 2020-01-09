package biz.belcorp.consultoras.common.dialog

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import java.util.ArrayList
import kotlinx.android.synthetic.main.item_number_phone.view.*

class CallCenterListAdapter
internal constructor(private val listener: Listener,
                     private val items: ArrayList<String>) : RecyclerView.Adapter<CallCenterListAdapter.ViewHolder>() {

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: String, listener: Listener) = with(itemView) {
            tvwNumberPhone?.text = item

            lltNumberPhone.setOnClickListener{
                val number = getOnlyNumber(item)
                listener.onCall(number)
            }
        }

        private fun getOnlyNumber(text: String): String {
            val regex = "\\D+".toRegex()
            return text.replace(regex,"")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_number_phone,  parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size


    /** */

    interface Listener {
        fun onCall(number: String)
    }

}
