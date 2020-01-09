package biz.belcorp.consultoras.feature.home.ganamas

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasErrorFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasLandingFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasOffersFragment


class GanaMasPagerAdapter(fm: FragmentManager?,
                          private val listenerFromHome: GanaMasOffersFragment.Listener,
                          private val listenerFromLanding: GanaMasLandingFragment.Listener,
                          private val listenerFromError: GanaMasErrorFragment.Listener,
                          private val flagHideViewsForTesting: Boolean )
    : FragmentPagerAdapter(fm) {

    var offersFragment: GanaMasOffersFragment = GanaMasOffersFragment.newInstance(flagHideViewsForTesting)
    var landingFragment: GanaMasLandingFragment = GanaMasLandingFragment.newInstance(flagHideViewsForTesting)
    var errorFragment: GanaMasErrorFragment = GanaMasErrorFragment.newInstance()

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment? {
        return when(position){
            OFFERS_FRAGMENT -> {
                offersFragment.listener = this.listenerFromHome
                offersFragment
            }
            LANDING_FRAGMENT -> {
                landingFragment.listener = this.listenerFromLanding
                landingFragment
            }
            ERROR_FRAGMENT -> {
                errorFragment.listener = this.listenerFromError
                errorFragment
            }
            else -> null
        }
    }

    companion object {
        const val OFFERS_FRAGMENT = 0
        const val LANDING_FRAGMENT = 1
        const val ERROR_FRAGMENT = 2
    }

}
