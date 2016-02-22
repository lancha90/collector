package colector.co.com.collector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dherrera on 17/10/15.
 */
public class SurveySave {

    private Long instanceId;
    private Long id;
    private String latitud;
    private String longitud;
    private List<IdValue> responses;

    public SurveySave(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<IdValue> getResponses() {
        if(responses == null){
            responses = new ArrayList<IdValue>();
        }
        return responses;
    }

    public void setResponses(List<IdValue> responses) {
        this.responses = responses;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

}
