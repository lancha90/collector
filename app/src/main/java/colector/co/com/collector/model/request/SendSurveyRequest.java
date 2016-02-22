package colector.co.com.collector.model.request;

import java.util.ArrayList;
import java.util.List;

import colector.co.com.collector.model.IdInputValue;
import colector.co.com.collector.model.IdValue;

/**
 * Created by dherrera on 11/10/15.
 */
public class SendSurveyRequest {

    private String colector_id;
    private String form_id;
    private List<IdInputValue> responses;
    private String latitud;
    private String longitud;
    private String horaini;
    private String horafin;

    public String getColector_id() {
        return colector_id;
    }

    public void setColector_id(String colector_id) {
        this.colector_id = colector_id;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public List<IdInputValue> getResponses() {
        return responses;
    }

    public void setResponses(List<IdInputValue> responses) {
        this.responses = responses;
    }

    public void setResponsesData(List<IdValue> responsesData) {
        responses = new ArrayList<IdInputValue>();
        for (IdValue item: responsesData) {
            responses.add(new IdInputValue(String.valueOf(item.getId()),item.getValue(),item.getOvValue() ));
        }
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

    public String getHoraini() {
        return horaini;
    }

    public void setHoraini(String horaini) {
        this.horaini = horaini;
    }

    public String getHorafin() {
        return horafin;
    }

    public void setHorafin(String horafin) {
        this.horafin = horafin;
    }
}

