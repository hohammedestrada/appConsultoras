package biz.belcorp.consultoras.feature.caminobrillante.feature.tutorial


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class TutorialPagerAdapter(fm: FragmentManager, private val tutorialModelList: List<Fragment>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return tutorialModelList[position]
    }

    override fun getCount(): Int {
        return tutorialModelList.size
    }
}

