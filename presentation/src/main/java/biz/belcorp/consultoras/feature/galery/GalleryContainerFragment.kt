package biz.belcorp.consultoras.feature.galery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.feature.galery.di.GaleryComponent
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.fragment_galery_container.*
import javax.inject.Inject

class GalleryContainerFragment : BaseFragment(), GalleryView, LoadingView, GalleryVerticalListAdapter.VerticalGaleryListener, GalleryFilterSectionAdapter.sectionListener {

    @Inject
    lateinit var presenter: GalleryContainerPresenter

    companion object{
        const val CONCAT_GALLERY = " | "
        const val FILTER_BY_GALLERY = "Filtrar por"
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(GaleryComponent::class.java).inject(this)
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_galery_container, container, false)
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        init()
    }

    override fun onGetGalerySuccess(list: ArrayList<SectionGalleryItemModel>) {
        activity?.let {
            it.runOnUiThread {
                (rcvw.adapter as GalleryVerticalListAdapter).setData(list)
                showlayoutSuccess()
            }
        }
    }

    override fun onGetFiltrosSuccess(list: ArrayList<SectionGalleryFilterItemModel>) {
        activity?.let {
            it.runOnUiThread {
                (rcvwFiltros.adapter as GalleryFilterSectionAdapter).setData(list)
            }
        }
    }

    override fun onGetGaleryNoItems() {
        activity?.let {
            it.runOnUiThread {
                showlayoutNoItem()
            }
        }
    }

    override fun onGetGaleryFails() {
        activity?.let {
            it.runOnUiThread {
                showlayoutFail()
            }
        }
    }

    override fun showLoading() {
        super.showLoading()
        activity?.let {
            it.runOnUiThread {
                view_loading.visibility = View.VISIBLE
            }
        }
    }

    override fun hideLoading() {
        super.hideLoading()
        activity?.let {
            it.runOnUiThread {
                view_loading.visibility = View.GONE
            }
        }
    }

    private fun init() {
        rcvw.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rcvw.adapter = GalleryVerticalListAdapter(this@GalleryContainerFragment)

        rcvwFiltros.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rcvwFiltros.adapter = GalleryFilterSectionAdapter(this@GalleryContainerFragment)

        btnRetry.setOnClickListener {
            presenter.start()
            presenter.user?.let { u ->
                Tracker.Gallery.trackVolverIntentar(btnRetry.text.toString(), u)
            }
        }

        btnGoFilters.setOnClickListener {
            layoutFiltro.visibility = View.VISIBLE
            presenter.user?.let {u ->
                Tracker.Gallery.trackBtnFiltrosClic(u)
            }
        }

        btnCloseFilters.setOnClickListener {
            layoutFiltro.visibility = View.GONE
        }

        btnApplyFilters.setOnClickListener {
            rcvwFiltros?.adapter?.let {
                layoutFiltro.visibility = View.GONE

                val selectedFilters = (rcvwFiltros.adapter as GalleryFilterSectionAdapter).selectedFilters()

                (rcvw.adapter as GalleryVerticalListAdapter).filter(selectedFilters)

                if(selectedFilters.isEmpty()){
                    txtBtnFiltros.text = resources.getString(R.string.filtros)
                }else{
                    txtBtnFiltros.text = "${resources.getString(R.string.filtros)} (${selectedFilters.size})"

                    presenter.user?.let {u ->
                        var nameSelectedFilter = ""

                        selectedFilters.forEachIndexed {index, it ->
                            if(index == (selectedFilters.size - 1)){
                                nameSelectedFilter += it.Descripcion
                            }else {
                                nameSelectedFilter += it.Descripcion + CONCAT_GALLERY
                            }
                        }

                        Tracker.Gallery.trackAplicarFiltrar(nameSelectedFilter, u)
                    }
                }
            }
        }

        btnCleanFilters.setOnClickListener {
            rcvwFiltros?.adapter?.let {
                (rcvw.adapter as GalleryVerticalListAdapter).clean()
                (rcvwFiltros.adapter as GalleryFilterSectionAdapter).clean()

                layoutFiltro.visibility = View.GONE
                layout_success.visibility = View.VISIBLE
                txtBtnFiltros.text = resources.getString(R.string.filtros)

                presenter.user?.let {u ->
                    Tracker.Gallery.trackLimpiarFiltrar(u)
                }
            }
        }
    }

    private fun showlayoutSuccess() {
        activity?.let {
            it.runOnUiThread {
                layout_success.visibility = View.VISIBLE
                layout_fail.visibility = View.GONE
                layout_no_items.visibility = View.GONE
            }
        }
    }

    private fun showlayoutFail() {
        activity?.let {
            it.runOnUiThread {
                layout_success.visibility = View.GONE
                layout_fail.visibility = View.VISIBLE
                layout_no_items.visibility = View.GONE
            }
        }
    }

    private fun showlayoutNoItem() {
        activity?.let {
            it.runOnUiThread {
                layout_success.visibility = View.GONE
                layout_fail.visibility = View.GONE
                layout_no_items.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemSelected(position: Int, items: ArrayList<ListadoImagenModel>?) {
        activity?.let {
            presenter.user?.let { u ->
                items?.let {
                    Tracker.Gallery.trackImageClic(it[position].titulo,u)
                }
            }

            val extras = Bundle()
            extras.putInt(GlobalConstant.POSITION_CURRENT_ITEM_GALLERY, position)
            extras.putParcelableArrayList(GlobalConstant.ITEMS_GALLERY, items)

            val intent = Intent(it, GalleryDetailActivity::class.java)
            intent.putExtras(extras)
            startActivity(intent)
        }
    }

    override fun onMarcarAnalytics(tipoFiltro: String, label: String) {
        presenter.user?.let {u ->
            Tracker.Gallery.trackFiltrar("$FILTER_BY_GALLERY $tipoFiltro", label, u)
        }
    }
}
