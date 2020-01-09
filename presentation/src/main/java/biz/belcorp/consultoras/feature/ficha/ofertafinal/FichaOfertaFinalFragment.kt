package biz.belcorp.consultoras.feature.ficha.ofertafinal

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.feature.ficha.ofertafinal.FichaOfertaFinalActivity.Companion.EXTRA_PREMIO_DETALLE
import biz.belcorp.consultoras.feature.ficha.ofertafinal.FichaOfertaFinalActivity.Companion.EXTRA_PREMIO_IMG
import biz.belcorp.consultoras.feature.ficha.ofertafinal.FichaOfertaFinalActivity.Companion.EXTRA_PREMIO_NOMBRE
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_premio_ofertafinal.*


class FichaOfertaFinalFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_premio_ofertafinal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
       getData()
    }

    fun getData() {

        val premioImg = arguments?.getString(EXTRA_PREMIO_IMG)
        val premioNomre = arguments?.getString(EXTRA_PREMIO_NOMBRE)
        val premioDetalle = arguments?.getString(EXTRA_PREMIO_DETALLE)

        tvPremioNombre.text = premioNomre
        tvPremioDetalle.text = premioDetalle

        Glide.with(this)
            .asBitmap()
            .load(premioImg)
            .into(ivPremioImg)

    }

    override fun context(): Context {
        return context?.let { it } ?: throw NullPointerException("No hay contexto")
    }

    companion object {
        val TAG: String = FichaOfertaFinalFragment::class.java.simpleName
    }

}
