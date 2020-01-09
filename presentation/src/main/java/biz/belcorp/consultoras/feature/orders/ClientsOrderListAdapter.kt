package biz.belcorp.consultoras.feature.orders

import android.animation.LayoutTransition
import android.content.Context
import android.annotation.SuppressLint
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import kotlinx.android.synthetic.main.item_order_client.view.*
import java.text.DecimalFormat
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class ClientsOrderListAdapter(
    clients: ArrayList<ClienteModel>
) : RecyclerView.Adapter<ClientsOrderListAdapter.ViewHolder>() {

    private var allItems: ArrayList<ClienteModel> = clients
    private var moneySymbol: String = ""
    private var consultantName: String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()
    lateinit var enabledclicks: Array<Boolean>
    lateinit var quantity: Array<Int>
    lateinit var total: Array<Double>
    lateinit var heights: Array<Int>
    lateinit var order: OrderModel
    var isReservation: Boolean = false

    var listener: OrdersList.OrderListListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(model: ClienteModel) = with(itemView) {

            if (model.clienteID == 0) {
                tvwTitle.text = context.getText(R.string.add_order_consultant)
            } else {
                var name = model.nombres + if (model.apellidos == null) "" else " " + model.apellidos
                name = name.trim { it <= ' ' }
                val parts = name.split(Pattern.quote(" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                if (name != "") {
                    if (parts.size >= 2)
                        tvwTitle.text = parts[0] + " " + parts[1]
                    else
                        tvwTitle.text = name
                }
            }


            val layoutTransition = ordersListParent.layoutTransition
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)


            cvwItem.setOnClickListener {
                when(ordersList.visibility){

                    View.GONE->{
                        ordersList.visibility = View.VISIBLE
                        ivwHide.setImageDrawable(VectorDrawableCompat.create(context!!.resources,  R.drawable.ic_arrow_up_black, null))
                        rltData.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_box_card_view_client))
                        viewLine.visibility = View.VISIBLE
                        ordersList?.postDelayed({
                            listener?.scrolldown(ordersList.height)
                        }, 300)
                    }
                    View.VISIBLE->{
                        ordersList.visibility = View.GONE
                        ivwHide.setImageDrawable(VectorDrawableCompat.create(context!!.resources, R.drawable.ic_arrow_down_black, null))
                        rltData.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_box_card_view_client_inactive))
                        viewLine.visibility = View.GONE
                    }
                }
            }

            var cantidades = 0
            var totales = 0.0

            for (item in model.orderList) {
                cantidades += item!!.cantidad!!
                totales += (item.precioUnidad!!.toDouble() * item.cantidad!!)
            }

            ordersList.orderListListener = listener
            ordersList.isReservation = isReservation
            ordersList.setData(model.orderList)
            ordersList.setDecimalFormat(decimalFormat, moneySymbol, consultantName, OrdersListAdapter.TYPE_CLIENT)

            with(itemView) {
                ordersList.postDelayed(
                    {
                        heights[adapterPosition] = ordersList.measuredHeight
                        quantity[adapterPosition] = cantidades
                        total[adapterPosition] = totales



                        if (quantity[adapterPosition] != 0) {
                            if (quantity[adapterPosition] == 1) {
                                tvwDescription.text = "${quantity[adapterPosition]} unidad ingresada"
                            } else {
                                tvwDescription.text = "${quantity[adapterPosition]} unidades ingresadas"
                            }
                        }

                        if (total[adapterPosition] != 0.0) {
                            tvwSubTitle.text = "$moneySymbol ${decimalFormat.format(total[adapterPosition])}"
                        }

                        if(isReservation) {
                            if (allItems.size > 1) hideDetails(context, itemView, adapterPosition)
                        }else {
                            if (adapterPosition != 0)
                                hideDetails(context, itemView, adapterPosition)
                            else
                                viewLine.visibility = View.VISIBLE
                        }

                    }, 100)
            }

        }
    }

    private fun hideDetails(context: Context, view: View, position: Int){
        enabledclicks[position] = true
        view.ordersList.visibility = View.GONE
        view.ivwHide.setImageDrawable(VectorDrawableCompat.create(context!!.resources, R.drawable.ic_arrow_down_black, null))
        view.rltData.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_box_card_view_client_inactive))
        view.viewLine.visibility = View.GONE
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_order_client, viewGroup, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allItems[position])
    }

    override fun getItemCount(): Int = allItems.size

    fun setList(orderListItems: List<ClienteModel?>?) {
        allItems.clear()
        orderListItems?.filterNotNull()?.let {
            allItems.addAll(it)
        }
        enabledclicks = Array(allItems.size, { true })
        quantity = Array(allItems.size, { 0 })
        total = Array(allItems.size, { 0.0 })
        heights = Array(allItems.size, { 0 })
        notifyDataSetChanged()
    }

    fun setFormat(decimalFormat: DecimalFormat, moneySymbol: String, consultantName: String) {
        this.decimalFormat = decimalFormat
        this.moneySymbol = moneySymbol
        this.consultantName = consultantName
    }

    fun animateHeight(v: View, height: Int, show: Boolean, adapterPosition: Int) {

        val initialHeight = v.measuredHeight
        val duration = 250
        val interpolator = AccelerateInterpolator(2f)

        v.layoutParams.height = initialHeight
        v.requestLayout()

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (show) {
                    v.layoutParams.height = (height * interpolatedTime).toInt()
                } else {
                    v.layoutParams.height = initialHeight - (height * interpolatedTime).toInt()
                }
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = duration.toLong()
        a.interpolator = interpolator
        a.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // EMPTY
            }

            override fun onAnimationEnd(animation: Animation?) {
                enabledclicks[adapterPosition] = true
            }

            override fun onAnimationStart(animation: Animation?) {
                // EMPTY
            }
        })
        v.startAnimation(a)
    }

}
