package biz.belcorp.consultoras.feature.product

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.mensajeprol.MensajeProlDataMapper
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.consultoras.domain.entity.OrderListItem
import biz.belcorp.consultoras.feature.product.di.ProductComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.view_product_components.*
import kotlinx.android.synthetic.main.view_product_information.*
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject


class ProductFragment : BaseFragment(),
    ProductView {


    @Inject
    lateinit var presenter: ProductPresenter

    private var product: ProductItem? = null
    private var clienteModel: ClienteModel? = null
    private var onClientFilterClick: OnClientFilterClick? = null
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var moneySymbol: String = ""
    private var pedidoID: Int? = null
    private var mensajes: Collection<MensajeProl?>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.initScreenTrack()
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(ProductComponent::class.java).inject(this)
        return true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ProductFragment.OnClientFilterClick) {
            onClientFilterClick = context
        }
    }

    // Override BaseFragment
    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    // Override ProductView
    override fun loadProduct(product: ProductItem) {

        if (product.isArmaTuPack == true) {

            lnlInformation.visibility = View.GONE
            lnlComponents.visibility = View.VISIBLE

            val precio = "$moneySymbol ${decimalFormat.format(product.importeTotal)}"
            txtDescripcion.text = context?.getText(R.string.arma_tu_pack)
            txtPrecio.text = precio

            val adapter = ProductComponentAdapter(product.components)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvComponents.layoutManager = layoutManager
            rcvComponents.adapter = adapter

            // Aceptar
            btn_accept.setOnClickListener {
                val returnIntent = Intent()
                activity?.setResult(Activity.RESULT_CANCELED, returnIntent)
                activity?.finish()
            }

        } else {

            lnlInformation.visibility = View.VISIBLE
            lnlComponents.visibility = View.GONE

            val beforePrice = "$moneySymbol ${decimalFormat.format(product.precioUnidad)}"
            val actualPrice = "$moneySymbol ${decimalFormat.format(product.importeTotal)}"

            edt_quantity.setText(product.cantidad.toString())

            val name = product.descripcionProd?.let {
                it.split("|")
            }

            name?.let {
                if(name.isNotEmpty()){
                    txt_product_name.text = name[0]
                }
            } ?: kotlin.run {
                txt_product_name.text = product.descripcionProd
            }

            txt_price_before.text = beforePrice
            txt_price_now.text = actualPrice

            if (product.conjuntoID != 0) {
                lnlAsignacion.visibility = View.GONE
            }

            // Oferta Producto
            product.etiquetaProducto?.let {
                if (!it.isEmpty()) {
                    txt_offert.text = it
                    txt_offert.visibility = View.VISIBLE
                }
            } ?: run { txt_offert.visibility = View.GONE }

            // Tachar el texto (Mas adelante)
            //txt_price_before.paintFlags = txt_price_before.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            when (product.clienteID) {
                0 -> {
                    txt_client.text = resources.getText(R.string.product_for_me)
                }
                else -> {
                    val clientName = "${resources.getText(R.string.product_owner)}  ${product.nombreCliente}"
                    txt_client.text = clientName
                }
            }

            // Restar
            btn_subtract.setOnClickListener {
                val quantity = restarQuantity(getQuantity())
                edt_quantity.setText(quantity.toString())
                changeTotal(quantity)
                btn_add.isEnabled = true
                btn_accept.isEnabled = true
                lnr_excedido_editar.visibility = View.GONE
            }

            // Sumar
            btn_add.setOnClickListener {
                val quantity = sumarQuantity(getQuantity())
                edt_quantity.setText(quantity.toString())
                changeTotal(quantity)
            }

            // Edicion manual de la cantidad
            edt_quantity.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString() != "") {
                        changeTotal((p0.toString()).toInt())
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

            // Asignacion de Cliente
            btn_assignment.setOnClickListener {
                onClientFilterClick?.onFilter()
            }

            // Aceptar
            btn_accept.setOnClickListener {
                if (edt_quantity.text.toString().isEmpty() || edt_quantity.text.toString() == "0") {
                    Toast.makeText(activity, activity?.getString(R.string.add_order_product_quantity_empty), Toast.LENGTH_SHORT).show()
                } else {

                    if (!NetworkUtil.isThereInternetConnection(context())) {
                        showNetworkError()
                    } else {
                        pedidoID?.let { _ ->
                            val updateProduct = parseProduct(product)
                            updateProduct.cantidad = getQuantity()
                            clienteModel?.let { _ ->
                                updateProduct.clienteID = clienteModel?.clienteID
                                updateProduct.nombreCliente = clienteModel?.nombres + clienteModel?.apellidos
                            }
                            presenter.updateProduct(pedidoID!!, DeviceUtil.getId(activity), updateProduct)
                        }
                    }
                }
            }
        }
    }

    override fun onProductSaved(message: String?) {
        val returnIntent = Intent()
        returnIntent.putExtra(ProductActivity.EXTRA_MESSAGE_ADDING, message)
        
        if (mensajes != null) {
            if (mensajes!!.size > 0) {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ProductActivity.EXTRA_MENSAJE_PROL, MensajeProlDataMapper().transformToDomainModel(mensajes) as ArrayList<out Parcelable>)
                returnIntent.putExtra(ProductActivity.EXTRA_BUNDLE, bundle)
            }
        }
        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()
    }

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_PRODUCT, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_PRODUCT, model)
    }

    override fun showTooltipError(message: String?) {
        message?.let {
            llt_error.visibility = View.VISIBLE
            txt_error.text = it
        }
    }

    override fun hideTooltipError() {
        if (llt_error.visibility == View.VISIBLE) {
            llt_error.visibility = View.GONE
        }
    }

    // ClientOrderFilterFragment.OnFilterCompleteListener
    @SuppressLint("SetTextI18n")
    fun onComplete(clienteModel: ClienteModel) {

        this.clienteModel = clienteModel
        val client = clienteModel.nombres + if (clienteModel.apellidos != null) " " + clienteModel.apellidos else ""

        when(clienteModel.clienteID){
            0 -> txt_client.text = resources.getText(R.string.product_for_me)
            else -> {
                val name = "${resources.getText(R.string.product_owner)}  $client"
                txt_client.text = name
            }
        }
    }

    override fun showErrorExcedido(message: String?) {
        lnr_excedido_editar.visibility = View.VISIBLE
        btn_accept.isEnabled = false
        btn_add.isEnabled = false
        if(!message.isNullOrBlank())
            tvw_excedido_extra.text = message
    }

    override fun showBootomMessage(message: String?) {
        context?.let {
            BottomDialog.Builder(it)
                .setImage(R.drawable.ic_mano_error)
                .setImageDefault(R.drawable.ic_mano_error)
                .setImageSize(50, 50)
                .setContentTextSize(16)
                .setNeutralBackgroundColor(R.color.magenta)
                .setNeutralText(getString(R.string.product_error_reservation_button))
                .onNeutral(object: BottomDialog.ButtonCallback{
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .setContentTextColor(R.color.black)
                .setContent(message ?: getString(R.string.product_error_reservation_body))
                .autoDismiss(true, 2500)
                .show()
        }
    }

    // Metodos
    private fun init(){
        edt_quantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrBlank()){
                    btn_add!!.isEnabled = true
                    btn_accept.isEnabled = true
                    lnr_excedido_editar.visibility= View.GONE
                }
                else{
                    btn_add.isEnabled = false
                    btn_accept.isEnabled = false
                }
            }
        })
        product = arguments?.getParcelable(ProductActivity.EXTRA_PRODUCTO)
        moneySymbol = arguments?.getString(ProductActivity.EXTRA_MONEY_SYMBOL) ?: ""
        pedidoID = arguments?.getInt(ProductActivity.EXTRA_ID_PEDIDO)

        arguments?.getString(ProductActivity.EXTRA_COUNTRY_ISO)?.let {
            this.decimalFormat = CountryUtil.getDecimalFormatByISO(it, true)
        }
        product?.let { loadProduct(it) }.run { }


        edt_quantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //nada
                val q: String = s.toString()
                if (q.isNotEmpty()) {
                    btn_accept.isEnabled = q.toInt() != 0
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //nada
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //nada
            }

        })
    }

    private fun getQuantity(): Int {
        val quantity = edt_quantity.text.toString()
        return if (quantity.isEmpty() || quantity == "") 0
        else Integer.parseInt(quantity)
    }

    private fun restarQuantity(quantity: Int): Int {
        return if ((quantity - 1) <= 0) 1 else {
            quantity - 1
        }
    }

    private fun sumarQuantity(quantity: Int): Int {
        return if (quantity == 99) {
            quantity
        } else {
            quantity + 1
        }
    }

    private fun changeTotal(quantity: Int) {
        val calc = quantity.toFloat() * product?.precioUnidad!!.toFloat()
        val total = "$moneySymbol ${decimalFormat.format(calc)}"
        txt_price_now.text = total
    }

    private fun parseProduct(product: ProductItem): OrderListItem {
        return OrderListItem().apply {
            id = product.id
            cuv = product.cuv
            descripcionProd = product.descripcionProd
            descripcionProd = product.descripcionCortaProd
            cantidad = product.cantidad
            precioUnidad = product.precioUnidad
            importeTotal = product.importeTotal
            clienteID = product.clienteID
            clienteLocalID = product.clienteLocalID
            nombreCliente = product.nombreCliente
            subido = product.subido
            isEsKitNueva = product.isEsKitNueva
            tipoEstrategiaID = product.tipoEstrategiaID
            tipoOfertaSisID = product.tipoOfertaSisID
            observacionPROL = product.observacionPROL
            etiquetaProducto = product.etiquetaProducto
            indicadorOfertaCUV = product.indicadorOfertaCUV
            mensajeError = product.mensajeError
            setID = product.conjuntoID
        }
    }

    override fun onMensajeProl(mensajes: Collection<MensajeProl?>?) {
        this@ProductFragment.mensajes = mensajes
    }

    // Interfaces
    interface OnClientFilterClick {
        fun onFilter()
    }
}
