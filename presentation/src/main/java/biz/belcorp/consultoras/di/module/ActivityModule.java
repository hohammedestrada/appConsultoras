package biz.belcorp.consultoras.di.module;

import android.app.Activity;

import biz.belcorp.consultoras.di.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona objetos que vivirán durante el ciclo de vida de una actividad.
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expone la actividad
     */
    @Provides
    @PerActivity
    Activity provideActivity() {
        return this.activity;
    }
}
