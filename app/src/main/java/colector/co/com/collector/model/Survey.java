package colector.co.com.collector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dherrera on 11/10/15.
 */
public class Survey {
    private Long form_id;
    private String form_name;
    private String form_description;
    private List<Section> sections;
    private Long instanceId;
    private String instanceDate;
    private List<IdValue> instanceAnswers;

    public Survey() {
        super();
    }

    public Survey(Long form_id, String form_name, String form_description, List<Section> sections) {
        this.form_id = form_id;
        this.form_name = form_name;
        this.form_description = form_description;
        this.sections = sections;
    }


    public String getSurveyDoneDescription(){
        return instanceDate;
    }

    public Long getForm_id() {
        return form_id;
    }

    public void setForm_id(Long form_id) {
        this.form_id = form_id;
    }

    public String getForm_name() {
        return form_name;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }

    public String getForm_description() {
        return form_description;
    }

    public void setForm_description(String form_description) {
        this.form_description = form_description;
    }

    public List<Section> getSections() {
        if(sections==null){
            sections=new ArrayList<Section>();
        }
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceDate() {
        return instanceDate;
    }

    public void setInstanceDate(String instanceDate) {
        this.instanceDate = instanceDate;
    }

    public List<IdValue> getInstanceAnswers() {
        if(instanceAnswers==null){
            instanceAnswers=new ArrayList<IdValue>();
        }
        return instanceAnswers;
    }

    public void setInstanceAnswers(List<IdValue> instanceAnswers) {
        this.instanceAnswers = instanceAnswers;
    }

    public String getAnswer(Long id){
        for(IdValue item : instanceAnswers){
            if(item.getId().equals(id)){
                return item.getValue();
            }
        }
        return "";
    }
}
