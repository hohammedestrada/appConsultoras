package biz.belcorp.consultoras.common.view;

public interface RetryView {
    /**
     * Mostrar una vista de reintento en caso de un error al recuperar datos.
     */
    void showRetry();

    /**
     * Ocultar una vista de reintento mostrada si hubo un error al recuperar datos.
     */
    void hideRetry();
}
