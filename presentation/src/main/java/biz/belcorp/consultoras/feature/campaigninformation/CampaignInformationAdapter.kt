package biz.belcorp.consultoras.feature.campaigninformation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class CampaignInformationAdapter(var fm: FragmentManager, var tabTitles: List<String>) : FragmentStatePagerAdapter(fm) {

    private val fragmentPast = CampaignInformationFragmentPast()
    private val fragmentFuture = CampaignInformationFragmentFuture()

    override fun getItem(position: Int): Fragment? {

        if (position == 0)
            return fragmentPast

        return fragmentFuture
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    fun setArguments(extrasPast : Bundle , extrasFuture : Bundle){
        fragmentPast.arguments = extrasPast
        fragmentFuture.arguments = extrasFuture
    }
}
