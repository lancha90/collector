package colector.co.com.collector.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import colector.co.com.collector.R;
import colector.co.com.collector.SurveyActivity;
import colector.co.com.collector.http.AsyncResponse;
import colector.co.com.collector.http.BackgroundTask;
import colector.co.com.collector.http.ResourceNetwork;
import colector.co.com.collector.model.Survey;
import colector.co.com.collector.model.request.SendSurveyRequest;
import colector.co.com.collector.model.response.ErrorResponse;
import colector.co.com.collector.model.response.SendSurveyResponse;
import colector.co.com.collector.persistence.dao.SurveyDAO;
import colector.co.com.collector.session.AppSession;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Survey item = (Survey) this.items.get(position);

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.adapter_survey, parent, false);

        TextView row_name = (TextView) row.findViewById(R.id.adapter_survey_title);
        row_name.setText(item.getForm_name());

        TextView row_description = (TextView) row.findViewById(R.id.adapter_survey_description);

        if(idTab.equals(AppSettings.TAB_ID_AVAILABLE_SURVEY)){
            row_description.setText(item.getForm_description());
        }else if(idTab.equals(AppSettings.TAB_ID_DONE_SURVEY)){

            row_description.setText(item.getSurveyDoneDescription());

            ImageButton deleteButton = (ImageButton) row.findViewById(R.id.buttonDeleteSurvey);
            deleteButton.setTag(item.getInstanceId());
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), getContext().getString(R.string.survey_delete_ok, view.getTag().toString()), Toast.LENGTH_LONG).show();
                    new SurveyDAO(getContext()).deleteSurveyInstance(Long.parseLong(view.getTag().toString()));
                    items.remove(item);
                    SurveyAdapter.this.notifyDataSetChanged();
                }
            });
            ImageButton uploadUpload = (ImageButton) row.findViewById(R.id.buttonUploadSurvey);
            uploadUpload.setTag(item.getInstanceId());
            uploadUpload.setVisibility(View.VISIBLE);
            uploadUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    SendSurveyRequest toSend = new SendSurveyRequest();
                    toSend.setForm_id(String.valueOf(item.getForm_id()));
                    toSend.setColector_id(String.valueOf(AppSession.getInstance().getUser().getColector_id()));
                    toSend.setResponsesData(item.getInstanceAnswers());

                    AsyncResponse callback = new AsyncResponse() {
                        @Override
                        public void callback(Object output) {

                            if(output instanceof SendSurveyResponse){
                                SendSurveyResponse response = (SendSurveyResponse) output;

                                if(response.getResponseCode().equals(AppSettings.HTTP_OK)){
                                    new SurveyDAO(getContext()).deleteSurveyInstance(Long.parseLong(view.getTag().toString()));
                                    items.remove(item);
                                    SurveyAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(getContext(), getContext().getString(R.string.survey_save_send_ok), Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getContext(),response.getResponseDescription(),Toast.LENGTH_LONG).show();
                                }

                            }else if(output instanceof ErrorResponse){
                                Toast.makeText(getContext(), ((ErrorResponse) output).getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getContext(), getContext().getString(R.string.survey_save_send_error), Toast.LENGTH_LONG).show();
                            }

                        }
                    };

                    BackgroundTask bt = new BackgroundTask(getContext(), toSend, new SendSurveyResponse(), callback);
                    bt.execute(ResourceNetwork.URL_SEND_SURVEY);


                }
            });

            ImageButton editUpload = (ImageButton) row.findViewById(R.id.buttonEditSurvey);
            editUpload.setTag(item.getInstanceId());
            editUpload.setVisibility(View.VISIBLE);
            editUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppSession.getInstance().setCurrentSurvey(items.get(position));
                    Intent intent = new Intent(getContext(), SurveyActivity.class);
                    context.startActivity(intent);
                }
            });

        }

        return row;
    }




}
