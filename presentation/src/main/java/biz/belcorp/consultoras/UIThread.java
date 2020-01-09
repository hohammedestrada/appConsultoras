package biz.belcorp.consultoras;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.library.log.BelcorpLogger;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

@Singleton
public class UIThread implements PostExecutionThread {

    @Inject
    public UIThread() {
        // EMPTY
        Long tiempoInicio = System.currentTimeMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                    Long tiempoFin = System.currentTimeMillis();

                    Long milisegundos = tiempoInicio - tiempoFin;

                    BelcorpLogger.d(TimeUnit.MILLISECONDS.toSeconds(milisegundos));
                } catch (InterruptedException e) {
                    BelcorpLogger.e(e);
                }
            }
        }).start();

    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
