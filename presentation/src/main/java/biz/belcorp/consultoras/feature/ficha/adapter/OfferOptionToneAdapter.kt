package biz.belcorp.consultoras.feature.ficha.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.domain.entity.Componente
import biz.belcorp.consultoras.domain.entity.Opciones
import biz.belcorp.mobile.components.core.helpers.StylesHelper
import biz.belcorp.mobile.components.design.tone.model.ToneModel
import biz.belcorp.mobile.components.design.tone.section.ToneSection
import kotlinx.android.synthetic.main.item_ficha_section_component.view.*

class OfferOptionToneAdapter(
    val ofertaVencida: Boolean,
    val items : List<Componente?>,
    val strategyCode: String?,
    val context: Context?,
    val listener: OfferOptionTonesListener
) : RecyclerView.Adapter<OfferOptionToneAdapter.ViewHolder>(), SafeLet {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(context).inflate(R.layout.item_ficha_section_component, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    private fun showLineBar(position: Int): Boolean{
        if(items.size > position + 1){
            val element = items[position + 1] as Componente
            return !element.nombreMarca.isNullOrBlank()
        }
        return !(items.size == position + 1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = items[position] as Componente
        val stylesHelper= StylesHelper(holder.itemView.context)

        if(element.indicarFaltaSeleccion) holder.itemView.tswSection.setupFaltaSeleccionar()
        else holder.itemView.tswSection.setupBackground()

        if(showLineBar(position)){
            holder.itemView.vwComponentDivider.visibility =  View.VISIBLE
        }else{
            holder.itemView.vwComponentDivider.visibility =  View.GONE
        }

        if(!element.nombreMarca.isNullOrBlank()){
            holder.itemView.tvwMarca.text = element.nombreMarca
            holder.itemView.tvwMarca.visibility = View.VISIBLE
        }else{
            holder.itemView.tvwMarca.visibility = View.GONE
        }

        holder.itemView.tvwShowDetailName.typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.itemView.resources.getFont(stylesHelper.fontLatoBold)
        } else {
            ResourcesCompat.getFont(holder.itemView.context, stylesHelper.fontLatoBold)
        }

        if (items.size > 1 && (element.secciones?.isNotEmpty() == true || element.especificaciones?.isNotEmpty() == true ) )
            holder.itemView.lltContainerShowDetail.visibility = View.VISIBLE

        holder.itemView.lltContainerShowDetail.setOnClickListener {
            listener.didPressedShowDetail(element, holder.adapterPosition)
        }

        holder.itemView.tvwComponent.text = "x ${element.factorRepeticion!!*element.factorCuadre!!} ${element.nombreComercial}"

        element.opciones?.let { parent ->
            if(parent.isNotEmpty()){
                holder.itemView.tswSection.visibility = View.VISIBLE

                safeLet(element.factorCuadre, element.factorRepeticion) { factorCuadre, factorRepeticion ->
                    val factor = factorCuadre * factorRepeticion
                    holder.itemView.tswSection.setFactorCuadre(factor)
                    checkForSelectedItems(holder.itemView.tswSection, parent, factor)
                    holder.itemView.tswSection.setList(transformList(parent, factor))
                    if(ofertaVencida){
                        holder.itemView.tswSection.hideGrid(true)
                        holder.itemView.tswSection.hideButton()
                    }
                    if (itemCount > 1) {
                        holder.itemView.tswSection.hideGrid(true)
                    }
                }

                holder.itemView.tswSection.toneSectionListener = object : ToneSection.ToneSectionListener {

                    override fun onClickSectionOption(list: ArrayList<ToneModel>) {
                        context?.let {
                            if (holder.itemView.tswSection.getListSelected().size > 0)
                                listener.changeOption(element)
                            else
                                listener.chooseOption(element)
                        }
                    }

                    override fun onClickGridItem(item: ToneModel) {
                        cleanItem(holder.adapterPosition)

                        val opciones = items[holder.adapterPosition]?.opciones

                        val opcion = opciones?.firstOrNull { it?.cuv == item.key }?.apply {
                            selected = true
                            cantidad = 1
                        }

                        val isComplete = isCompleteSelected(items)
                        listener.completeTones(isComplete)
                        opcion?.let {
                            listener.selectOption(element, it)
                        }
                    }

                    override fun onClickDialogItem(item: ToneModel) {
                        items[holder.adapterPosition]?.opciones?.first { opt -> opt?.cuv == item.key }?.let { op ->
                            listener.selectOption(op)
                        }
                    }

                    override fun onCompleteDialogTone(tones: ArrayList<ToneModel>) {
                       cleanItem(holder.adapterPosition)
                        tones.map { it.enable = false }

                        tones.toSet().forEach { parent ->
                            items[holder.adapterPosition]?.opciones?.firstOrNull { opt -> opt?.cuv == parent.key }?.apply {
                                selected = parent.quantity > 0
                                cantidad = parent.quantity
                            }
                        }

                        val componente = items[holder.adapterPosition]
                        componente?.indicarFaltaSeleccion = false
                        componente?.selected = true
                        notifyDataSetChanged()

                        val isComplete= isCompleteSelected(items)
                        if(isComplete) listener.completeTones(isComplete)
                        listener.applySelection(element)
                    }

                    override fun onCloseDialog() { }

                }
            }
        }
    }

    private fun checkForSelectedItems(section: ToneSection, items: List<Opciones?>, factor: Int) {
        val opciones = items.filterNotNull()
        val seleccionados = arrayListOf<Opciones>()

        opciones.forEach { opcion -> if (opcion.selected){
            for(i in 1..opcion.cantidad)
                seleccionados.add(opcion.clone() as Opciones)
        }

        }

        if (seleccionados.isNotEmpty())
            section.setListSelected(transformList(seleccionados, factor))

    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view)

    fun cleanItem(position: Int) {
        items[position]?.opciones?.forEach { opt ->
            opt?.selected = false
            opt?.cantidad = 0
        }
    }

    fun getTonesSelected() : List<Componente> {
        val tones = ArrayList<Componente>()
        items.forEach { comp ->
            val tone = comp?.opciones?.filter { it?.cantidad ?: 0 > 0 && it?.selected ?: false }
            if (tone != null && tone.isNotEmpty()) {
                tones.add(comp.copy(opciones = tone))
            }
        }
        return tones
    }

    private fun transformList(list: List<Opciones?>?, factor: Int): ArrayList<ToneModel>{
        return arrayListOf<ToneModel>().apply {
            list?.forEach {
                it?.let { it1 ->
                    safeLet(it1.cuv, it1.nombreOpcion, it.selected, it.cantidad) {_, _, _, _ ->
                        transform(it1, factor)?.let { it2 -> add(it2) }
                    }
                }
            }
        }
    }

    private fun transform(item: Opciones, factor: Int): ToneModel {
        return ToneModel(
            key = item.cuv!!,
            name = item.nombreOpcion!!,
            urlImage = item.imagenURL,
            selected = item.selected,
            quantity = item.cantidad,
            soldOut = item.agotado ?: false,
            total = factor
        )
    }

    private fun isCompleteSelected(list: List<Componente?>?): Boolean{
        val isCompleteSelected = mutableSetOf<Boolean>()

        list?.forEach {
            val factorCuadre = ((it?.factorCuadre ?: 0)*(it?.factorRepeticion ?: 0))
            var acum = 0
            it?.let { parent ->
                if(it.opciones?.size ?: 0 > 0){
                    parent.opciones?.forEach {
                        it?.let {
                            if(it.cantidad > 0){
                                acum += it.cantidad
                            }
                        }
                    }

                    if(acum == factorCuadre)
                        isCompleteSelected.add(true)
                    else
                        isCompleteSelected.add(false)
                }
            }

        }
        return isCompleteSelected.all { it }
    }

}

interface OfferOptionTonesListener {
    fun completeTones(complete: Boolean)
    fun selectOption(item: Componente, option: Opciones)
    fun changeOption(item: Componente)
    fun chooseOption(item: Componente)
    fun selectOption(item: Opciones)
    fun applySelection(item: Componente)
    fun didPressedShowDetail(item: Componente, position: Int)
}

