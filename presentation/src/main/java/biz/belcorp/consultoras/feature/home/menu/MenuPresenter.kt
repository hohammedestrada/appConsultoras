package biz.belcorp.consultoras.feature.home.menu

import java.util.Collections

import javax.inject.Inject

import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.menu.MenuModel
import biz.belcorp.consultoras.common.model.menu.MenuModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.UserConfigData
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.MenuUseCase
import biz.belcorp.consultoras.util.MenuPosition
import biz.belcorp.consultoras.util.anotation.MenuCode
import biz.belcorp.consultoras.util.anotation.UserConfigAccountCode

@PerActivity
class MenuPresenter @Inject
internal constructor(private val menuUseCase: MenuUseCase,
                     private val menuModelDataMapper: MenuModelDataMapper,
                     private val accountUseCase: AccountUseCase) : Presenter<MenuView> {

    private var menuView: MenuView? = null

    override fun attachView(view: MenuView) {
        this.menuView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.menuUseCase.dispose()
        this.menuView = null
    }

    /**
     * @param posicionMenu menu type
     */
    operator fun get(posicionMenu: Int?) {
        menuUseCase.getFromDatabase(posicionMenu, GetMenuListObserver(posicionMenu))
    }


    private inner class GetMenuListObserver internal constructor(private var posicionMenu: Int?) : BaseObserver<List<Menu?>?>() {

        override fun onNext(menList: List<Menu?>?) {
            menList?.let {
                it.filterNotNull().let { menuList ->
                    val menuModelList = menuModelDataMapper.transformToDomainModel(menuList).toMutableList()
                    if (posicionMenu == MenuPosition.LATERAL) {
                        val versionItem = MenuModel()
                        versionItem.subMenus = mutableListOf()
                        versionItem.descripcion = "Versión " + BuildConfig.VERSION_NAME

                        menuModelList.add(versionItem)
                    }

                    accountUseCase.getConfigActiveWithUpdate(UserConfigAccountCode.MENU_OFF, ConfigMenuObserver(menuModelList))
                    posicionMenu = null
                }
            }
        }
    }

    private inner class ConfigMenuObserver internal constructor(private val menuModelList: List<MenuModel>) : BaseObserver<Collection<UserConfigData?>?>() {

        override fun onNext(userConfigData: Collection<UserConfigData?>?) {
            super.onNext(userConfigData)

            val listFilter = userConfigData?.filter { it?.code == CONFIG_ESIKA_AHORA }

            val list = menuModelList

            if (listFilter?.isEmpty() == true) {
                list.forEach { it1 ->
                    if (it1.codigo?.startsWith(PREFIX_MEN_LAT) == true) {

                        it1.subMenus.removeAll { it.codigo == MenuCode.MEN_ESIKA_AHORA}
                    }
                }
            }

            menuView?.showMenuList(list)
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            menuView?.showMenuList(menuModelList)
        }
    }

    companion object {
        private const val PREFIX_MEN_LAT = "MEN_LAT"
        private const val CONFIG_ESIKA_AHORA = "ESIKA_AHORA"
    }
}
