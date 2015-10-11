package colector.co.com.collector.session;

import java.util.ArrayList;
import java.util.List;

import colector.co.com.collector.model.Survey;

/**
 * Created by dherrera on 11/10/15.
 */
public class AppSession {

    private static AppSession singletonObject;
    private String token;
    private List<Survey> surveyAvailable;
    private List<Survey> surveyDone;

    public AppSession() {
    }

    /**
     * Singleton instance
      * @return
     */
    public static synchronized AppSession getInstance() {
        if (singletonObject == null) {
            singletonObject = new AppSession();
        }
        return singletonObject;
    }

    public static synchronized void resetSingletonObject() {
        singletonObject = null;
    }


    public List<Survey> getSurveyDone() {
        if(surveyDone == null){
            surveyDone = new ArrayList<Survey>();
        }
        return surveyDone;
    }

    public void setSurveyDone(List<Survey> surveyDone) {
        this.surveyDone = surveyDone;
    }

    public List<Survey> getSurveyAvailable() {
        if(surveyAvailable == null){
            surveyAvailable = new ArrayList<Survey>();
        }
        return surveyAvailable;
    }

    public void setSurveyAvailable(List<Survey> surveyAvailable) {
        this.surveyAvailable = surveyAvailable;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


