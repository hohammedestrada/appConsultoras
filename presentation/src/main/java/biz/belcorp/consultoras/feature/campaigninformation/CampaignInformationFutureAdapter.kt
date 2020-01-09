package biz.belcorp.consultoras.feature.campaigninformation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.campaignInformation.InfoCampaignDetailModel
import kotlinx.android.synthetic.main.item_facturacion_campania_proximo.view.*

class CampaignInformationFutureAdapter(var items: ArrayList<InfoCampaignDetailModel>) : RecyclerView.Adapter<CampaignInformationFutureAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_facturacion_campania_proximo, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: InfoCampaignDetailModel) {
            item?.let {
                itemView.apply {
                    tvwCampania.text = it.Campania
                    tvwFechaFacturacion.text = it.FechaFacturacion
                    tvwInfoFechaPago.text = "${resources.getString(R.string.fecha_pago_pedido)}:"
                    tvwFecha.text = it.FechaPago
                }
            }
        }
    }
}
