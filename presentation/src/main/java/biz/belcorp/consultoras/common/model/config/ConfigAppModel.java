package biz.belcorp.consultoras.common.model.config;

public class ConfigAppModel {

    private int connectivityType;
    private boolean notification;
    private boolean sonido;

    public int getConnectivityType() {
        return connectivityType;
    }

    public void setConnectivityType(int connectivityType) {
        this.connectivityType = connectivityType;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean isSonido() {
        return sonido;
    }

    public void setSonido(boolean sonido) {
        this.sonido = sonido;
    }
}
