package colector.co.com.collector.model.response;

import java.util.ArrayList;
import java.util.List;

import colector.co.com.collector.model.Survey;

/**
 * Created by dherrera on 11/10/15.
 */
public class GetSurveysResponse {

    private List<Survey> response_data;
    private Long response_code;
    private String response_description;

    public GetSurveysResponse() {
        super();
    }

    public GetSurveysResponse(List<Survey> response_data, Long response_code, String response_description) {
        super();
        this.response_data = response_data;
        this.response_code = response_code;
        this.response_description = response_description;
    }

    public List<Survey> getResponseData() {
        return response_data;
    }

    public void setResponseData(List<Survey> response_data) {
        if(response_data==null){
            response_data=new ArrayList<Survey>();
        }
        this.response_data = response_data;
    }

    public Long getResponseCode() {
        return response_code;
    }

    public void setResponseCode(Long response_code) {
        this.response_code = response_code;
    }

    public String getResponseDescription() {
        return response_description;
    }

    public void setResponseDescription(String response_description) {
        this.response_description = response_description;
    }
}
