package biz.belcorp.consultoras.common.sync;

public class SyncEvent {

    private Boolean isSync;

    public SyncEvent (Boolean isSync) {
        this.isSync = isSync;
    }

    public Boolean isSync() {
        return isSync;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
    }
}
