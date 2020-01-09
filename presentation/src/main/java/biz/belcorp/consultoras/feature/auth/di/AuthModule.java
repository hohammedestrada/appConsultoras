package biz.belcorp.consultoras.feature.auth.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.AuthUseCase;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.FacebookUseCase;
import biz.belcorp.consultoras.domain.interactor.FestivalUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.domain.repository.AccountRepository;
import biz.belcorp.consultoras.domain.repository.ApiRepository;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.ClienteRepository;
import biz.belcorp.consultoras.domain.repository.CountryRepository;
import biz.belcorp.consultoras.domain.repository.FacebookRepository;
import biz.belcorp.consultoras.domain.repository.FestivalRepository;
import biz.belcorp.consultoras.domain.repository.MenuRepository;
import biz.belcorp.consultoras.domain.repository.SessionRepository;
import biz.belcorp.consultoras.domain.repository.UserRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona los casos de usos del Splash
 */
@Module
public class AuthModule {

    @Provides
    @PerActivity
    CountryUseCase providesCountryUseCase(SessionRepository sessionRepository,
                                          CountryRepository countryRepository,
                                          ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread) {
        return new CountryUseCase(sessionRepository, countryRepository, threadExecutor,
            postExecutionThread);
    }

    @Provides
    @PerActivity
    AuthUseCase providesAuthUseCase(AuthRepository authRepository,
                                    SessionRepository sessionRepository,
                                    FacebookRepository facebookRepository,
                                    UserRepository userRepository,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        return new AuthUseCase(authRepository, sessionRepository, facebookRepository,
            userRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    FacebookUseCase providesFacebookUseCase(FacebookRepository repository,
                                            ThreadExecutor threadExecutor,
                                            PostExecutionThread postExecutionThread) {
        return new FacebookUseCase(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    AccountUseCase providesAccountUseCase(AccountRepository repository,
                                          AuthRepository authRepository,
                                          ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          UserRepository userRepository,
                                          CountryRepository countryRepository) {
        return new AccountUseCase(repository, authRepository, threadExecutor, userRepository,
            countryRepository, postExecutionThread);
    }

    @Provides
    @PerActivity
    UserUseCase providesUserUseCase(UserRepository userRepository,
                                    AuthRepository authRepository,
                                    MenuRepository menuRepository,
                                    ClienteRepository clienteRepository,
                                    SessionRepository sessionRepository,
                                    ApiRepository apiRepository,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        return new UserUseCase(userRepository, authRepository, menuRepository, clienteRepository,
            sessionRepository, apiRepository, threadExecutor, postExecutionThread);
    }


    @Provides
    @PerActivity
    FestivalUseCase providesFestivalUseCase(FestivalRepository repository,
                                            ThreadExecutor threadExecutor,
                                            PostExecutionThread postExecutionThread) {
        return new FestivalUseCase(repository, threadExecutor, postExecutionThread);
    }

}
