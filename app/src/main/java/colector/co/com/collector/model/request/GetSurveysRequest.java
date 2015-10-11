package colector.co.com.collector.model.request;

/**
 * Created by dherrera on 11/10/15.
 */
public class GetSurveysRequest {

    private String token;

    public GetSurveysRequest() {
        super();
    }
    public GetSurveysRequest(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
