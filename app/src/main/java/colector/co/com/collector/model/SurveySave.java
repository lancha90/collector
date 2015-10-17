package colector.co.com.collector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dherrera on 17/10/15.
 */
public class SurveySave {

    private Long id;
    private String latitude;
    private String longitude;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
}
