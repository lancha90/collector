package colector.co.com.collector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dherrera on 11/10/15.
 */
public class ResponseComplex {

    private RecordId record_id;
    private List<ResponseItem> responses;



    public ResponseComplex(RecordId record_id, List<ResponseItem> responses) {
        this.record_id = record_id;
        this.responses = responses;
    }

    public ResponseComplex() {
        super();
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


}
