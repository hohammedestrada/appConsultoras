package biz.belcorp.consultoras.feature.caminobrillante.feature.tutorial


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.tutorial.TutorialModel
import biz.belcorp.mobile.components.design.button.Button
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_tutorial_card_camino_brillante.*


class TutorialCardFragment : BaseFragment() {

    private var unbinder: Unbinder? = null
    var tutorialModel: TutorialModel? = null
    var listener: TutorialFragment.TutorialCaminoBrillanteListener? = null

    override fun context(): Context {
        return activity?.applicationContext!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tutorial_card_camino_brillante, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorialModel?.let {
            title_tutorial_camino_brillante.text = it.title1
            tutorial_description_camino_brillante.text = it.descripcion

            Glide.with(this).load(it.imagen).into(tutorial_image_camino_brillante)
        }

        btn_omitir_camino_brillante.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                listener?.removeTutorial()
            }
        }
    }

    override fun onDestroyView() {
        unbinder?.unbind()
        super.onDestroyView()
    }


}
