package biz.belcorp.consultoras.feature.orders

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.component.CustomTypefaceSpan
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.util.KeyboardUtil
import kotlinx.android.synthetic.main.item_order_product_list.view.*
import kotlinx.android.synthetic.main.tooltip_backorder.view.*
import kotlinx.android.synthetic.main.tooltip_error.view.*
import kotlinx.android.synthetic.main.tooltip_info.view.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Toast
import biz.belcorp.consultoras.common.dialog.InfoListFullDialog
import biz.belcorp.consultoras.util.anotation.FestType
import biz.belcorp.consultoras.util.anotation.OrderItemTag
import biz.belcorp.consultoras.common.dialog.InfoListFullObsDialog
import biz.belcorp.consultoras.common.model.promotion.PromotionOfferModel
import kotlin.collections.ArrayList


/**
 * @author andres.escobar on 25/04/2017.
 */
class OrdersListAdapter
constructor(
    private val listener: OrderListener
) : RecyclerView.Adapter<OrdersListAdapter.ViewHolder>() {

    private val mList = ArrayList<ProductItem>()
    private var isReservation: Boolean = false
    private var moneySymbol: String = ""
    private var consultantName: String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var type: Int = 0
    private var montogift: String? = null
    private var precioRegalo: Boolean? = false
    private var listItemTag: Map<Int, String?> = mutableMapOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ProductItem, listener: OrderListener, decimalFormat: DecimalFormat,
                 moneySymbol: String, isReservation: Boolean, type: Int, position: Int, size: Int, montogift: String?, precioRegalo: Boolean?, listItemTag: Map<Int, String?>) = with(itemView) {

            lltInfo.visibility = View.GONE

            val tRegular = Typeface.createFromAsset(context.assets, GlobalConstant.LATO_REGULAR_SOURCE)
            val tfBold = Typeface.createFromAsset(context.assets, GlobalConstant.LATO_BOLD_SOURCE)


            val descriptionSpannable = Spannable.Factory.getInstance().newSpannable(item.descripcionCortaProd)
            descriptionSpannable.setSpan(CustomTypefaceSpan(tRegular), 0, item.descripcionCortaProd!!.length, 0)

            val spannableString = SpannableStringBuilder()
            spannableString.append(descriptionSpannable)

            if (item.isEsPremioElectivo!!) {

                if (!montogift.isNullOrEmpty()) {

                    tvwSiLLegas.visibility = View.VISIBLE
                    tvwSiLLegas.setTextColor(context.resources.getColor(R.color.black))
                    tvwSiLLegas.text = context.getText(R.string.soloSiLLegasA).toString() + " " + moneySymbol + " " + montogift
                } else {
                    tvwSiLLegas.visibility = View.GONE
                }

                val obsPROL: String? = if (item.observacionPROLList?.size ?: 0 > 0) item.observacionPROLList?.get(0) else null
                if (obsPROL.isNullOrEmpty()) {
                    ivwDelete.isEnabled = false
                    ImageViewCompat.setImageTintList(ivwDelete, ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.disabled)))
                } else {
                    ivwDelete.isEnabled = true
                    ImageViewCompat.setImageTintList(ivwDelete, ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.black)))
                }


            } else {
                tvwSiLLegas.visibility = View.GONE
                ivwDelete.isEnabled = true
                ImageViewCompat.setImageTintList(ivwDelete, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.black)))
            }
            item.isEsKitNueva?.let {
                when {
                    it -> {
                        item.isDeleteKit?.let { eliminar ->
                            showDelete(eliminar)
                        } ?: showDelete(false)
                    }
                    isReservation -> ivwDelete.visibility = View.GONE
                    else -> {
                        showDelete(true)
                    }
                }
            }

            ivwInfo.setOnClickListener {
                tvwMensajeInfo.text = if (item.isDeleteKit != null && item.isDeleteKit == true && item.isEsKitNueva == true) {
                    context.getText(R.string.kit_inicio_info)
                } else if (item.isDeleteKit != null && item.isDeleteKit == false && item.isEsKitNueva == true) {
                    context.getText(R.string.socia_info)
                } else {
                    context.getText(R.string.kit_inicio_info)
                }

                if (lltInfo.visibility == View.GONE) {
                    lltInfo.visibility = View.VISIBLE
                } else {
                    lltInfo.visibility = View.GONE
                }
            }

            showHideObservation(item, tfBold,  listener )

            item.etiquetaProducto?.let {
                when {
                    item.isEsDuoPerfecto == true -> {
                        tvwTitle.text = context.getString(R.string.duo_perfecto)
                        tvwTitle.visibility = View.VISIBLE
                    }
                    it.isEmpty() -> tvwTitle.visibility = View.GONE
                    else -> {
                        if (item.isEsDuoPerfecto == true) {
                            tvwTitle.text = context.getText(R.string.duoPerfectoTag)
                        } else {
                            if (item.isEsPremioElectivo == true)
                                if (precioRegalo == true) {
                                    tvwTitle.text = context.getText(R.string.tagProdKitIni)
                                } else {
                                    tvwTitle.text = context.getText(R.string.tagProgNueva)
                                }
                            else {
                                if (item.flagFestival == FestType.FEST_PREMIO) {
                                    val tagFestItem = listItemTag[OrderItemTag.TAG_FEST]
                                        ?: itemView.context.getString(R.string.tagFestival)
                                    tvwTitle.text = String.format(context.getString(R.string.fest_tag_order), tagFestItem.toUpperCase())
                                }else {
                                    if (item.isPromocion == true) {
                                        tvwTitle.text = it + " ${context.resources.getString(R.string.add_order_promocion)}"
                                    } else {
                                        tvwTitle.text = it
                                    }
                                }
                            }
                        }
                        tvwTitle.visibility = View.VISIBLE
                    }
                }

            } ?: run {
                tvwTitle.visibility = View.GONE
            }

            if (item.isArmaTuPack == true) {
                tvwCuv.visibility = View.GONE
                tvwDetalle.text = item.descripcionCortaProd
                    ?: context.getText(R.string.arma_tu_pack)
            } else {
                tvwCuv.text = item.cuv!!
                tvwCuv.visibility = View.VISIBLE
                tvwDetalle.text = item.descripcionCortaProd
            }

            tvwPrice.text = "$moneySymbol ${decimalFormat.format(item.precioUnidad)}"
            tvwPriceSubtotal.text = "$moneySymbol ${decimalFormat.format(
                item.precioUnidad?.multiply(BigDecimal(item.cantidad.toString())))}"
            tvwQuantity.text = item.cantidad?.toString()

            when (type) {
                TYPE_PRODUCT -> {
                    tvwClient?.visibility = View.VISIBLE
                    if (item.clienteID == 0 || item.isEsPremioElectivo == true) {
                        tvwClient?.text = "Para mí"
                    } else {
                        item.nombreCliente?.let {

                            val client = Spannable.Factory.getInstance().newSpannable(it)
                            client.setSpan(CustomTypefaceSpan(tfBold), 0, it.length, 0)

                            val spannableClientString = SpannableStringBuilder()
                            spannableClientString.append("Cliente: ")
                            spannableClientString.append(client)

                            tvwClient?.text = spannableClientString
                        }
                    }
                }
                TYPE_CLIENT -> {
                    tvwClient?.visibility = View.GONE
                    if (position == size) {
                        separator.visibility = View.GONE
                    }
                }
                else -> {
                    tvwClient?.visibility = View.GONE
                }
            }

            rltOrders.setOnClickListener {
                var view = rltOrders.rootView
                if (view == null) view = View(context)
                KeyboardUtil.dismissKeyboard(context, view)
            }

            val kit = item.isEsKitNueva ?: false
            val duoPerfecto = item.isEsDuoPerfecto ?: false
            val flagNueva = item.isFlagNueva ?: false
            val caminoBrillante = item.isKitCaminoBrillante ?: false
            val festival = item.isFestival ?: false
            //val promocion = item.isPromocion ?: false

            //if (!kit && !duoPerfecto && !flagNueva && !caminoBrillante && !festival && !promocion) {
            if (!kit && !duoPerfecto && !flagNueva && !caminoBrillante && !festival) {

                tvwQuantity.setTextColor(resources.getColor(R.color.black))
                rltOrders.setOnClickListener {
                    listener.onEdit(item)
                }

            } else {
                tvwQuantity.setTextColor(resources.getColor(R.color.disabled))
            }

            ivwDelete.setOnClickListener {
                listener.onDelete(item)
            }

            btnBackorder.setOnClickListener {
                listener.onBackorder(item)
            }

            btnBackorderNo.setOnClickListener {
                lltBackorderDelete.visibility = View.GONE
                listener.onDelete(item)
            }

        }

        private fun showHideObservation(item: ProductItem, tfBold: Typeface,  listener: OrderListener) = with(itemView) {

            if (item.isEsBackOrder == true) {
                if (item.isAceptoBackOrder == true) {
                    lltObservation.visibility = View.VISIBLE
                    tvwMensajeError.text = GlobalConstant.ENTREGA_PROXIMA_CAMPANNA
                    lltBackorderDelete.visibility = View.GONE
                } else {
                    lltObservation.visibility = View.GONE
                    lltBackorderDelete.visibility = View.VISIBLE
                }
            } else {
                lltBackorderDelete.visibility = View.GONE
                if (item.observacionPROLType == 0) {
                    item.mensajeError?.let {
                        if (!it.isEmpty()) {
                            lltObservation.visibility = View.VISIBLE
                            tvwMensajeError.text = it
                        } else {
                            item.observacionPROL?.let {
                                if (!it.isEmpty()) {
                                    lltObservation.visibility = View.VISIBLE
                                    tvwMensajeError.text = it
                                } else lltObservation.visibility = View.GONE
                            } ?: run { lltObservation.visibility = View.GONE }
                        }
                    } ?: run {
                        item.observacionPROL?.let {
                            if (!it.isEmpty()) {
                                lltObservation.visibility = View.VISIBLE
                                tvwMensajeError.text = it
                            } else lltObservation.visibility = View.GONE
                        } ?: run { lltObservation.visibility = View.GONE }
                    }
                } else if ((item.isPromocion ?: false) && (item.observacionPromociones?.any()?: false)) {

                    item.observacionPromociones?.let { list ->


                        lltObservation.visibility = View.VISIBLE

                        val link = Spannable.Factory.getInstance().newSpannable(context.resources.getString(R.string.pedido_text_obs_look_details))
                        link.setSpan(UnderlineSpan(), 0, link.length, 0)
                        link.setSpan(CustomTypefaceSpan(tfBold), 0, link.length, 0)
                        val spannableString = SpannableStringBuilder()
                        spannableString.append(context.resources.getString(R.string.pedido_text_obs_product))
                        spannableString.append(link)

                        tvwMensajeError.text = spannableString
                        tvwMensajeError.setOnClickListener {
                            InfoListFullObsDialog.Builder(context!!)
                                .withList(list)
                                .withCallback {

                                    listener?.loadPromotion(item.cuv)

                                }
                                .show()

                        }

                    }
                }else{
                    item.observacionPROLList?.let { list ->
                        if(list.isNotEmpty()){
                            if (list.size > 1) {
                                val link = Spannable.Factory.getInstance().newSpannable(context.resources.getString(R.string.pedido_text_obs_look_details))
                                link.setSpan(UnderlineSpan(), 0, link.length, 0)
                                link.setSpan(CustomTypefaceSpan(tfBold), 0, link.length, 0)
                                val spannableString = SpannableStringBuilder()
                                spannableString.append(context.resources.getString(R.string.pedido_text_obs_product))
                                spannableString.append(link)

                                tvwMensajeError.text = spannableString
                                tvwMensajeError.setOnClickListener {
                                    InfoListFullDialog.Builder(context!!)
                                        .withList(list)
                                        .show()
                                }
                            } else {
                                tvwMensajeError.text = list[0]
                            }
                            lltObservation.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        private fun showDelete(showDelete: Boolean) = with(itemView) {
            if (showDelete) {
                ivwDelete.visibility = View.VISIBLE
                ivwInfo.visibility = View.GONE
            } else {
                ivwDelete.visibility = View.GONE
                ivwInfo.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return when {
            consultantName.isEmpty() ->
                return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_order_product_list, viewGroup, false))
            else ->
                ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_order_client_list, viewGroup, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(mList[position], listener, decimalFormat, moneySymbol, isReservation, type, position, mList.size, montogift, precioRegalo, listItemTag)

    override fun getItemCount(): Int = mList.size

    fun setList(list: List<ProductItem?>?) {
        mList.clear()
        list?.filterNotNull()?.let {
            mList.addAll(it)
        }
        notifyDataSetChanged()
    }


    fun setFormat(decimalFormat: DecimalFormat, moneySymbol: String, consultantName: String, isReservation: Boolean, type: Int) {
        this.decimalFormat = decimalFormat
        this.moneySymbol = moneySymbol
        this.consultantName = consultantName
        this.isReservation = isReservation
        this.type = type
        notifyDataSetChanged()
    }

    fun setMoney(moneySymbol: String, monto: Double) {
        this.montogift = if (monto == 0.00) "" else decimalFormat.format(monto)
        this.moneySymbol = moneySymbol
        notifyDataSetChanged()
    }

    fun setPrecioRegalo(precioRegalo: Boolean?) {
        this.precioRegalo = precioRegalo
    }

    fun setListItemTag(listItemTag: MutableMap<Int, String?>) {
        this.listItemTag = listItemTag
    }

    interface OrderListener {
        fun onDelete(item: ProductItem)
        fun onEdit(item: ProductItem)
        fun scrollList(height: Int)
        fun scrollDelete(height: Int, position: Int)
        fun onBackorder(item: ProductItem)
        fun loadPromotion(cuv: String?)
    }

    companion object {
        const val TYPE_CLIENT = 0
        const val TYPE_PRODUCT = 1
    }
}
