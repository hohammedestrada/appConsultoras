package biz.belcorp.consultoras.feature.catalog

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.catalog.CatalogModel
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.fragment_catalog.*

class CatalogFragment : BaseFragment() {

    private var listener: CatalogAdapter.CatalogEventListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            init()
        }
    }

    private fun init() {

        val arguments = arguments ?: return

        val campaignName = arguments.getString(GlobalConstant.CAMPAIGN_KEY)
        val catalogs = arguments.getParcelableArrayList<CatalogModel>(GlobalConstant.BOOK_BY_CAMPAIGN_KEY)

        val adapter = CatalogAdapter()
        adapter.setCatalogs(catalogs)

        if (listener != null) adapter.setEventListener(listener)

        tvwCampaignName.text = campaignName
        rvwCatalog.layoutManager = LinearLayoutManager(activity)
        rvwCatalog.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(rvwCatalog, false)
    }

    override fun context(): Context? {
        return context
    }

    companion object {

        fun newInstance(listener: CatalogAdapter.CatalogEventListener): CatalogFragment {
            val fragment = CatalogFragment()
            fragment.listener = listener

            return fragment
        }
    }
}
