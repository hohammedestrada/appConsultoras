package biz.belcorp.consultoras.feature.client.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.client.card.ClientCardFragment;
import biz.belcorp.consultoras.feature.client.edit.ClientEditFragment;
import biz.belcorp.consultoras.feature.client.note.NoteFragment;
import biz.belcorp.consultoras.feature.client.order.ClientOrderWebFragment;
import biz.belcorp.consultoras.feature.client.order.history.ClientOrderHistoryWebFragment;
import biz.belcorp.consultoras.feature.client.registration.ClientRegistrationFragment;
import biz.belcorp.consultoras.feature.history.AddRecordatoryFragment;
import biz.belcorp.consultoras.feature.history.DebtPaymentDetailMovementFragment;
import biz.belcorp.consultoras.feature.history.DebtPaymentHistoryFragment;
import biz.belcorp.consultoras.feature.history.debt.DebtFragment;
import biz.belcorp.consultoras.feature.home.accountstate.AccountStateFragment;
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersFragment;
import biz.belcorp.consultoras.feature.home.addorders.client.ClientOrderFilterFragment;
import biz.belcorp.consultoras.feature.home.myorders.MyOrdersFragment;
import biz.belcorp.consultoras.feature.home.survey.SurveyBottomDialogFragment;
import biz.belcorp.consultoras.feature.payment.PaymentFragment;
import biz.belcorp.consultoras.feature.payment.SendPaymentFragment;
import biz.belcorp.consultoras.feature.search.list.SearchListFragment;
import biz.belcorp.consultoras.feature.search.results.SearchResultsFragment;
import biz.belcorp.consultoras.feature.search.single.SearchProductFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ClientModule.class})
public interface ClientComponent extends ActivityComponent {

    void inject(ClientRegistrationFragment clientRegistrationFragment);

    void inject(ClientCardFragment clientCardFragment);

    void inject(ClientEditFragment clientEditFragment);

    void inject(ClientOrderWebFragment clientOrderWebFragment);

    void inject(ClientOrderHistoryWebFragment clientOrderHistoryWebFragment);

    void inject(DebtPaymentHistoryFragment debtPaymentHistoryFragment);

    void inject(DebtPaymentDetailMovementFragment debtPaymentDetailMovementFragment);

    void inject(PaymentFragment paymentFragment);

    void inject(SendPaymentFragment sendPaymentFragment);

    void inject(NoteFragment noteFragment);

    void inject(AddRecordatoryFragment addRecordatoryFragment);

    void inject(DebtFragment noteFragment);

    void inject(AccountStateFragment accountStatusFragment);

    void inject(MyOrdersFragment myOrderFragment);

    void inject(AddOrdersFragment addOrdersFragment);

    void inject(SearchProductFragment searchProductFragment);

    void inject(SearchListFragment searchListFragment);

    void inject(SearchResultsFragment searchResultsFragment);

    void inject(ClientOrderFilterFragment clientOrderFilterFragment);

    void inject(biz.belcorp.consultoras.feature.history.debtxpedidov2.DebtXPedidoFragment debtXPedidoFragment);

    void inject(SurveyBottomDialogFragment fragment);
}
