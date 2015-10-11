package colector.co.com.collector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import colector.co.com.collector.R;
import colector.co.com.collector.model.Survey;
import colector.co.com.collector.settings.AppSettings;

/**
 * Created by dherrera on 11/10/15.
 */
public class SurveyAdapter extends ArrayAdapter<Survey> {

    private Context context;
    private List<Survey> items;
    private String idTab;

    public SurveyAdapter(Context context, ArrayList<Survey> items,String idTab) {
        super(context, R.layout.adapter_survey,items);
        this.context = context;
        this.items = items;
        this.idTab = idTab;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Survey item = (Survey) this.items.get(position);

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.adapter_survey, parent, false);

        TextView row_name = (TextView) row.findViewById(R.id.adapter_survey_title);
        row_name.setText(item.getForm_name());

        TextView row_description = (TextView) row.findViewById(R.id.adapter_survey_description);

        if(idTab.equals(AppSettings.TAB_ID_AVAILABLE_SURVEY)){
            row_description.setText(item.getForm_description());
        }else if(idTab.equals(AppSettings.TAB_ID_DONE_SURVEY)){
            row_description.setText(item.getSurveyDoneDescription());
        }

        return row;
    }


}
