package biz.belcorp.consultoras.feature.search.single

import android.app.Activity
import android.graphics.Paint
import android.support.v4.app.FragmentManager
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.library.util.DeviceUtil
import android.widget.LinearLayout
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.util.anotation.OrderItemTag
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog


class ProductTooltip(builder: Builder) {

    private var tooltip: View? = null
    private var parameters: ProductParams? = null
    private var activity: Activity
    private var anchor: View
    private var parent: RelativeLayout
    private var animation: Boolean
    private var locked: Boolean
    private var message: String
    private var messagePopup: String
    private var errorMessage: String? = null
    private var errorTitle: String? = null
    private var quantity: Int? = null
    private var support: FragmentManager? = null
    private var addingEnable: Boolean? = null
    private lateinit var botnAdd: Button
    private lateinit var btnSuma: LinearLayout
    private lateinit var lnrError:LinearLayout
    private lateinit var txvError: TextView

    init {
        this.activity = builder.activity!!
        this.anchor = builder.anchor!!
        this.parent = builder.parent!!
        this.animation = builder.animation
        this.locked = builder.locked
        this.message = builder.message
        this.messagePopup = builder.messagePopup
        this.errorTitle = builder.errorTitle
        this.errorMessage = builder.errorMessage
        this.parameters = builder.parameters
        this.quantity = builder.quantity
        this.support = builder.support
        this.addingEnable = builder.addingEnable
    }

    fun show() {
        tooltip = createTooltip()
        tooltip = addLayoutParams(tooltip)
        parent.addView(tooltip)
    }

    fun showError() {
        // Creamos la vista
        val view = View.inflate(activity, R.layout.view_tooltip_product_error, null)

        val tvwTitle: TextView = view.findViewById(R.id.tvwTitle)
        val tvwMessage = view.findViewById<TextView>(R.id.tvwMessage)

        if (null != errorTitle) tvwTitle.text = errorTitle
        if (null != errorMessage) {
            tvwMessage.text = errorMessage}
        else{
            if(errorMessage == "") tvwMessage.visibility = View.GONE
        }

        tooltip = view

        tooltip = addLayoutParams(tooltip)
        parent.addView(tooltip)
    }

    fun showErrorAdding(texto: String) {
        botnAdd.isEnabled = false
        btnSuma.isEnabled = false
        txvError.text=texto
        lnrError.visibility = View.VISIBLE

    }

    private fun addLayoutParams(view: View?): View? {

        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.MATCH_PARENT)
        params.width = parent.width
        params.addRule(RelativeLayout.END_OF, anchor.id)

        view?.layoutParams = params

        return view
    }

    fun remove() {
        if (tooltip != null) {
            parent.removeView(tooltip)
        }
    }

    private fun addAnimation(view: View) {

    }

    private fun createTooltip(): View {

        // Creamos la vista
        val view = View.inflate(activity, R.layout.view_tooltip_product, null)

        // Parametros necesarios
        val producto = parameters?.prdCUV
        val precio = "${parameters?.mnySymb} ${parameters?.dcmlFrmt?.format(producto?.precioCatalogo)}"

        // Referencias a los elementos de la vista
        val tvwCantidad = view.findViewById<TextView>(R.id.tvwCantidad)
        val tvwPrecio = view.findViewById<TextView>(R.id.tvwPrecio)
        val tvwPrecioValorizado = view.findViewById<TextView>(R.id.tvwPrecioBefore)
        val tvwMessage = view.findViewById<TextView>(R.id.tvwMessage)
        val lltControls = view.findViewById<LinearLayout>(R.id.lltControls)
        val cvwMessagePopup = view.findViewById<CardView>(R.id.cvwMessagePopup)
        val tvwMsgPopup = view.findViewById<TextView>(R.id.tvwMsgPopup)
        val ivwCloseMsgPopup = view.findViewById<ImageView>(R.id.ivwCloseMsgPopup)
        val lnlblock =  view.findViewById<LinearLayout>(R.id.lnlblock)
        btnSuma = view.findViewById<LinearLayout>(R.id.btnAgregar)

        lnrError = view.findViewById(R.id.lnr_excedido)
        txvError = view.findViewById(R.id.tvw_excedido)
        botnAdd = view.findViewById(R.id.btnAdd)
        // Elementos sin necesidad de referencias
        view.findViewById<TextView>(R.id.tvwProducto).text = producto?.description ?: ""

        // Seteamos texto
        tvwPrecio.text = precio

        producto?.precioValorizado?.let { valorizado ->
            producto.precioCatalogo?.let {precio ->
                if(precio < valorizado){
                    tvwPrecioValorizado.text = "${parameters?.mnySymb} ${parameters?.dcmlFrmt?.format(valorizado)}"
                    tvwPrecioValorizado.paintFlags = tvwPrecioValorizado.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvwPrecioValorizado.visibility = View.VISIBLE
                }
            }
        }



        cvwMessagePopup.visibility = View.GONE
        quantity?.let { tvwCantidad.text = it.toString() }

        // Boton para sumar productos
        btnSuma.setOnClickListener {
            if (tvwCantidad.text.isEmpty()) tvwCantidad.text = "0"
            val quantity = sumarQuantity(Integer.parseInt(tvwCantidad.text.toString()))
            tvwCantidad.text = quantity.toString()
        }

        // Boton para restar productos
        view.findViewById<LinearLayout>(R.id.btnRestar).setOnClickListener {
            if (tvwCantidad.text.isEmpty()) tvwCantidad.text = "0"
            val quantity = restarQuantity(Integer.parseInt(tvwCantidad.text.toString()))
            tvwCantidad.text = quantity.toString()
            habilitateAdding()
        }

        botnAdd.setOnClickListener {
            if (tvwCantidad.text.toString().isEmpty() || tvwCantidad.text.toString() == "0" || tvwCantidad.text.toString() == "00") {
                Toast.makeText(activity, activity.getString(R.string.add_order_product_quantity_empty), Toast.LENGTH_SHORT).show()
            } else {
                val cantidad= Integer.parseInt(tvwCantidad.text.toString())
                if (producto?.flagFestival == true) {
                    val offersFest = parameters?.order?.productosDetalle?.firstOrNull { it?.cuv == producto.cuv }
                    when {
                        cantidad > 1 -> {
                            showPopupAlertOneFest(activity)
                        }
                        offersFest != null -> {
                            showPopupAlertOneFest(activity)
                        }
                        producto.reemplazarFestival == true -> {
                            showPopupFestReplace(activity) {
                                addToCart(producto, cantidad)
                            }
                        }
                        else -> {
                            addToCart(producto, cantidad)
                        }
                    }
                } else {
                    addToCart(producto, cantidad)
                }
            }
        }

        if (locked) lltControls.visibility = View.GONE
        else lltControls.visibility = View.VISIBLE

        if(message != ""){
            tvwMessage.text = message
            tvwMessage.visibility = View.VISIBLE
        }else{
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, 0, 20)
            lnlblock.layoutParams = layoutParams
        }

        ivwCloseMsgPopup.setOnClickListener {
            cvwMessagePopup.visibility = View.GONE
        }

        if (messagePopup != "") {
            tvwMsgPopup.text = messagePopup
            cvwMessagePopup.visibility = View.VISIBLE
        }
        // }

        tvwCantidad.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(!p0.isNullOrBlank())
                        habilitateAdding()
                    else{
                        botnAdd.isEnabled = false
                        btnSuma.isEnabled = false
                    }
                }
            })

        return view
    }

    private fun addToCart(producto: ProductCUV?, cantidad: Int) {
        producto?.clienteId = parameters?.clntMdl?.clienteID
        producto?.clienteLocalId = parameters?.clntMdl?.id
        producto?.cantidad = cantidad
        producto?.identifier = DeviceUtil.getId(activity)
        parameters?.prdPrsntr?.insertHomologado(producto!!, DeviceUtil.getId(activity))
    }

    private fun showPopupAlertOneFest(activity: Activity) {
        val context = activity.applicationContext
        val nameFest = parameters?.listTagOrder?.get(OrderItemTag.TAG_FEST)
            ?: context.getString(R.string.tagFestival)
        val messageFest = String.format(context.getString(R.string.fest_one_alert_message), nameFest)
        BottomDialog.Builder(activity)
            .setIcon(R.drawable.ic_mano_error)
            .setTitle(context.getString(R.string.fest_one_alert_title))
            .setTitleBold()
            .setContent(messageFest)
            .setNeutralText(context.getString(R.string.msj_entendido))
            .onNeutral(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog) {
                    dialog.dismiss()
                }
            })
            .setNeutralBackgroundColor(R.color.magenta)
            .show()
    }

    private fun showPopupFestReplace(activity: Activity, positiveAction: () -> Unit) {
        val context = activity.applicationContext
        BottomDialog.Builder(activity)
            .setIcon(R.drawable.ic_mano_error)
            .setTitle(context.getString(R.string.fest_alert_title))
            .setTitleBold()
            .setContent(context.getString(R.string.fest_alert_message))
            .setNegativeText(context.getString(R.string.fest_alert_cancel))
            .setPositiveText(context.getString(R.string.fest_alert_ok))
            .setNegativeTextColor(R.color.black)
            .setNegativeBorderColor(R.color.black)
            .setNegativeBackgroundColor(R.color.white)
            .onNegative(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog) {
                    dialog.dismiss()
                }
            })
            .onPositive(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog, chbxConfirmacion: CheckBox) {
                    positiveAction()
                }
            })
            .setPositiveBackgroundColor(R.color.magenta)
            .show()
    }

    private fun habilitateAdding() {
        botnAdd.isEnabled = true
        btnSuma.isEnabled = true
        lnrError.visibility= View.GONE
    }

    private fun restarQuantity(quantity: Int): Int {
        return if ((quantity - 1) <= 0) 1
        else quantity - 1
    }

    private fun sumarQuantity(quantity: Int): Int {
        return if (quantity >= 99) quantity else (quantity + 1)
    }

    fun changeClienteModel(clienteModel: ClienteModel) {
        parameters?.clntMdl = clienteModel
    }


    class Builder(internal var activity: Activity?) {

        var anchor: View? = null
        var gravity: Int? = null
        var parent: RelativeLayout? = null
        var animation: Boolean = false
        var locked: Boolean = false
        var message: String = ""
        var errorMessage: String? = null
        var errorTitle: String? = null
        var messagePopup: String = ""

        var parameters: ProductParams? = null
        var quantity: Int? = null
        var support: FragmentManager? = null
        var addingEnable: Boolean ? = null

        fun into(parent: RelativeLayout): Builder {
            this.parent = parent
            return this
        }

        fun withAnchor(view: View): Builder {
            this.anchor = view
            return this
        }

        fun withAnimation(animation: Boolean): Builder {
            this.animation = animation
            return this
        }

        fun isLocked(locked: Boolean): Builder {
            this.locked = locked
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setMessagePopup(messagePopup: String): Builder {
            this.messagePopup = messagePopup
            return this
        }

        fun setError(title: String?, message: String?): Builder {
            this.errorTitle = title
            this.errorMessage = message
            return this
        }

        fun withParameters(parameters: ProductParams): Builder {
            this.parameters = parameters
            return this
        }

        fun withQuantity(quantity: Int): Builder {
            this.quantity = quantity
            return this
        }

        fun withSupport(support: FragmentManager): Builder {
            this.support = support
            return this
        }

        fun withEnabledAdding(state: Boolean): Builder{
            this.addingEnable = state
            return this
        }

        fun build(): ProductTooltip {
            if (activity == null) throw IllegalStateException("You must set an activity")
            if (parent == null) throw IllegalStateException("You must addFromHome a parent")
            if (anchor == null) throw IllegalStateException("You must addFromHome a anchor")
            return ProductTooltip(this)
        }
    }

}
