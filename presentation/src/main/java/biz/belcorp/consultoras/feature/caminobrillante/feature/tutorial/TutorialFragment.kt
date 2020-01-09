package biz.belcorp.consultoras.feature.caminobrillante.feature.tutorial


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.tutorial.TutorialModel
import kotlinx.android.synthetic.main.fragment_tutorial_camino_brillante.*


class TutorialFragment : BaseFragment() {

    private lateinit var adapter: TutorialPagerAdapter
    lateinit var listenerTutorial: TutorialCaminoBrillanteListener
    private lateinit var tutorialFragment: TutorialCardFragment

    companion object {
        fun newInstance(): TutorialFragment {
            return TutorialFragment()
        }
    }

    override fun context(): Context {
        return activity?.applicationContext!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tutorial_camino_brillante, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TutorialPagerAdapter(childFragmentManager, getTutorialFragmentList(getTutorialModelList()))
        vwpTutorial.adapter = adapter
        vpiTutorial.setupWithViewPager(vwpTutorial)
    }

    private fun getTutorialFragmentList(tutorialModelList: List<TutorialModel>): List<Fragment> {
        val tutorialFragmentList = ArrayList<Fragment>()

        for (tutorialModel in tutorialModelList) {
            tutorialFragment = TutorialCardFragment()
            tutorialFragment.tutorialModel = tutorialModel
            tutorialFragmentList.add(tutorialFragment)
            tutorialFragment.listener = listenerTutorial
        }
        return tutorialFragmentList
    }

    private fun getTutorialModelList(): List<TutorialModel> {
        val tutorialModelList = ArrayList<TutorialModel>()
        val tutorialModel = TutorialModel()

        tutorialModel.title1 = getString(R.string.title_1)
        tutorialModel.descripcion = getString(R.string.description_1)
        tutorialModel.imagen = R.drawable.nivel_y_beneficios

        val tutorialModel2 = TutorialModel()
        tutorialModel2.title1 = getString(R.string.title_2)
        tutorialModel2.descripcion = getString(R.string.description_2)
        tutorialModel2.imagen = R.drawable.pase_de_pedido

        val tutorialModel3 = TutorialModel()
        tutorialModel3.title1 = getString(R.string.title_3)
        tutorialModel3.descripcion = getString(R.string.description_3)
        tutorialModel3.imagen = R.drawable.logros_camino_brillante

        tutorialModelList.add(tutorialModel)
        tutorialModelList.add(tutorialModel2)
        tutorialModelList.add(tutorialModel3)

        return tutorialModelList
    }

    interface TutorialCaminoBrillanteListener {
        fun removeTutorial()
    }

}
