package biz.belcorp.consultoras.feature.detalleprogramabrillante

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import kotlinx.android.synthetic.main.item_bright_path_level.view.*

class ProgramaBrillanteNivelesAdapter(private var niveles: ArrayList<NivelModel>) : RecyclerView.Adapter<ProgramaBrillanteNivelesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.item_bright_path_level, parent, false)))

    override fun getItemCount(): Int {
        return niveles?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(niveles[position])
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bind(nivel: NivelModel?) = with(itemView) {
            nivel?.let { nivel ->
                nivel.opciones?.let { opciones ->
                    if(opciones.size >= 1){
                        opciones[0].premios?.let { premios ->
                            if(premios.size >= 1){
                                vwBody.visibility = View.VISIBLE
                                tvwTitle.text ="${nivel.opciones[0].premios[0].descripcionPremio?: ""}:"
                                tvwMinimoCristal.text = "${resources.getString(R.string.puntaje_minimo)}: ${nivel.puntosNivel.toString()} ${resources.getString(R.string.puntos)}."
                            }
                        }
                    }
                }
            }
        }
    }
}
