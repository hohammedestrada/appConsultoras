package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity
import biz.belcorp.consultoras.feature.caminobrillante.di.CaminoBrillanteComponent
import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.demostrador.DemostradorFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.kit.KitFragment
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel
import kotlinx.android.synthetic.main.fragment_ofertas_especiales.*
import javax.inject.Inject

class OfertasEspecialesFragment : BaseFragment(), OfertasEspecialesView {

    @Inject
    lateinit var presenter: OfertasEspecialesPresenter

    lateinit var listener: OfertasEspecialesListener

    lateinit var filtros: ArrayList<CategoryFilterModel>

    private lateinit var kitFragment: KitFragment
    private lateinit var demostradorFragment: DemostradorFragment

    private var isDemostradoresVisible = true

    companion object {
        fun newInstance(): OfertasEspecialesFragment {
            return OfertasEspecialesFragment()
        }
    }

    override fun context(): Context? {
        return activity?.applicationContext
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CaminoBrillanteActivity) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ofertas_especiales, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(CaminoBrillanteComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        init()
    }

    fun init() {
        presenter.getUser()
        kitFragment = KitFragment.newInstance()
        demostradorFragment = DemostradorFragment.newInstance()

        viewPager.adapter = TabPagerAdapter(this).apply {

            when {
                isDemostradoresVisible -> {
                    addFragment(kitFragment, getString(R.string.ofertas_especiales_kits))
                    addFragment(demostradorFragment, getString(R.string.ofertas_especiales_demostradores))
                }
                else -> addFragment(kitFragment, getString(R.string.ofertas_especiales_kits))
            }

        }

        tablayout.setupWithViewPager(viewPager)
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> kitFragment.onSelectTab()
                    1 -> demostradorFragment.onSelectTab()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("OfertasEspeciales", "onTabReselected")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("OfertasEspeciales", "onTabUnselected")
            }
        })
    }

    override fun setUser(user: User) {
        Tracker.trackView(GlobalConstant.SCREEN_NIVEL_Y_BENEFICIOS_PRODUCTOS, user)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    //region TabAdapter
    class TabPagerAdapter(val fragment: Fragment) : FragmentPagerAdapter(fragment.childFragmentManager) {

        val fragments = mutableListOf<Fragment>()
        private val titles = mutableListOf<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }

        fun removeFragment(fragment: Fragment, title: String) {
            fragments.remove(fragment)
            titles.remove(title)
        }
    }
    //endregion

    override fun reloadOffers() {
        listener.resetIsLoading()
        if (isDemostradoresVisible) {
            kitFragment.loadKits()
            demostradorFragment.loadDemostradores()
        } else {
            kitFragment.loadKits()
        }
    }


    override fun getCurrentPosition(): Int {
        return viewPager.currentItem
    }

    override fun getSizeCount(): Int {
        return viewPager.childCount
    }

    interface OfertasEspecialesListener {
        fun goToOffers()
        fun resetIsLoading()
        fun goToOrders(menu: Menu)
    }

}
