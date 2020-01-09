package biz.belcorp.consultoras.feature.client.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.NoteUseCase;
import biz.belcorp.consultoras.domain.interactor.SurveyUseCase;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.ClienteRepository;
import biz.belcorp.consultoras.domain.repository.NoteRepository;
import biz.belcorp.consultoras.domain.repository.SessionRepository;
import biz.belcorp.consultoras.domain.repository.SurveyRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona los casos de usos del Home
 */
@Module
public class ClientModule {

    ClientModule() {
        // EMPTY
    }

    @Provides
    @PerActivity
    ClienteUseCase providesClientUseCase(ClienteRepository clienteRepository,
                                         AuthRepository authRepository,
                                         ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         SessionRepository sessionRepository) {
        return new ClienteUseCase(clienteRepository
            , threadExecutor, authRepository
            , postExecutionThread
            , sessionRepository);
    }

    @Provides
    @PerActivity
    NoteUseCase providesNoteUseCase(NoteRepository noteRepository,
                                      ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread) {
        return new NoteUseCase(noteRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SurveyUseCase providesSurveyUseCase(ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread,
                                        SurveyRepository surveyRepository) {
        return new SurveyUseCase(threadExecutor, postExecutionThread, surveyRepository);
    }
}
