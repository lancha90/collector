package colector.co.com.collector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dherrera on 11/10/15.
 */
public class ResponseComplex {

    private Long id;
    private String value;
    private List<IdValue> data;

    public ResponseComplex(){
        super();
    }

    public ResponseComplex(Long id, String value, List<IdValue> data) {
        this.id = id;
        this.value = value;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<IdValue> getData() {
        if(data == null){
            data = new ArrayList<IdValue>();
        }
        return data;
    }

    public void setData(List<IdValue> data) {
        this.data = data;
    }
}
