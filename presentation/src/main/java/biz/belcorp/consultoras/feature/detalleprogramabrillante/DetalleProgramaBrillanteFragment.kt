package biz.belcorp.consultoras.feature.detalleprogramabrillante

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.fragment_detalle_programa_brillante.*

class DetalleProgramaBrillanteFragment : BaseFragment() {

    override fun onInjectView(): Boolean {
        return super.onInjectView()
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detalle_programa_brillante, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    private fun init() {
        btnVolver.setOnClickListener {
            activity?.finish()
        }

        val niveles: ArrayList<NivelModel>? = arguments?.getParcelableArrayList(GlobalConstant.CONTEST_BRIGHT_PATH_LEVELS_KEY)

        showLevels(niveles)
    }

    private fun showLevels(niveles: ArrayList<NivelModel>?) {
        niveles?.let {niv ->
            if(niv.size > 0){
                rvwBrightPathLevels.visibility = View.VISIBLE
                rvwBrightPathLevels.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                rvwBrightPathLevels.adapter = ProgramaBrillanteNivelesAdapter(niv)
            }
        }
    }
}
