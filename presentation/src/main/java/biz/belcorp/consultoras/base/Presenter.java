package biz.belcorp.consultoras.base;

import android.support.annotation.NonNull;

public interface Presenter<T extends View> {
    /**
     * Método que adjunta la vista en el presentador y interactua con la interfaz. Debe ser llamado en la vista
     */
    void attachView(@NonNull T view);

    /**
     * Método que controla el ciclo de vida de la vista. Debe ser llamado en la vista
     * (Activity o Fragment) Método onResume().
     */
    void resume();

    /**
     * Método que controla el ciclo de vida de la vista. Debe ser llamado en la vista
     * (Activity o Fragment) Método onPause().
     */
    void pause();

    /**
     * Método que controla el ciclo de vida de la vista. Debe ser llamado en la vista
     * (Activity o Fragment) Método onDestroy().
     */
    void destroy();
}
