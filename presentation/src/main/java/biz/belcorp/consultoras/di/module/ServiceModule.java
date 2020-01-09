package biz.belcorp.consultoras.di.module;

import android.app.Service;

import biz.belcorp.consultoras.di.PerService;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona objetos que vivirán durante el ciclo de vida de una actividad.
 */
@Module
public class ServiceModule {
    private final Service service;

    public ServiceModule(Service service) { this.service = service; }

    /**
     * Expone el servicio
     */
    @Provides
    @PerService
    Service provideService() {
        return this.service;
    }

}
