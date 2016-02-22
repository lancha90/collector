package colector.co.com.collector.utils;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dherrera on 1/02/16.
 */
public class OrdenVentaClass {
    private Long question;
    private TextView total;
    private Map<String,Double> subtotal;
    private Map<String,Integer> count;


    public OrdenVentaClass(TextView total,Long question) {
        this.total = total;
        this.question = question;
    }

    public TextView getTotal() {
        return total;
    }

    public void setTotal(TextView total) {
        this.total = total;
    }

    public Map<String, Double> getSubtotal() {
        if(subtotal == null){
            subtotal = new HashMap<String,Double>();
        }
        return subtotal;
    }

    public void setSubtotal(Map<String, Double> subtotal) {
        this.subtotal = subtotal;
    }

    public Map<String, Integer> getCount() {
        if(count == null){
            count = new HashMap<String,Integer>();
        }
        return count;
    }

    public void setCount(Map<String, Integer> count) {
        this.count = count;
    }

    public Long getQuestion() {
        return question;
    }

    public void setQuestion(Long question) {
        this.question = question;
    }

    public void updateTotal(){

        List<Double> toSUM = new ArrayList<>(subtotal.values());
        double toPrint = 0.0;

        for (Double tmp: toSUM){
            toPrint += tmp;
        }

        total.setText("Total: "+toPrint);
    }
}
