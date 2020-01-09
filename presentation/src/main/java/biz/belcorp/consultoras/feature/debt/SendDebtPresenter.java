package biz.belcorp.consultoras.feature.debt;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ContactoModel;
import biz.belcorp.consultoras.common.model.debt.DebtResumeModel;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.model.user.UserModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.ShareDebt;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.DebtUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.library.annotation.DatetimeFormat;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.DateUtil;

/**
 *
 */
@PerActivity
class SendDebtPresenter implements Presenter<SendDebtView> {

    private SendDebtView sendDebtView;

    private final DebtUseCase debtUseCase;
    private final UserUseCase userUseCase;
    private final UserModelDataMapper userModelDataMapper;
    private final ClienteModelDataMapper clienteModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    private UserModel userModel;
    private ClienteModel clienteModel;
    private BigDecimal totalDebt = BigDecimal.ZERO;

    @Inject
    SendDebtPresenter(DebtUseCase debtUseCase, UserUseCase userUseCase, ClienteModelDataMapper clienteModelDataMapper
        , UserModelDataMapper userModelDataMapper, LoginModelDataMapper loginModelDataMapper) {
        this.debtUseCase = debtUseCase;
        this.userUseCase = userUseCase;
        this.clienteModelDataMapper = clienteModelDataMapper;
        this.userModelDataMapper = userModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull SendDebtView view) {
        this.sendDebtView = view;
    }

    @Override
    public void resume() {
        // EMPTY
    }

    @Override
    public void pause() {
        // EMPTY
    }

    @Override
    public void destroy() {
        debtUseCase.dispose();
        this.sendDebtView = null;
    }

    /**********************************************************/

    void getDebtData(int userID) {
        debtUseCase.getInfoForSharing(userID, new DebtDataObserver());
    }

    void validateShareType(int shareType) {

        if (shareType == 0 || null == sendDebtView) return;

        Integer contactType = shareType == 2 ? ContactType.EMAIL : ContactType.MOBILE;
        String phoneNumber = null;

        for (ContactoModel contactoModel : clienteModel.getContactoModels())
            if (contactoModel.getTipoContactoID().equals(contactType)) {
                phoneNumber = contactoModel.getValor();
                break;
            }

        if (TextUtils.isEmpty(phoneNumber))
            sendDebtView.onShareTypeClick(null);
    }

    void shareDebt(int shareType) {

        if (null == sendDebtView) return;

        Integer contactType = shareType == 2 ? ContactType.EMAIL : ContactType.MOBILE;
        String phoneNumber = null;

        for (ContactoModel contactoModel : clienteModel.getContactoModels())
            if (contactoModel.getTipoContactoID().equals(contactType)) {
                phoneNumber = contactoModel.getValor();
                break;
            }

        if (!TextUtils.isEmpty(phoneNumber))
            sendDebtView.shareDebt(phoneNumber);
        else
            sendDebtView.onError(shareType);
    }

    void setTotalDebt(BigDecimal totalDebt) {
        this.totalDebt = totalDebt;
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void initScreenTrackEnviarMensajeDeuda(int shareType) {
        userUseCase.get(new UserPropertyEnviarMensajeDeudaObserver(shareType));
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private class DebtDataObserver extends BaseObserver<ShareDebt> {

        @Override
        public void onNext(ShareDebt shareDebt) {

            if( null != sendDebtView) {

                userUseCase.updateScheduler(new UpdateObserver());

                clienteModel = clienteModelDataMapper.transform(shareDebt.getCliente());
                userModel = userModelDataMapper.transform(shareDebt.getUser());

                DebtResumeModel debtResumeModel = new DebtResumeModel();
                debtResumeModel.setClientName(clienteModel.getNombres());
                debtResumeModel.setConsultantName(shareDebt.getUser().getConsultantName());

            DecimalFormat decimalFormat = CountryUtil.getDecimalFormatByISO(userModel.getCountryISO(), true);

                debtResumeModel.setTotalDebt(String.format(Locale.getDefault(), "%1$s %2$s"
                    , shareDebt.getUser().getCountryMoneySymbol(), decimalFormat.format(totalDebt)));

                if (!clienteModel.getRecordatorioModels().isEmpty()) {
                    RecordatorioModel recordatorioModel = clienteModel.getRecordatorioModels().get(0);

                    String deadLineFormatted = null;

                    try {
                        Date recordatoryDate = DateUtil.convertFechaToDate(recordatorioModel.getFecha(), DatetimeFormat.ISO_8601);

                        deadLineFormatted = DateUtil.convertFechaToString(recordatoryDate, DatetimeFormat.LOCAL_DATE_SIMPLE_LONG);
                    } catch (ParseException e) {
                        BelcorpLogger.w("ParseException", e);
                    }

                    debtResumeModel.setDebtDescription(null);
                    debtResumeModel.setDeadLine(deadLineFormatted);
                }

                boolean mobile = clienteModel.getContactoModelMap().get(ContactType.MOBILE) != null &&
                    !TextUtils.isEmpty(clienteModel.getContactoModelMap().get(ContactType.MOBILE).getValor());

                boolean email = clienteModel.getContactoModelMap().get(ContactType.EMAIL) != null &&
                    !TextUtils.isEmpty(clienteModel.getContactoModelMap().get(ContactType.EMAIL).getValor());

                sendDebtView.setUpShareType(mobile, mobile, email);
                sendDebtView.showMessage(debtResumeModel);
                sendDebtView.hideLoading();
            }

        }

        @Override
        public void onError(Throwable exception) {
            if( null != sendDebtView) {
                sendDebtView.hideLoading();
                sendDebtView.onError(exception);
            }
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean b) {
            super.onNext(b);
            BelcorpLogger.d("PaymentOnlinePresenter", "TarjetaPago registrado: " + b);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (null == sendDebtView) return;
            sendDebtView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyEnviarMensajeDeudaObserver extends BaseObserver<User> {

        private int shareType;

        public UserPropertyEnviarMensajeDeudaObserver(int shareType) {
            this.shareType = shareType;
        }

        @Override
        public void onNext(User user) {
            if (null == sendDebtView) return;
            sendDebtView.initScreenTrackEnviarMensajeDeuda(loginModelDataMapper.transform(user));
            shareDebt(shareType);
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == sendDebtView) return;
            sendDebtView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}
