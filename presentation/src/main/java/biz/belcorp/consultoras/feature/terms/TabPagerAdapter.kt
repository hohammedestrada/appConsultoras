package biz.belcorp.consultoras.feature.terms

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import biz.belcorp.consultoras.feature.vinculacion.VinculacionFragment

/**
 * @author andres.escobar on 21/08/2017.
 */
internal class TabPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val vinculacionFragment = VinculacionFragment()
    val termsFragment = TermsFragment()

    override fun getItem(position: Int): Fragment? {
        return when(position){
            0 -> {
                vinculacionFragment
            }
            1 -> {
                termsFragment
            }
            else ->{
                termsFragment
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

}
