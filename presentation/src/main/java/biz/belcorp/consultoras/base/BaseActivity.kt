package biz.belcorp.consultoras.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.view.View

import com.orhanobut.logger.Logger

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import javax.inject.Inject

import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.navigation.Navigator
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.StringUtil
import java.lang.Exception
import com.uxcam.UXCam;
import io.github.inflationx.viewpump.ViewPumpContextWrapper

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator
    /** */

    protected val appComponent: AppComponent
        get() = (application as ConsultorasApp).appComponent

    protected val activityModule: ActivityModule
        get() = ActivityModule(this)

    /** */

    val visibleFragment: Fragment?
        get() {
            val fragmentManager = supportFragmentManager
            return fragmentManager.findFragmentById(R.id.fltContainer)
        }

    /** */

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.appComponent.inject(this)
        generateHash()
        UXCam.startWithKey(UXCAM_KEY)
        UXCam.setAutomaticScreenNameTagging(false)
    }

    /** */

    protected abstract fun init(savedInstanceState: Bundle?)

    protected abstract fun initializeInjector()

    protected abstract fun initControls()

    protected abstract fun initEvents()

    /** */

    fun context(): Context {
        return applicationContext
    }

    @JvmOverloads
    protected fun addFragment(containerViewId: Int, fragment: Fragment?, isFirst: Boolean = true, name: String = "") {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if (StringUtil.isNullOrEmpty(name))
            fragmentTransaction.add(containerViewId, fragment)
        else
            fragmentTransaction.add(containerViewId, fragment, name)
        if (!isFirst)
            fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @JvmOverloads
    protected fun replaceFragment(containerViewId: Int, fragment: Fragment, isFirst: Boolean, name: String = "", transicion: Boolean = true) {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        if (transicion) fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if (StringUtil.isNullOrEmpty(name)) {
            fragmentTransaction.replace(containerViewId, fragment)
        } else {
            fragmentTransaction.replace(containerViewId, fragment, name)
        }
        if (!isFirst)
            fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    /** */

    @SuppressLint("PackageManagerGetSignatures")
    private fun generateHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Logger.i("KEY HASH :" + Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.e(e.message)
        } catch (e: NoSuchAlgorithmException) {
            Logger.e(e.message)
        }
    }

    companion object {
        const val UXCAM_KEY = "idz2mu8waiy7m3j"
    }
}
