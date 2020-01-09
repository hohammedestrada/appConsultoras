package biz.belcorp.consultoras.feature.home.clients;

public class ClientsListEvent {

    private final boolean refresh;

    public ClientsListEvent(boolean refresh) {
        this.refresh = refresh;
    }

}
