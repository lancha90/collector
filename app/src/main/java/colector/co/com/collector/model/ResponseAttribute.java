package colector.co.com.collector.model;

/**
 * Attributes into the dynamic form
 * Created by dherrera on 13/11/15.
 */
public class ResponseAttribute {

    private Long input_id;
    private Long type;
    private String label;

    public ResponseAttribute(Long input_id, Long type, String label) {
        super();
        this.input_id = input_id;
        this.type = type;
        this.label = label;
    }

    public Long getInput_id() {
        return input_id;
    }

    public void setInput_id(Long input_id) {
        this.input_id = input_id;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}