package colector.co.com.collector.model;

/**
 * Created by dherrera on 11/10/15.
 */
public class IdValue {
    private Long id;
    private String value;
    private String ovValue;

    public IdValue(){
        super();
    }

    public IdValue(Long id, String value) {
        super();
        this.id = id;
        this.value = value;
    }

    public IdValue(Long id, String value, String ovValue) {
        this.id = id;
        this.value = value;
        this.ovValue = ovValue;
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

    public String getOvValue() {
        return ovValue;
    }

    public void setOvValue(String ovValue) {
        this.ovValue = ovValue;
    }
}
