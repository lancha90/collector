package colector.co.com.collector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dherrera on 11/10/15.
 */
public class Question {


    private Long input_id;
    /**
     * TEXTO = '1' OK
     * PARRAFO = '2' OK
     * OPCION = '3' OK
     * UNICA = '4' OK
     * MULTIPLE = '5'
     * FOTO = '6'
     * FECHA = '7' OK
     * NUMERO = '8' OK
     * SCAN = '9'
     * DINAMICA = '10'
     */
    private int type;

    private List<IdOptionValue> responses;
    private List<ResponseComplex> options;
    private String name;
    private String description;

    public Question() {
        super();
    }

    public Question(Long id, int type, List<IdOptionValue> responses, List<ResponseComplex> options, String name, String description) {
        this.input_id = id;
        this.type = type;
        this.responses = responses;
        this.options = options;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return input_id;
    }

    public void setId(Long id) {
        this.input_id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<IdOptionValue> getResponses() {
        if(responses == null){
            responses = new ArrayList<IdOptionValue>();
        }
        return responses;
    }

    public void setResponses(List<IdOptionValue> responses) {
        this.responses = responses;
    }

    public List<ResponseComplex> getOptions() {

        if( options == null){
            options = new ArrayList<ResponseComplex>();
        }
            return options;
    }

    public void setOptions(List<ResponseComplex> options) {
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
