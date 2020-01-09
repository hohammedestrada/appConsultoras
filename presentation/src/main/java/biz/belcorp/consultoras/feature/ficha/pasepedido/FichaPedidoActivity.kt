package biz.belcorp.consultoras.feature.ficha.pasepedido

import android.content.Context
import android.content.Intent
import android.os.Bundle
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment

class FichaPedidoActivity : BaseFichaActivity() {

    override fun generateFichaFragment(): BaseFichaFragment = FichaPedidoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbarIcons(FichaMenuItems.SHARE_ITEM, FichaMenuItems.CART_ITEM, FichaMenuItems.SEARCH_ITEM)
    }

    companion object {

        fun getCallingIntent(context: Context, extras: Bundle? = null): Intent {
            val mIntent = Intent(context, FichaPedidoActivity::class.java)
            extras?.let { mIntent.putExtras(it) }
            return mIntent
        }

    }

}
