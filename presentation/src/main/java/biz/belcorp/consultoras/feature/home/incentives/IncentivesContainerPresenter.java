package biz.belcorp.consultoras.feature.home.incentives;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModelDataMapper;
import biz.belcorp.consultoras.common.model.incentivos.IncentivesRequestModel;
import biz.belcorp.consultoras.common.model.incentivos.IncentivesRequestModelDataMapper;
import biz.belcorp.consultoras.common.model.incentivos.NivelModel;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.BasicDto;
import biz.belcorp.consultoras.domain.entity.Concurso;
import biz.belcorp.consultoras.domain.entity.ContenidoResumen;
import biz.belcorp.consultoras.domain.entity.ResumenRequest;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.IncentivesUseCase;
import biz.belcorp.consultoras.domain.interactor.SessionUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.annotation.Country;

@PerActivity
class IncentivesContainerPresenter implements Presenter<IncentivesContainerView> {

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    private final IncentivesUseCase incentivesUseCase;
    private final SessionUseCase sessionUseCase;
    private final ConcursoModelDataMapper concursoModelDataMapper;
    private final IncentivesRequestModelDataMapper incentivesRequestModelDataMapper;
    private final AccountUseCase accountUseCase;

    private IncentivesContainerView incentivesView;

    @Inject
    IncentivesContainerPresenter(UserUseCase userUseCase,
                                 IncentivesUseCase incentivesUseCase,
                                 SessionUseCase sessionUseCase,
                                 LoginModelDataMapper loginModelDataMapper,
                                 ConcursoModelDataMapper concursoModelDataMapper,
                                 IncentivesRequestModelDataMapper incentivesRequestModelDataMapper,
                                 AccountUseCase accountUseCase) {
        this.userUseCase = userUseCase;
        this.incentivesUseCase = incentivesUseCase;
        this.sessionUseCase = sessionUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.concursoModelDataMapper = concursoModelDataMapper;
        this.incentivesRequestModelDataMapper = incentivesRequestModelDataMapper;
        this.accountUseCase = accountUseCase;
    }

    @Override
    public void attachView(@NonNull IncentivesContainerView view) {
        incentivesView = view;
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
        this.userUseCase.dispose();
        this.incentivesUseCase.dispose();
        this.sessionUseCase.dispose();
        this.incentivesView = null;
    }

    /**********************************************************/

    public void get() {
        incentivesView.showLoading();
        userUseCase.get(new GetUser(false));
    }

    public void getOffline() {
        userUseCase.get(new GetUser(true));
    }

    public void initScreenTrack(int type) {
        userUseCase.get(new UserPropertyObserver(type));
    }

    public void trackEvent(String screenName,
                           String eventCat,
                           String eventAction,
                           String eventLabel,
                           String eventName) {
        userUseCase.get(new EventPropertyObserver(screenName, eventCat, eventAction, eventLabel, eventName));
    }

    public void getContenidoUserDigital() {
        incentivesView.showLoading();
        userUseCase.get(new GetUserDigital());
    }

    private void getIncentives(User user) {

        IncentivesRequestModel requestModel = new IncentivesRequestModel();
        requestModel.setConsultantCode(user.getConsultantCode());
        requestModel.setCountryISO(user.getCountryISO());
        requestModel.setCampaingCode(user.getCampaing());
        requestModel.setRegionCode(user.getRegionCode());
        requestModel.setZoneCode(user.getZoneCode());

        incentivesUseCase.getAndSave(new GetIncentivesObserver(user),
            incentivesRequestModelDataMapper.transform(requestModel));
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {

        private Boolean offline;

        GetUser(Boolean offline) {
            this.offline = offline;
        }

        @Override
        public void onNext(User user) {

            if (!offline) {
                incentivesView.showLoading();
                getIncentives(user);
            } else sessionUseCase.checkSchedule(new ScheduleObserver(user));

        }
    }

    private final class GetUserDigital extends BaseObserver<User> {

        GetUserDigital() {
        }

        @Override
        public void onNext(User user) {

            if (user.getIndicadorConsultoraDigital() == 0 || !user.getCountryISO().equals(Country.PE)) {
                incentivesView.hideLoading();
                incentivesView.showDefaultContainer();
                return;
            }

            String documento = (user.getNumeroDocumento() == null || user.getNumeroDocumento().isEmpty())? "0":user.getNumeroDocumento();

            ResumenRequest request = new ResumenRequest();
            request.setCampaing(Integer.parseInt(user.getCampaing()));
            request.setCodeRegion(user.getRegionCode());
            request.setCodeSection(user.getCodigoSeccion());
            request.setCodeZone(user.getZoneCode());
            request.setIdContenidoDetalle(0);
            request.setIndConsulDig(String.valueOf(user.getIndicadorConsultoraDigital()));
            request.setNumeroDocumento(documento);
            request.setPrimerNombre(user.getPrimerNombre());
            request.setPrimerApellido(user.getPrimerApellido());
            request.setFechaNacimiento(user.getFechaNacimiento());
            request.setCorreo(user.getEmail());
            request.setEsLider(user.getEsLider());

            incentivesView.showLoading();
            accountUseCase.getResumenConfig(request,
                new GetResumen());
        }

        @Override
        public void onError(@NotNull Throwable exception) {
            if (null == incentivesView) return;
            incentivesView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                incentivesView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                incentivesView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class GetResumen extends BaseObserver<BasicDto<Collection<ContenidoResumen>>> {
        @Override
        public void onNext(BasicDto<Collection<ContenidoResumen>> collectionBasicDto) {

            if (collectionBasicDto == null) {
                incentivesView.showDefaultContainer();
                incentivesView.hideLoading();
                return;
            }

            if (collectionBasicDto.getCode() == null) {
                incentivesView.showDefaultContainer();
                incentivesView.hideLoading();
                return;
            }

            if (!collectionBasicDto.getCode().equalsIgnoreCase(GlobalConstant.CODE_OK)) {
                incentivesView.showDefaultContainer();
                incentivesView.hideLoading();
                return;
            }

            if (collectionBasicDto.getData() == null) {
                incentivesView.showDefaultContainer();
                incentivesView.hideLoading();
                return;
            }

            for (ContenidoResumen item : collectionBasicDto.getData()) {

                if (!item.getCodigoResumen().equalsIgnoreCase(GlobalConstant.CODE_BONIF_RESUMEN))
                    continue;

                if (item.getContenidoDetalle() == null) continue;

                if (item.getContenidoDetalle().size() == 0) continue;

                if (item.getContenidoDetalle().get(0).getUrlDetalleResumen() == null){
                    incentivesView.showDefaultContainer();
                    incentivesView.hideLoading();
                    return;
                }

                if (item.getContenidoDetalle().get(0).getUrlDetalleResumen().trim().length() == 0){
                    incentivesView.showDefaultContainer();
                    incentivesView.hideLoading();
                    return;
                }

                incentivesView.loadViewBonifDigital(item.getContenidoDetalle().get(0).getUrlDetalleResumen());
                incentivesView.hideLoading();
                return;
            }

            incentivesView.showDefaultContainer();
            incentivesView.hideLoading();
        }

        @Override
        public void onError(@NotNull Throwable exception) {
            if (null == incentivesView) return;
            incentivesView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                incentivesView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                incentivesView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class GetIncentivesObserver extends BaseObserver<Collection<Concurso>> {

        private String campaingCurrent;
        private String countryMoneySymbol;
        private String countryISO;

        private GetIncentivesObserver(User user) {
            this.campaingCurrent = user.getCampaing();
            this.countryMoneySymbol = user.getCountryMoneySymbol();
            this.countryISO = user.getCountryISO();
        }

        @Override
        public void onNext(Collection<Concurso> collection) {

            if (null == incentivesView) return;

            int currentCampaign = Integer.parseInt(campaingCurrent);

            List<ConcursoModel> historyContest = new ArrayList<>();
            List<ConcursoModel> constanciaCurrentContest = new ArrayList<>();
            List<ConcursoModel> listaPedidoCurrentContest = new ArrayList<>();
            List<ConcursoModel> listaPedidoPreviousContest = new ArrayList<>();
            List<ConcursoModel> listaNewProgramContest = new ArrayList<>();
            ConcursoModel brightPathContest = null;

            List<ConcursoModel> list = new ArrayList<>(concursoModelDataMapper.transform(collection));

            for (ConcursoModel concursoModel : list) {
                //Programa Brillantes
                if(concursoModel.getTipoConcurso() != null && concursoModel.getTipoConcurso().equals("T"))//Programa brillantes
                    brightPathContest = concursoModel;

                //Históricos

                if (concursoModel.getCampaniaId() != null && concursoModel.getCampaniaId().equals("0")) {
                    historyContest.add(concursoModel);
                } else {
                    //Pedidos y Constancia

                    if (concursoModel.getTipoConcurso() != null &&
                        !concursoModel.getTipoConcurso().equals("X") &&
                        !concursoModel.getTipoConcurso().equals("K") &&
                        !concursoModel.getTipoConcurso().equals("P")) continue;

                    if ((concursoModel.getCampaniaIDInicio() != null && concursoModel.getCampaniaIDFin() != null) &&
                        (currentCampaign >= concursoModel.getCampaniaIDInicio() && currentCampaign <= concursoModel.getCampaniaIDFin())) {

                        if (concursoModel.getTipoConcurso().equals("X"))
                            listaPedidoCurrentContest.add(concursoModel);
                        else if (concursoModel.getTipoConcurso().equals("K"))
                            constanciaCurrentContest.add(concursoModel);
                    } else {
                        if (concursoModel.getTipoConcurso() != null && concursoModel.getTipoConcurso().equals("P")) {
                            listaNewProgramContest.add(concursoModel);
                        } else if (concursoModel.getNiveles() != null && !concursoModel.getNiveles().isEmpty()) {
                            // Validaciones BelCenter
                            List<NivelModel> listaPremio = new ArrayList<>();

                            for (NivelModel model : concursoModel.getNiveles()) {
                                if ((model.getIndicadorBelCenter()
                                    && !TextUtils.isEmpty(model.getFechaVentaRetail()))
                                    || concursoModel.getNivelAlcanzado() >= model.getCodigoNivel()) {
                                    listaPremio.add(model);
                                }
                            }

                            if (!listaPremio.isEmpty()) {
                                listaPedidoPreviousContest.add(concursoModel);
                            }
                        }

                    }
                }
            }

            incentivesView.showByHistory(historyContest, countryISO);
            incentivesView.showByActive(listaNewProgramContest, constanciaCurrentContest, listaPedidoCurrentContest,
                listaPedidoPreviousContest, brightPathContest, campaingCurrent, countryMoneySymbol, countryISO);
            incentivesView.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == incentivesView) return;
            incentivesView.initializeAdapter(countryISO);
            incentivesView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                incentivesView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                incentivesView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        private final int type;

        public UserPropertyObserver(int type) {
            this.type = type;
        }

        @Override
        public void onNext(User user) {
            if (null == incentivesView) return;
            incentivesView.initScreenTrack(loginModelDataMapper.transform(user), type);
        }
    }

    private final class EventPropertyObserver extends BaseObserver<User> {

        final String screenHome;
        final String eventCat;
        final String eventAction;
        final String eventLabel;
        final String eventName;

        private EventPropertyObserver(String screenHome,
                                      String eventCat,
                                      String eventAction,
                                      String eventLabel,
                                      String eventName) {
            this.screenHome = screenHome;
            this.eventCat = eventCat;
            this.eventAction = eventAction;
            this.eventLabel = eventLabel;
            this.eventName = eventName;
        }

        @Override
        public void onNext(User user) {
            incentivesView.trackEvent(screenHome,
                eventCat,
                eventAction,
                eventLabel,
                eventName,
                loginModelDataMapper.transform(user));
        }
    }

    private final class ScheduleObserver extends BaseObserver<Boolean> {

        private User user;

        ScheduleObserver(User user) {
            this.user = user;
        }

        @Override
        public void onNext(Boolean state) {
            if (state) {
                incentivesView.showLoading();
                getIncentives(user);
            }
        }
    }

}
