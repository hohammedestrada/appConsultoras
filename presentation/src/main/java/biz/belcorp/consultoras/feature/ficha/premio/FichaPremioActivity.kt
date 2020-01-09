package biz.belcorp.consultoras.feature.ficha.premio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment
import biz.belcorp.consultoras.feature.ficha.pasepedido.FichaPedidoActivity

class FichaPremioActivity: BaseFichaActivity() {

    override fun generateFichaFragment(): BaseFichaFragment = FichaPremioFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbarIcons(FichaMenuItems.SHARE_ITEM, FichaMenuItems.CART_ITEM, FichaMenuItems.SEARCH_ITEM)
    }

    companion object {

        val REQUEST_CODE_FICHA_PREMIO = 102

        fun getCallingIntent(context: Context, extras: Bundle? = null): Intent {
            val mIntent = Intent(context, FichaPremioActivity::class.java)
            extras?.let { mIntent.putExtras(it) }
            return mIntent
        }

    }


}
