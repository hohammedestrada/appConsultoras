package biz.belcorp.consultoras.feature.search.list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.domain.entity.OfferPromotionDual
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.Promotion
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.mobile.components.design.carousel.promotion.CarouselPromotion
import biz.belcorp.mobile.components.design.carousel.promotion.model.PromotionModel
import biz.belcorp.mobile.components.design.carousel.promotiondual.CarouselPromotionDual
import biz.belcorp.mobile.components.design.carousel.promotiondual.OfferPromotionModel
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.library.util.StringUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_search_empty.view.*
import kotlinx.android.synthetic.main.item_search_list.view.*
import kotlinx.android.synthetic.main.item_search_promotion_list.view.*
import kotlinx.android.synthetic.main.view_product_operator.view.*
import kotlinx.android.synthetic.main.item_search_empty.vw_empty_space
import java.text.DecimalFormat
import java.util.*

/**
 * Created by Leonardo on 13/09/2018.
 */

class SearchListAdapter
(private val context: Context, private val listener: SearchListener) : RecyclerView.Adapter<SearchListAdapter.BaseViewHolder<*>>(), SafeLet {
    private val productList = ArrayList<Any>()
    private var moneySymbol: String = StringUtil.Empty
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var maxNameShow = DEFAULT_CARACTERES_MOSTRAR
    var promotion: Promotion? = null

    var positionCarousel = 0

    companion object {
        const val DEFAULT_CARACTERES_MOSTRAR = 25
        const val COD_EST_COMPUESTA_VARIABLE = 2003

        private const val TYPE_PRODUCT = 0
        private const val TYPE_PROMOTION = 1
        private const val TYPE_FOOTER = 2
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, moneySymbol: String, maxNameShow: Int,
                          decimalFormat: DecimalFormat, position: Int, listener: SearchListener)
    }

    //region ViewHolder
    inner class ProductViewHolder(itemView: View) : BaseViewHolder<ProductCUV>(itemView) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: ProductCUV, moneySymbol: String, maxNameShow: Int,
                 decimalFormat: DecimalFormat, position: Int, listener: SearchListener) = with(itemView) {

            item.fotoProductoSmall?.let {
                Glide.with(this).asBitmap().load(it)
                    .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?,
                                                     target: Target<Bitmap>?,
                                                     dataSource: DataSource?,
                                                     isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any,
                                                  target: Target<Bitmap>,
                                                  isFirstResource: Boolean): Boolean {
                            img_product.setImageDrawable(ContextCompat
                                .getDrawable(context, R.drawable.ic_container_placeholder))
                            return true
                        }
                    })
                    .into(img_product)
            }

            if( item.tipoPersonalizacion == OfferTypes.CAT) {
                txt_precio.visibility = View.GONE
            } else {
                txt_precio.visibility = View.VISIBLE
            }

            txt_precio.text = "$moneySymbol ${decimalFormat.format(item.precioValorizado)}"
            txt_precio.paintFlags = txt_precio.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            txt_precio_oferta.text = "$moneySymbol ${decimalFormat.format(item.precioCatalogo)}"
            txt_catalogo.text = item.descripcionEstrategia
            lltOperators.resetQuantity()

            item.description?.let {
                if (it.length <= maxNameShow) {
                    txt_descripcion.text = it.trim()
                } else {
                    txt_descripcion.text = "${it.trim().take(maxNameShow)}..."
                }
            }

            if (item.stock == true) {

                // Resetando imagen de gris a original
                img_product.clearColorFilter()

                // Oferta digital y de tipo estrategia compuesta variable (Elige tu opcion)
                if (isOfertaDigital(item.codigoTipoEstrategia)
                    && item.codigoEstrategia == COD_EST_COMPUESTA_VARIABLE) {
                    lnl_panel_disabled.visibility = View.GONE
                    lltOperators.visibility = View.INVISIBLE
                    btnGoItem.visibility = View.VISIBLE
                    btnAddItem.visibility = View.GONE
                    lnlFicha.setPadding(0, 20, 0, 20)
                }
                // Estrategia individual o compuesta fija (Agregalo)
                else {
                    lnl_panel.visibility = View.VISIBLE
                    lnl_panel_disabled.visibility = View.GONE
                    lltOperators.visibility = View.VISIBLE
                    btnGoItem.visibility = View.GONE
                    btnAddItem.visibility = View.VISIBLE
                }

            } else {
                lnl_panel_disabled.visibility = View.VISIBLE
                lnl_panel.visibility = View.GONE

                //imagen en gris
                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0f)
                val filter = ColorMatrixColorFilter(colorMatrix)
                img_product.colorFilter = filter
            }

            if (isOfertaDigital(item.codigoTipoEstrategia)) {
                lnlFicha.setOnClickListener {
                    listener.onShowProduct(item)
                    listener.onPrintClickProduct(item, position)
                }
            } else {
                lnlFicha.setOnClickListener(null)
            }

            if (item.agregado == true) {
                txt_agregado.text = context.resources.getString(R.string.search_added)
            } else {
                txt_agregado.text = StringUtil.Empty
            }


            btnAddItem.setOnClickListener {
                item.cantidad = lltOperators.quantity
                listener.onAddProduct(item)
                listener.onPrintAddProduct(item, item.cantidad!!)
            }

            btnGoItem.setOnClickListener {
                item.cantidad = lltOperators.quantity
                listener.onShowProduct(item)
                listener.onPrintClickProduct(item, position)
            }

            listener.onPrintProduct(item, position)

        }

        // Private Functions
        private fun isOfertaDigital(codigoTipoEstrategia: String?): Boolean {
            val SearchCatalogo = "0"
            return (codigoTipoEstrategia in listOf("030", "005", "001", "007", "008", "009", "010", "011", "LMG", SearchCatalogo))
        }
    }

    inner class PromotionViewHolder(itemView: View) : BaseViewHolder<Promotion>(itemView) {
        override fun bind(item: Promotion, moneySymbol: String, maxNameShow: Int,
                          decimalFormat: DecimalFormat, position: Int, listener: SearchListener) = with(itemView) {

            val data = getPromotionList(item.detalle, moneySymbol, decimalFormat)

            carouselPromotion1.visibility = View.GONE
            carouselPromotionDual1.visibility = View.GONE
            linepromotion1.visibility = View.GONE

            if (data.size > 0) {
                carouselPromotion1.visibility = View.VISIBLE
                linepromotion1.visibility = View.VISIBLE

                carouselPromotion1.showLimit = 0
                carouselPromotion1.loadPromotion(data.toList())

                carouselPromotion1.listener = (object : CarouselPromotion.Listener {
                    override fun onClick(obj: PromotionModel) {
                        item.detalle?.firstOrNull { it?.cuv == obj.cuv }?.let {
                            listener.onClickPromotion(it, position)
                        }
                    }
                })

            }
            //conditions


            var offers = getConditionsList(item.detalleCondition, moneySymbol, decimalFormat)
            if (offers.size > 0) {
                carouselPromotionDual1.Load(offers, object : CarouselPromotionDual.Listaner {
                    override fun onClickItem(item: OfferPromotionModel) {
                        listener.onClickCondition(item)
                    }
                })
                carouselPromotionDual1.visibility = View.VISIBLE
                linepromotion1.visibility = View.VISIBLE
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

        override fun bind(item: String, moneySymbol: String, maxNameShow: Int, decimalFormat: DecimalFormat, position: Int, listener: SearchListener) = with(itemView) {
            vw_empty_space.visibility = View.VISIBLE
        }

    }

    //endregion ViewHolders

    override fun getItemViewType(position: Int): Int {
        val comparable = productList[position]

        return when (comparable) {
            is ProductCUV -> TYPE_PRODUCT
            is Promotion -> TYPE_PROMOTION
            is String -> TYPE_FOOTER
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    override fun getItemCount(): Int = productList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_PRODUCT -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_search_list, parent, false)
                ProductViewHolder(view)
            }
            TYPE_PROMOTION -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_search_promotion_list, parent, false)
                PromotionViewHolder(view)
            }
            TYPE_FOOTER -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_search_empty, parent, false)
                FooterViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = productList[position]
        when (holder) {
            is ProductViewHolder -> holder.bind(element as ProductCUV, moneySymbol, maxNameShow, decimalFormat,
                position, listener)
            is PromotionViewHolder -> holder.bind(element as Promotion, moneySymbol, maxNameShow, decimalFormat,
                position, listener)
            is FooterViewHolder -> holder.bind(element as String, moneySymbol, maxNameShow, decimalFormat,
                position, listener)
            else -> throw IllegalArgumentException()
        }
    }

    // Interface
    interface SearchListener {
        fun onAddProduct(item: ProductCUV)
        fun onShowProduct(item: ProductCUV)
        fun onPrintProduct(item: ProductCUV, position: Int)
        fun onPrintClickProduct(item: ProductCUV, position: Int)
        fun onPrintAddProduct(item: ProductCUV, quantity: Int)
        fun onClickPromotion(item: ProductCUV, promotionPosition: Int)
        fun onClickCondition(item: OfferPromotionModel)
    }

    // Public Functions
    fun setList(list: List<ProductCUV?>?, withFooter: Boolean = false) {
        var superaTotal: Boolean = false
        productList.clear()

        positionCarousel = (promotion?.posicion ?: 0)

        val total = (list?.size ?: 0)
        if (positionCarousel > total) {
            positionCarousel = total
            superaTotal = true
        }
        list?.filter { !it?.tipoPersonalizacion.equals(context.getString(R.string.promotion_val_noresultlist)) }?.filterNotNull()?.let { productList.addAll(it) }

        //regionpromotion
        if (promotion?.detalle?.any() ?: false) {
            //val posicion = 1 //for internal test (devs)
            if (positionCarousel > 0 && positionCarousel - 1 <= productList.size - 1) {
                productList.add(positionCarousel - 1, promotion!!)
            } else if (productList.size == 0 && (promotion?.detalle?.any() ?: false)) {
                productList.add(0, promotion!!)
            }
        } else {
            if (list?.size ?: 0 > 0) {
                //productList.add(positionCarousel - 1, promotion!!)
                if (superaTotal) {
                    productList.add(promotion!!)
                    positionCarousel = productList.size
                } else {
                    if (positionCarousel > 0){
                        productList.add(positionCarousel - 1, promotion!!)
                    }

                }
            }
        }

        if (withFooter) {
            val textFooter = "FOOTER"
            productList.add(textFooter)
        }

        //endregion promotion

        notifyDataSetChanged()
    }

    fun getList(): ArrayList<Any> = productList

    fun setData(moneySymbol: String, maxNameShow: Int, decimalFormat: DecimalFormat) {
        this.moneySymbol = moneySymbol
        this.maxNameShow = maxNameShow
        this.decimalFormat = decimalFormat
    }

    fun clearData() {
        productList.clear()
        notifyDataSetChanged()
    }

    fun setProductAdded(cuv: String) {
        var x = (productList.filter { it is ProductCUV }.first {
            it.run { it as ProductCUV }.cuv.equals(cuv)
        } as ProductCUV).apply { agregado = true; cantidad = 1 }

        notifyDataSetChanged()
    }

    fun getPromotionList(list: Collection<ProductCUV?>?, moneySymbol: String, decimalFormat: DecimalFormat): ArrayList<PromotionModel> {
        return ArrayList<PromotionModel>().apply {
            list?.filterNotNull()?.forEach {
                safeLet(it.tipoPersonalizacion, it.cuv, it.description, it.precioCatalogo, ImageUtils.verifiedImageUrl(it))
                { tipoPersonalizacion, cuv, descriptionPromotion, precioCatalogo, imagenPromotion ->
                    add(PromotionModel(
                        tipoPersonalizacion,
                        cuv,
                        context.getString(R.string.promotion_text_item, descriptionPromotion, "$moneySymbol ${decimalFormat.format(precioCatalogo)}"),
                        imagenPromotion))
                }
            }
        }
    }

    fun getConditionsList(items: ArrayList<OfferPromotionDual>?, moneySymbol: String, decimalFormat: DecimalFormat): ArrayList<OfferPromotionModel> {

        val offers = ArrayList<OfferPromotionModel>()
        items?.forEach {
            offers.add(OfferPromotionModel().apply {
                this.messagePromotion = context?.getString(R.string.condition_text_item, it?.descriptionPromotion, "$moneySymbol ${decimalFormat.format(it?.pricePromotion)}")
                this.cuvCondition = it.cuvCondition
                this.cuvPromotion = it.cuvPromotion
                this.imageURLCondition = it.imageURLCondition
                this.imageURLPromotion = it.imageURLPromotion
                this.typeCondition = it.typeCondition
                this.typePromotion = it.typePromotion
            })
        }

        return offers

        /*return ArrayList<PromotionModel>().apply {
            list?.filterNotNull()?.forEach {
                safeLet(it.tipoPersonalizacion, it.cuv, it.description, it.precioCatalogo, ImageUtils.verifiedImageUrl(it))
                { tipoPersonalizacion, cuv, descriptionPromotion, precioCatalogo, imagenPromotion ->
                    add(PromotionModel(
                        tipoPersonalizacion,
                        cuv,
                        context.getString(R.string.promotion_text_item, descriptionPromotion, "$moneySymbol ${decimalFormat.format(precioCatalogo)}"),
                        imagenPromotion))
                }
            }
        }*/
    }

    //endregion Functions
}
