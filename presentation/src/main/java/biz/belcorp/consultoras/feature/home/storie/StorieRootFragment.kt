package biz.belcorp.consultoras.feature.home.storie

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.stories.StorieModel
import biz.belcorp.consultoras.feature.home.storie.di.StorieCompound
import biz.belcorp.consultoras.util.GlobalConstant
import android.support.v4.app.Fragment
import android.view.MotionEvent
import biz.belcorp.consultoras.common.component.stories.customstoriepages.ProgressbarPausable
import biz.belcorp.consultoras.util.StorieUtils
import kotlinx.android.synthetic.main.fragment_root_storie.*
import javax.inject.Inject
import android.support.v4.view.ViewPager
import android.util.Log
import biz.belcorp.consultoras.util.AdRedirectUtil
import biz.belcorp.consultoras.util.anotation.RedirectionStories
import android.support.constraint.ConstraintSet


class StorieRootFragment : BaseFragment(),
    ProgressbarPausable.CallbackProgress, StorieFragmentContent.IStorieFragment, StorieView, ViewPager.OnPageChangeListener {

    private var detailModel: StorieModel? = null
    private var currentPosition = 0
    private var listProgress: MutableList<ProgressbarPausable> = mutableListOf()
    @Inject
    lateinit var presenter: StoriePresenter
    lateinit var adapter: StoriePagesAdapter

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(StorieCompound::class.java).inject(this)
        return true
    }

    override fun context(): Context? = activity?.applicationContext

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        init()
    }

    private val onTouchListener = View.OnTouchListener { _, event ->

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                PRESSTIME = System.currentTimeMillis()
                if (currentPosition >= listProgress.size)
                    listProgress[listProgress.size - 1].pauseCountDownBar()
                else {
                    listProgress[currentPosition].pauseCountDownBar()
                }
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                if (currentPosition >= listProgress.size) {
                    listProgress[listProgress.size - 1].continuarCountDown()
                } else {
                    listProgress[currentPosition].continuarCountDown()
                }
                return@OnTouchListener LIMIT < now - PRESSTIME
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_root_storie, container, false)

    fun close() {
        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }

    }

    fun init() {
        arguments?.let {
            detailModel = it.getParcelable(GlobalConstant.STORIE_MODEL)

        }

        listProgress.forEach {
            it.clearBar()
        }

        detailModel?.let { modelo ->

            val fragments: MutableList<Fragment> = buildFragments(modelo)
            currentPosition = StorieUtils.calcularIndiceInicio(modelo)
            adapter = StoriePagesAdapter(context, fragmentManager, fragments, this)
            viewpager.adapter = adapter
            viewpager.currentItem = currentPosition


            siguienteStorie.setOnTouchListener(onTouchListener)
            anteriorStorie.setOnTouchListener(onTouchListener)
            viewpager.addOnPageChangeListener(this)

            agregarProgress()

            anteriorStorie.setOnClickListener { it1 ->

                enableDisableView(false, it1)
                enableDisableView(false, siguienteStorie)
                setEmptyProgressActualPosition(currentPosition)
                if (--currentPosition < 0) {
                    currentPosition = 0
                    listProgress[currentPosition].start()
                }
                viewpager.currentItem = currentPosition
                enableDisableView(true, siguienteStorie)
                enableDisableView(true, it1)

            }

            siguienteStorie.setOnClickListener { it2 ->
                enableDisableView(false, it2)
                enableDisableView(false, anteriorStorie)
                setFullProgressActualPosition(currentPosition)
                gestionarSiguienteStorie()

                enableDisableView(true, anteriorStorie)
                enableDisableView(true, it2)

            }

            setFullprogressBefore()

            listProgress[currentPosition].start()

            modelo.contenidoDetalle?.let{contenidoDetalleValidate ->
                if(contenidoDetalleValidate.isNotEmpty()){
                    contenidoDetalleValidate[currentPosition]?.let{
                        if (it.descargado) {
                            saveStorieSeen(currentPosition)
                        }
                    }
                }
            }

            imgBtnCloseStorie.setOnClickListener {
                close()
            }
        }
    }

    private fun buildFragments(history: StorieModel): MutableList<Fragment> {
        val listFragments = ArrayList<Fragment>()

        history.contenidoDetalle?.let{contenidoDetalleValidate ->
            if (contenidoDetalleValidate.isNotEmpty()){
                for (i in 0 until contenidoDetalleValidate.size) {
                    val b = Bundle()
                    b.putParcelable(GlobalConstant.STORIE_UNIQUE, contenidoDetalleValidate[i])
                    listFragments.add(instantiate(context, StorieFragmentContent::class.java.name, b))
                }
            }
        }

        return listFragments
    }

    private fun agregarProgress() {

        detailModel?.contenidoDetalle?.let {
            it.map {
                listProgress.add(ProgressbarPausable(context!!))
            }
        }

        for (i in 0 until listProgress.size) {
            listProgress[i].setDuration(GlobalConstant.STORIE_DURATION_IMG)
            listProgress[i].storieCallback = this
            linearProgress.addView(listProgress[i])
        }

    }

    private fun setFullprogressBefore() {
        for (i in 0 until currentPosition) {
            listProgress[i].setFullProgress()
        }
    }

    private fun gestionarSiguienteStorie() {
        detailModel?.let {
            if (viewpager != null) {
                val positionCalculated = ++currentPosition
                it.contenidoDetalle?.let{contenidoDetalleValidate ->
                    if (contenidoDetalleValidate.isNotEmpty()){
                        if (positionCalculated >= contenidoDetalleValidate.size) {
                            close()
                        } else {
                            currentPosition = positionCalculated
                            viewpager.currentItem = currentPosition
                        }
                    }
                }

            }
        }
    }

    private fun setFullProgressActualPosition(pos: Int) {

        if (pos >= listProgress.size) {
            listProgress[listProgress.size - 1].setFullProgress()
        } else
            listProgress[pos].setFullProgress()
    }

    private fun setEmptyProgressActualPosition(pos: Int) {
        val position = when {
            pos <= 0 -> 0
            pos >= listProgress.size -> listProgress.size - 1
            else -> pos
        }
        listProgress[position].setEmptyProgress()
    }

    private fun enableDisableView(status: Boolean, view: View) {
        view.isClickable = status
        view.isEnabled = status
    }

    fun saveStorieSeen(position: Int) {
        detailModel?.let {detailModelValidate ->
            detailModelValidate.contenidoDetalle?.let{
                it[position]?.let { it1 ->
                    presenter.saveStateStorie(it1.idContenido)
                }
            }
        }
    }

    override fun onFinishProgreBar() {
        gestionarSiguienteStorie()
    }

    override fun redirect(donde: String) {
        context?.let {
            AdRedirectUtil.open(it, donde)
            activity?.finish()
        }
    }

    /**Metodos del ViewPager**/
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        listProgress[position].start()
        saveStorieSeen(position)

    }

    companion object {
        private var PRESSTIME = 0L
        private var LIMIT = 500L
    }
}
