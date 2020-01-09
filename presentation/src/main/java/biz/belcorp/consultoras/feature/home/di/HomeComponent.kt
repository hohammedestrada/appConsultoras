package biz.belcorp.consultoras.feature.home.di

import biz.belcorp.consultoras.common.notification.home.OrderNotification
import biz.belcorp.consultoras.common.notification.home.postulant.PostulantNotification
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.catalog.CatalogContainerFragment
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.HomeFragment
import biz.belcorp.consultoras.feature.home.clients.ClientsListFragment
import biz.belcorp.consultoras.feature.home.clients.all.AllClientsFragment
import biz.belcorp.consultoras.feature.home.clients.favorites.FavoriteClientsFragment
import biz.belcorp.consultoras.feature.home.clients.pedido.PedidoClientsFragment
import biz.belcorp.consultoras.feature.home.clients.porcobrar.PorCobrarFragment
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasLandingFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasOffersFragment
import biz.belcorp.consultoras.feature.home.incentives.GiftActiveFragment
import biz.belcorp.consultoras.feature.home.incentives.GiftHistoryFragment
import biz.belcorp.consultoras.feature.home.incentives.IncentivesContainerFragment
import biz.belcorp.consultoras.feature.home.menu.lateral.MenuLateral
import biz.belcorp.consultoras.feature.home.menu.top.MenuTop
import biz.belcorp.consultoras.feature.home.tracking.TrackingFragment
import biz.belcorp.consultoras.feature.embedded.offers.OffersFragment
import biz.belcorp.consultoras.feature.home.updatemail.UpdateMailDialog
import dagger.Component
import biz.belcorp.consultoras.feature.home.survey.SurveyBottomDialogFragment



@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class, HomeModule::class])
interface HomeComponent : ActivityComponent {

    fun inject(homeFragment: HomeFragment)

    fun inject(clientsListFragment: ClientsListFragment)

    fun inject(allClientsFragment: AllClientsFragment)

    fun inject(porCobrarFragment: PorCobrarFragment)

    fun inject(favoriteClientsFragment: FavoriteClientsFragment)

    fun inject(pedidoClientsFragment: PedidoClientsFragment)

    fun inject(offersFragment: OffersFragment)

    fun inject(incentivesContainerFragment: IncentivesContainerFragment)

    fun inject(giftConstancyFragment: GiftActiveFragment)

    fun inject(giftHistoryFragment: GiftHistoryFragment)

    fun inject(menuTop: MenuTop)

    fun inject(menuLateral: MenuLateral)

    fun inject(baseHomeFragment: BaseHomeFragment)

    fun inject(fragment: CatalogContainerFragment)

    fun inject(view: OrderNotification)

    fun inject(view: PostulantNotification)

    fun inject(view: TrackingFragment)

    fun inject(dialog: UpdateMailDialog)

    fun inject(fragment: GanaMasFragment)

    fun inject(fragment: GanaMasOffersFragment)

    fun inject(fragment: GanaMasLandingFragment)

    fun inject(fragment: SurveyBottomDialogFragment)
}
