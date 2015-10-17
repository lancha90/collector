package colector.co.com.collector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dherrera on 11/10/15.
 */
public class Question {


    private Long id;
    /**
     * TEXTO = '1' OK
     * PARRAFO = '2'
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

    private List<IdValue> responses;
    private List<ResponseComplex> filled_forms;
    private String name;
    private String description;

    public Question() {
        super();
    }

    public Question(Long id, int type, List<IdValue> responses, List<ResponseComplex> filled_forms, String name, String description) {
        this.id = id;
        this.type = type;
        this.responses = responses;
        this.filled_forms = filled_forms;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<IdValue> getResponses() {
        if(responses == null){
            responses = new ArrayList<IdValue>();
        }
        return responses;
    }

    public void setResponses(List<IdValue> responses) {
        this.responses = responses;
    }

    public List<ResponseComplex> getFilled_forms() {
        return filled_forms;
    }

    public void setFilled_forms(List<ResponseComplex> filled_forms) {
        this.filled_forms = filled_forms;
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
