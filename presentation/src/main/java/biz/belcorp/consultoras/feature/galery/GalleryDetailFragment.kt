package biz.belcorp.consultoras.feature.galery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.galery.di.GalleryDetailComponent
import kotlinx.android.synthetic.main.fragment_gallery_detail.*
import javax.inject.Inject

class GalleryDetailFragment : BaseFragment(), GalleryDetailView, GalleryDetailPageFragment.onSaveImage{

    @Inject
    lateinit var presenter: GalleryDetailPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(GalleryDetailComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        start()
    }

    override fun context(): Context? {
        return activity
    }

    override fun save(action : String, imageName: String) {
        Tracker.Gallery.trackSaveImage(action, imageName, presenter.user)
    }

    fun start() {
        arguments?.let {args ->
            if(args.containsKey(GlobalConstant.ITEMS_GALLERY)){
                val pages = ArrayList<GalleryDetailPageFragment>()

                val items = args.getParcelableArrayList<ListadoImagenModel>(GlobalConstant.ITEMS_GALLERY)

                items?.let {
                    pagerDetail.offscreenPageLimit = it.size

                    it.filterNotNull().forEach { item ->
                        val bundle = Bundle()
                        bundle.putParcelable(GlobalConstant.ITEM_SELECTED_GALLERY, item)
                        bundle.putString(GlobalConstant.GALLERY_TITULO_IMAGE_SELECTED, args.getString(GlobalConstant.GALLERY_TITULO_IMAGE_SELECTED))

                        val fragment = GalleryDetailPageFragment()
                        fragment.arguments = bundle
                        fragment.setListener(this@GalleryDetailFragment)
                        pages.add(fragment)
                    }
                }

                pagerDetail.adapter = GalleryDetailPagerAdapter(childFragmentManager, pages)

                if(args.containsKey(GlobalConstant.POSITION_CURRENT_ITEM_GALLERY)){
                    pagerDetail.currentItem = args.getInt(GlobalConstant.POSITION_CURRENT_ITEM_GALLERY, 0)
                }
            }
        }
    }
}
