package colector.co.com.collector.model;

/**
 * Created by dherrera on 13/11/15.
 */
public class RecordId {

    private String uuid;

    public RecordId(String uuid) {
        super();
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
