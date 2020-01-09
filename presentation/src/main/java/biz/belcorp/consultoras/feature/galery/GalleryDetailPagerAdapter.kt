package biz.belcorp.consultoras.feature.galery

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class GalleryDetailPagerAdapter(val fm : FragmentManager, var pages : ArrayList<GalleryDetailPageFragment>) : FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment? {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }
}
