package biz.belcorp.consultoras.feature.detalleprogramabrillante

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.app_bar_main.*

class DetalleProgramaBrillanteActivity : BaseActivity() {

    private var fragment: DetalleProgramaBrillanteFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_detalle_camino_brillante)
        init(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        tvw_toolbar_title.setText(R.string.programa_brillante)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }

        fragment  = DetalleProgramaBrillanteFragment()
        fragment?.arguments = intent.extras
        addFragment(R.id.fltContainer, fragment)
    }

    override fun initializeInjector() {
        //empty
    }

    override fun initControls() {
        //empty
    }

    override fun initEvents() {
        //empty
    }
}
