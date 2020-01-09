package biz.belcorp.consultoras.feature.home.ganamas.adapters

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.RelativeLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.offers.Multi
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import kotlinx.android.synthetic.main.item_offers_grid_item.view.*

class MultiOfferGridViewHolder(
    val view: View,
    private val listener: OffersGridItemListener?,
    private val itemPlaceholder: Drawable?,
    private val hideViewsForTesting: Boolean = false,
    private val stampLists: MutableList<Sello>
) : BaseViewHolder<OfferModel>(view) {

    private val multiItem = view.carouselMulti as Multi

    override fun bind(position: Int, item: OfferModel) {

        multiItem.placeholder = itemPlaceholder

        //INI ABT GAT-11
        multiItem.bonificacion = item.bonificacion
        //END ABT GAT-11

        multiItem.setValues(item.brand, item.productName, item.personalAmount,
            item.clientAmount, item.imageURL, item.isSelectionType, item.showCLientAmount,
            StringUtil.Empty, item.soldOut, flagFestival = item.flagFestival, flagKitNueva = item.flagKitNueva)

        item.tipoPersonalizacion?.let { tipoPers ->
            if (tipoPers == OfferTypes.CAT) {
                multiItem.isCLientAmountShow(false)
                multiItem.setTitleLabelYou(itemView.context.getString(R.string.precio_para_cliente))
            } else {
                multiItem.isCLientAmountShow(true)
                multiItem.setTitleLabelYou(itemView.context.getString(R.string.precio_para_ti))
            }
        }

        val currentStamp = when {
            item.flagFestival == true -> {
                stampLists.firstOrNull { it.getType() == Sello.FESTIVAL }
            }
            item.flagPromotion == true -> {
                stampLists.firstOrNull { it.getType() == Sello.PROMOTION }
            }
            else -> null
        }

        currentStamp?.let {
            multiItem.showStamp(true)
            multiItem.setStamp(it)
            if (item.soldOut == true) {
                multiItem.setStampBackgroundDisabled()
            }
        } ?: kotlin.run {
            multiItem.showStamp(false)
        }

        /**
         * Listener
         */

        multiItem.multiButtonListener = object : Multi.MultiButtonListener {
            override fun onClickSelection() {
                listener?.pressedItemButtonSelection(item.key, item.marcaID, item.brand, position)
                listener?.onPrintClickProduct(item, position)
            }

            override fun onClickAdd(quantity: Int, counterView: Counter) {
                listener?.pressedItemButtonAdd(item.key, quantity, counterView)
                listener?.onPrintAddProduct(item, position)
            }

            override fun onClickShowOffer() { /* Not necessary */
            }
        }

        multiItem.multiContainerListener = object : Multi.ContainerClickListener {
            override fun onClick() {

                listener?.pressedItem(item.key, item.marcaID, item.brand, position)
                listener?.onPrintClickProduct(item, position)
            }
        }

        listener?.onPrintProduct(item, position)

        /**
         * Agregue funcion para ocultar las vistas para el A/B testing
         */

        if (hideViewsForTesting) multiItem.hideViewsForTesting()
    }

    private fun setInternalDecoration(multi: Multi, position: Int) {
        val itemMargin = itemView.context.resources.getDimensionPixelSize(R.dimen.offers_grid_default_item_margin)
        val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(itemMargin, itemMargin, itemMargin, itemMargin)

        when (position % 2) {
            0 -> lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            1 -> lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        }

        multi.layoutParams = lp
    }
}

interface OffersGridItemListener {
    fun pressedItem(keyItem: String, marcaID: Int, marca: String, position: Int)
    fun pressedItemButtonAdd(keyItem: String, quantity: Int, counterView: Counter)
    fun pressedItemButtonSelection(keyItem: String, marcaID: Int, marca: String, position: Int)
    fun onPrintProduct(item: OfferModel, position: Int)
    fun onPrintClickProduct(item: OfferModel, position: Int)
    fun onPrintAddProduct(item: OfferModel, quantity: Int)
}
