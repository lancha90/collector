package colector.co.com.collector.model;

/**
 * Created by dherrera on 11/10/15.
 */
public class IdValue {
    private Long id;
    private String value;

    public IdValue(){
        super();
    }

    public IdValue(Long id, String value) {
        super();
        this.id = id;
        this.value = value;
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
}
