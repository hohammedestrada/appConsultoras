package biz.belcorp.consultoras.feature.ficha.caminobrillante

import android.content.Context
import android.content.Intent
import android.os.Bundle
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment

class FichaCaminoBrillanteActivity : BaseFichaActivity() {

    override fun generateFichaFragment(): BaseFichaFragment = FichaCaminoBrillanteFragment()

    companion object {

        fun getCallingIntent(context: Context, extras: Bundle? = null): Intent {
            val mIntent = Intent(context, FichaCaminoBrillanteActivity::class.java)
            extras?.let { mIntent.putExtras(it) }
            return mIntent
        }

    }

}
