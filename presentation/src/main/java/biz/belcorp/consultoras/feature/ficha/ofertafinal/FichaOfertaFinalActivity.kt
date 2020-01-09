package biz.belcorp.consultoras.feature.ficha.ofertafinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity

class FichaOfertaFinalActivity: BaseActivity() {

    private var fragment: FichaOfertaFinalFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        init(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragment = FichaOfertaFinalFragment()
            fragment?.arguments = intent.extras
            supportFragmentManager.beginTransaction()
                .add(R.id.fltContainer, fragment, FichaOfertaFinalFragment.TAG)
                .commit()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    companion object {

        const val EXTRA_PREMIO_IMG = "EXTRA_PREMIO_IMG"
        const val EXTRA_PREMIO_NOMBRE = "EXTRA_PREMIO_NOMBRE"
        const val EXTRA_PREMIO_DETALLE = "EXTRA_PREMIO_DETALLE"

        fun getCallingIntent(context: Context, extras: Bundle? = null): Intent {
            val mIntent = Intent(context, FichaOfertaFinalActivity::class.java)
            extras?.let { mIntent.putExtras(it) }
            return mIntent
        }
    }

    override fun initControls() {
        /* Empty */
    }

    override fun initEvents() {
        /* Empty */
    }

    override fun initializeInjector() {
        /* Empty */
    }
}
