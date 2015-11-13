package colector.co.com.collector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dherrera on 11/10/15.
 */
public class ResponseComplex {

    private RecordId record_id;
    private List<ResponseItem> responses;
    private List<ResponseAttribute> atributos;

    public ResponseComplex(RecordId record_id, List<ResponseItem> responses, List<ResponseAttribute> atributos) {
        this.record_id = record_id;
        this.responses = responses;
        this.atributos = atributos;
    }

    public RecordId getRecord_id() {
        return record_id;
    }

    public void setRecord_id(RecordId record_id) {
        this.record_id = record_id;
    }

    public List<ResponseItem> getResponses() {
        if(responses == null){
            responses = new ArrayList<ResponseItem>();
        }
        return responses;
    }

    public void setResponses(List<ResponseItem> responses) {
        this.responses = responses;
    }

    public List<ResponseAttribute> getAtributos() {
        if(atributos == null){
            atributos = new ArrayList<ResponseAttribute>();
        }
        return atributos;
    }

    public void setAtributos(List<ResponseAttribute> atributos) {
        this.atributos = atributos;
    }
}
