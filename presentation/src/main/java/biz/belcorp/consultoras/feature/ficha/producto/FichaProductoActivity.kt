package biz.belcorp.consultoras.feature.ficha.producto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment

class FichaProductoActivity : BaseFichaActivity() {

    override fun generateFichaFragment(): BaseFichaFragment = FichaProductoFragment()

    override fun extraActionOnReceive() {

        (fragment as? FichaProductoFragment)?.let { fichaProductoFragment ->

            val hasDeletedItem =
                (ordersCount ?: 0) > (SessionManager.getInstance(this@FichaProductoActivity).getOrdersCount() ?: 0)

            fichaProductoFragment.checkForRestoreSimpleButton(hasDeletedItem)

        }

    }

    companion object {

        fun getCallingIntent(context: Context, extras: Bundle? = null): Intent {
            val mIntent = Intent(context, FichaProductoActivity::class.java)
            extras?.let { mIntent.putExtras(it) }
            return mIntent
        }

    }

}
