package biz.belcorp.consultoras.common.network;

public class NetworkEvent {

    private Integer event;

    public NetworkEvent(Integer event) {
        this.event = event;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }
}
