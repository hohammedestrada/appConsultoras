package biz.belcorp.consultoras.feature.ofertafinal.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.domain.entity.PremioFinal
import biz.belcorp.mobile.components.offers.PremioMulti
import kotlinx.android.synthetic.main.item_premio_final.view.*

class PremioFinalAdapter(var listener: OnItemClickListener): RecyclerView.Adapter<PremioFinalAdapter.PremioFinalViewHolder>() {
    val items: MutableList<PremioFinal> = mutableListOf()


    var context:Context?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremioFinalViewHolder {

        this.context=parent.context
        return PremioFinalViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_premio_final, parent, false))

    }

    override fun onBindViewHolder(holder: PremioFinalViewHolder, position: Int) {
        val item = items[position]

        context?.let {
            holder.bind(item, listener, it)
        }


    }

    override fun getItemCount(): Int =  items.size

    fun updateData(newData: List<PremioFinal?>?) {
        items.clear()
        newData?.filterNotNull()?.let { items.addAll(it) }
        notifyDataSetChanged()
    }

    class PremioFinalViewHolder(view: View): RecyclerView.ViewHolder(view), SafeLet{
        val premioMulti = view.premioMulti


        fun bind(item: PremioFinal, listener:OnItemClickListener, context:Context){
            safeLet(item.nombre, item.imagen){nombre, imagen ->
                premioMulti.setValues(nombre, imagen)
            }

            premioMulti.updateIconImage(R.drawable.ic_candado_of)

            val isEnabled = item.habilitado ?: false
            val isSelected = item.seleccionado ?: false

            premioMulti.setDisableAdd(!isEnabled)
            premioMulti.showIconImage(!isEnabled)
            premioMulti.setSelectedItem(isSelected && isEnabled)

            premioMulti.premioListener = object : PremioMulti.OnPremioMultiListener {
                override fun onClick(view: View) {
                    listener.onClick(item, view)
                }

                override fun onClickAdd() {
                    listener.onClickAdd(item)
                }
            }
        }
    }


    interface OnItemClickListener {
        fun onClick(premio: PremioFinal, v: View)
        fun onClickAdd(premio: PremioFinal)
    }


}

