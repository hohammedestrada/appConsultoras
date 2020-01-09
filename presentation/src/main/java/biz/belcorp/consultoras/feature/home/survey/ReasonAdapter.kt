package biz.belcorp.consultoras.feature.home.survey

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.survey.ReasonModel
import kotlinx.android.synthetic.main.item_reason.view.*
import javax.inject.Inject

class ReasonAdapter @Inject constructor(): RecyclerView.Adapter<ReasonAdapter.ReasonViewHolder>() {

    var list: MutableList<ReasonModel>? = mutableListOf()
    var onClickReason: OnClickReason? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reason, parent, false)
        return ReasonViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ReasonViewHolder, position: Int) {
        val reason = list?.let { it[position] }

        reason?.let {
            holder.itemView.reastonText.text = it.motivo
            holder.itemView.reastonText.setOnClickListener {
                onClickReason?.onClickReason(reason, holder.adapterPosition)
            }
            holder.itemView.reastonText.setBackgroundResource(if (it.isSelect)
                R.drawable.bg_reason_item_select else R.drawable.bg_reason_item_unselect)
            holder.itemView.reastonText.setTextColor(ContextCompat.getColor(holder.itemView.context,
                if (it.isSelect) R.color.brand_general else R.color.gray_item_unselect))
        }
    }

    inner class ReasonViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    interface OnClickReason {
        fun onClickReason(reason: ReasonModel, position : Int)
    }
}
