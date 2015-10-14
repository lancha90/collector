package colector.co.com.collector;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import colector.co.com.collector.model.Question;
import colector.co.com.collector.model.Section;
import colector.co.com.collector.model.Survey;
import colector.co.com.collector.session.AppSession;

public class SurveyActivity extends AppCompatActivity {

    private LinearLayout container;
    private Survey survey = AppSession.getInstance().getCurrentSurvey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        container = (LinearLayout) findViewById(R.id.survey_contaniner);

        buildSurvey();
    }

    private void buildSurvey(){

        // Print survey name
        container.addView(buildTextView(survey.getForm_name()));

        for(Section section : survey.getSections()){
            container.addView(buildSeparator());
            buildSection(section);
        }

    }


    private void buildSection(Section section){

        LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.addView(buildTextView(section.getName()));
        setLayoutParams(linear);

        for(Question question : section.getInputs()){

            switch (question.getType()){
                // input text
                case 1:
                    linear.addView(buildTextView(question.getName()));
                    linear.addView(buildEditText());
                    break;
                // TODO implementar el multilinea
                // input text multiline
                case 2:

                    break;
                // date
                case 7:
                    final TextView tv = buildTextView(question.getName());
                    linear.addView(tv);
                    linear.addView(buildEditText(new View.OnClickListener() {
                        public void onClick(View v) {
                            DialogFragment newFragment = new TimePickerFragment((EditText) v);
                            newFragment.show(SurveyActivity.this.getFragmentManager(), "timePicker");
                        }
                    }));
                    break;

            }

        }

        container.addView(linear);

    }


    // ---------- CREATE COMPONENTS -----------

    /**
     * Create programatically a button
     * @param label
     * @return
     */
    private Button buildButton(String label,View.OnClickListener listener){
        Button toReturn = new Button(this);
        toReturn.setText(label);
        toReturn.setOnClickListener(listener);
        setLayoutParams(toReturn);
        return toReturn;
    }

    /**
     * Create programatically a input text
     * @return
     */
    private EditText buildEditText(){
        EditText toReturn = new EditText(this);
        setLayoutParams(toReturn);
        return toReturn;
    }

    private EditText buildEditText(View.OnClickListener listener){
        EditText toReturn = new EditText(this);
        toReturn.setOnClickListener(listener);
        toReturn.setFocusable(false);
        setLayoutParams(toReturn);
        return toReturn;
    }

    /**
     * Create programtically a textview
     * @param label
     * @return textview with text @param label
     */
    private TextView buildTextView(String label){
        TextView toReturn = new TextView(this);
        toReturn.setText(label);
        setLayoutParams(toReturn);
        return toReturn;
    }


    // --------- UTILITIES --------------

    private void setLayoutParams(View view) {

        LinearLayout.LayoutParams layoutWRAP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutMATCH = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        if (view instanceof TextView) {
            ((TextView) view).setLayoutParams(layoutWRAP);
        } else if (view instanceof LinearLayout) {
            ((LinearLayout) view).setLayoutParams(layoutMATCH);
        } else if (view instanceof EditText) {
            ((EditText) view).setLayoutParams(layoutMATCH);
        }
    }

    private View buildSeparator(){
        View toReturn = new View(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1);
        layoutParams.setMargins(10,5,10,5);
        toReturn.setLayoutParams(layoutParams);
        toReturn.setBackgroundColor(Color.rgb(51, 51, 51));
        return toReturn;
    }


    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private EditText toPrint;

        public  TimePickerFragment(EditText toPrint){
            super();
            this.toPrint = toPrint;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            toPrint.setText(day+"/"+month+"/"+year);
        }
    }
}
