package colector.co.com.collector.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import colector.co.com.collector.R;
import colector.co.com.collector.SurveyActivity;
import colector.co.com.collector.adapters.SurveyAdapter;
import colector.co.com.collector.model.Survey;
import colector.co.com.collector.session.AppSession;
import colector.co.com.collector.settings.AppSettings;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SurveyAvailable.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SurveyAvailable#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurveyAvailable extends Fragment {

    private String idTab;
    private ListView list;
    private List<Survey> toPrint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_survey_available, container, false);
        list = (ListView) v.findViewById(R.id.list_items);

        idTab = this.getTag();

        if(idTab.equals(AppSettings.TAB_ID_AVAILABLE_SURVEY)){
            toPrint = AppSession.getInstance().getSurveyAvailable();
        }else if(idTab.equals(AppSettings.TAB_ID_DONE_SURVEY)){
            toPrint = AppSession.getInstance().getSurveyDone();
        }

        fillList();

        return v;
    }


    private void fillList(){

        SurveyAdapter adapter = new SurveyAdapter(getActivity(),new ArrayList<>(toPrint),idTab);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AppSession.getInstance().setCurrentSurvey(toPrint.get(position));
                Intent intent = new Intent(getContext(), SurveyActivity.class);
                startActivity(intent);

            }
        });
    }


}
