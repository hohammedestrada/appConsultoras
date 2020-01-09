package biz.belcorp.consultoras.feature.catalog

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import javax.inject.Inject

import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.feature.catalog.di.CatalogComponent
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.CommunicationUtils
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.fragment_catalog_main.*

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class CatalogContainerFragment : BaseFragment(), CatalogContainerView, CatalogAdapter.CatalogEventListener, CatalogContainerAdapter.CatalogContainerAdapterListener {

    companion object {

        fun newInstance(): CatalogContainerFragment {
            return CatalogContainerFragment()
        }
    }

    @Inject
    lateinit var presenter: CatalogContainerPresenter

    private var adapter: CatalogContainerAdapter? = null

    private var refreshDataHandler: Handler? = null
    private var refreshDataRunnable: Runnable? = null

    private var tfRegular: Typeface? = null
    private var loginModel: LoginModel? = null

    private var listener: OnFragmentListener? = null

    val currentPage: Int
        get() = vwpCatalogs.currentItem

    override fun onInjectView(): Boolean {
        getComponent(CatalogComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        presenter.loadBooks()
        presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE)
        initHandler()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentListener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()

        if (refreshDataHandler == null) initHandler()

        refreshDataHandler?.removeCallbacks(refreshDataRunnable)
        refreshDataHandler?.postDelayed(refreshDataRunnable, 200)
    }

    override fun onGetMenu(menu: Menu) {
        listener?.onGetMenu(menu)
    }

    override fun getPdfUrlSuccess(url: String, title: String) {
        activity?.let {
            it.runOnUiThread {
                val request = DownloadManager.Request(Uri.parse(url.replace("\"", "")))
                    .setTitle(getString(R.string.catalog_download))
                    .setDescription(title)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title.replace(" ", "_") + ".pdf")
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

                if (Build.VERSION.SDK_INT >= 24) {
                    request.setRequiresCharging(false)
                }

                val downloadManager = it.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
            }
        }
    }

    override fun getPdfUrlFail() {
        activity?.let {
            it.runOnUiThread { Toast.makeText(activity, R.string.catalog_url_download_fail, Toast.LENGTH_LONG).show() }
        }
    }

    override fun showSearchOption() {
        (activity as CatalogContainerActivity).showSearchOption()
    }


    override fun initScreenTrack(loginModel: LoginModel, type: Int) {
        this.loginModel = loginModel

        val screenName = getCurrentScreenName(type)

        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(loginModel)

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties)
    }

    private fun init() {
        tfRegular = Typeface.createFromAsset(context()?.assets, GlobalConstant.LATO_REGULAR_SOURCE)

        initControls()
        changeTabsFont()
    }

    private fun initControls() {
        adapter = CatalogContainerAdapter(childFragmentManager, this, arguments)
        adapter?.setCatalogContainerAdapterListener(this)
        vwpCatalogs?.offscreenPageLimit = 2
        vwpCatalogs?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // EMPTY
            }

            override fun onPageSelected(position: Int) {
                presenter.initScreenTrack(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // EMPTY
            }
        })

        tblHeader?.tabMode = TabLayout.MODE_FIXED
        tblHeader?.setupWithViewPager(vwpCatalogs)
    }

    private fun initHandler() {
        refreshDataHandler = Handler()
        refreshDataRunnable = Runnable {
            vwpCatalogs?.let {
                presenter.initScreenTrack(it.currentItem)
            }
        }
    }

    private fun changeTabsFont() {
        val vg = tblHeader.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = tfRegular
                    tabViewChild.textSize = 12f
                }
            }
        }
    }

    override fun context(): Context? {
        val activity = activity

        return activity?.applicationContext
    }

    override fun showCatalog(catalogs: List<CatalogByCampaignModel>, paisISO: String, user: User) {

        if (user.isBrillante) {
            context?.let {
                user.isBrillante = SessionManager.getInstance(it).getDownloadPdfCatalog() ?: false
            }
        }

        adapter?.setCatalogs(catalogs, paisISO, user)
    }

    override fun showMagazine(magazines: List<CatalogByCampaignModel>) {
        adapter?.setMagazines(magazines)
    }

    override fun initializeAdapter(countSize: Int) {
        if (countSize != 0) {
            vwpCatalogs?.adapter = adapter
        } else {
            if (NetworkUtil.isThereInternetConnection(context()))
                tvwMessage.setText(R.string.catalog_no_items)
            else
                tvwMessage.setText(R.string.catalog_no_items_conexion)
            tvwMessage.visibility = View.VISIBLE

            lltContent.visibility = View.GONE
            lltNoCatalogs.visibility = View.VISIBLE
        }
    }

    override fun trackBackPressed(loginModel: LoginModel, type: Int) {
        val screenName = getCurrentScreenName(type)

        val analytics = Bundle()
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName)
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK)
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK)
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE)
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(loginModel)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties)

        listener?.onBackFromFragment()

    }

    override fun showError(throwable: Throwable) {
        processError(throwable)
    }

    fun trackBackPressed() {
        presenter.trackBackPressed(currentPage)
    }

    override fun trackEvent(category: String, action: String, label: String, eventName: String) {

        val analytics = Bundle()
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, getCurrentScreenName(currentPage))
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, category)
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, action)
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, label)
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(loginModel)

        BelcorpAnalytics.trackEvent(eventName, analytics, properties)
    }

    override fun hiddenTab() {
        tblHeader.visibility = View.GONE
    }

    fun getCurrentScreenName(page: Int) = when (page) {
        0 -> GlobalConstant.SCREEN_CATALOG_CATALOG
        1 -> GlobalConstant.SCREEN_CATALOG_MAGAZINE
        else -> GlobalConstant.SCREEN_CATALOG_CATALOG
    }

    override fun onDownloadPDFRequest(descripcion: String, title: String) {
        downloadPdfWithPermissionCheck(descripcion, title)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun downloadPdf(descripcion: String?, title: String?) {
        presenter.downLoadCatalog(descripcion, title)
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onShowRationaleDownloadPdf(request: PermissionRequest) {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_write_rationale)
                .setPositiveButton(R.string.button_aceptar) { _, _ -> request.proceed() }
                .setNegativeButton(R.string.button_cancelar) { _, _ -> request.cancel() }
                .setCancelable(false)
                .show()
        }
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onDownloadPdfNeverAskAgain() {
        context?.let {
            Toast.makeText(it, R.string.permission_write_neverask, Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_write_denied)
                .setPositiveButton(R.string.button_go_to_settings) { _, _ ->
                    CommunicationUtils.goToSettings(it)
                }
                .setNegativeButton(R.string.button_cancelar) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    interface OnFragmentListener {
        fun onGetMenu(menu: Menu)

        fun onBackFromFragment()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
