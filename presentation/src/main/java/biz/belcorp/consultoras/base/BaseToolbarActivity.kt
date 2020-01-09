package biz.belcorp.consultoras.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import biz.belcorp.consultoras.R

abstract class BaseToolbarActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.inflateMenu(R.menu.search_menu)
    }

}
