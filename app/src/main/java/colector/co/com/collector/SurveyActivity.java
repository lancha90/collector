package colector.co.com.collector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import colector.co.com.collector.adapters.SurveyAdapterOptionalType;
import colector.co.com.collector.model.IdOptionValue;
import colector.co.com.collector.model.IdValue;
import colector.co.com.collector.model.Question;
import colector.co.com.collector.model.Section;
import colector.co.com.collector.model.Survey;
import colector.co.com.collector.model.SurveySave;
import colector.co.com.collector.persistence.dao.SurveyDAO;
import colector.co.com.collector.session.AppSession;

public class SurveyActivity extends AppCompatActivity {

    private LinearLayout container;
    private Survey survey = AppSession.getInstance().getCurrentSurvey();
    private boolean isModify = false;
    static final int REQUEST_TAKE_PHOTO = 1;
    File photoFile = null;
    String mCurrentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        container = (LinearLayout) findViewById(R.id.survey_contaniner);

        buildSurvey();

        container.addView(buildButton(getString(R.string.survey_save), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;
                SurveySave toInsert = new SurveySave();

                for (int i = 0; i < container.getChildCount(); i++) {

                    if (container.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout toFind = (LinearLayout) container.getChildAt(i);

                        for (int j = 0; j < toFind.getChildCount(); j++) {

                            if (toFind.getChildAt(j) instanceof EditText) {
                                EditText toProcess = (EditText) toFind.getChildAt(j);

                                if (toProcess != null && !toProcess.getText().toString().isEmpty()) {

                                    toInsert.getResponses().add(new IdValue((Long) toProcess.getTag(), toProcess.getText().toString()));

                                } else {
                                    isValid = false;
                                }

                            } else if (toFind.getChildAt(j) instanceof Spinner) {
                                Spinner toProcess = (Spinner) toFind.getChildAt(j);

                                if (toProcess != null) {
                                    toInsert.getResponses().add(new IdValue((Long) toProcess.getTag(), String.valueOf(toProcess.getSelectedView().getTag())));
                                } else {
                                    isValid = false;
                                }


                            } else if (toFind.getChildAt(j) instanceof ImageView) {
                                ImageView toProcess = (ImageView) toFind.getChildAt(j);

                                if (toProcess != null && toProcess.getDrawable() != null) {
                                    String base64 = getEncoded64ImageStringFromBitmap(((BitmapDrawable) toProcess.getDrawable()).getBitmap());
                                    toInsert.getResponses().add(new IdValue((Long) toProcess.getTag(), base64));
                                } else {
                                    isValid = false;
                                }
                            }
                        }
                    }
                }

                if (isValid) {
                    toInsert.setId(survey.getForm_id());
                    // TODO implemnetar el api geografica
                    toInsert.setLatitude("LATITUDE");
                    toInsert.setLongitude("LONGITUDE");

                    Long result;
                    if(isModify){
                        toInsert.setInstanceId(survey.getInstanceId());
                        result = new SurveyDAO(SurveyActivity.this).modifySurveyInstance(toInsert);
                    }else {
                        result = new SurveyDAO(SurveyActivity.this).saveSurveyInstance(toInsert);
                    }
                    // Validate result to print message
                    if (result != -1) {
                        if(!isModify) {
                            Toast.makeText(SurveyActivity.this, getString(R.string.survey_save_ok, String.valueOf(result)), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SurveyActivity.this, getString(R.string.survey_modify_ok, String.valueOf(result)), Toast.LENGTH_LONG).show();
                        }
                        finish();
                    } else {
                        Toast.makeText(SurveyActivity.this, getString(R.string.survey_save_error), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SurveyActivity.this, getString(R.string.survey_save_error_field), Toast.LENGTH_LONG).show();
                }
            }
        }));


    }

    private void buildSurvey(){

        // Print survey name
        setTitle(survey.getForm_name());

        for(Section section : survey.getSections()){
            buildSection(section);
        }

    }


    private void buildSection(Section section){

        LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.addView(buildTextView(section.getName()));
        linear.addView(buildSeparator());
        setLayoutParams(linear);

        for(Question question : section.getInputs()){

            switch (question.getType()){
                // input text
                case 1:
                    linear.addView(buildTextView(question.getName()));
                    linear.addView(buildEditText(question.getId()));
                    break;
                // input text multiline
                case 2:
                    linear.addView(buildTextView(question.getName()));
                    linear.addView(buildEditTextMultiline(question.getId()));
                    break;
                // opcion o combobox
                case 3:
                    linear.addView(buildTextView(question.getName()));
                    linear.addView(buildSpinner(question.getResponses(),question.getId()));
                    break;
                // picture
                case 6:
					linear.addView(buildImageView(question.getId()));
                    final Long id = question.getId();
                    linear.addView(buildButton(question.getName(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dispatchTakePictureIntent(id);
                        }
                    }));
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
                    },question.getId()));
                    break;
                // numeric input text
                case 8:
                    linear.addView(buildTextView(question.getName()));
                    linear.addView(buildEditTextNumeric(question.getId()));
                    break;
            }

        }

        container.addView(linear);

    }

    private void dispatchTakePictureIntent(Long id) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                Bundle extras = getIntent().putExtra("idImageView", id).getExtras();

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO,extras);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/collector";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            setPic();
        }
    }
    private void setPic() {
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ImageView imageView = null;
        Long idImageView = getIntent().getExtras().getLong("idImageView", -1);
        if (idImageView > -1){
            imageView = (ImageView) findViewById(idImageView.intValue());
            imageView.setImageBitmap(bitmap);
            imageView.getLayoutParams().height = 100;
            imageView.getLayoutParams().width = 100;
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    // ---------- CREATE COMPONENTS -----------

    /**
     * Create programatically a button
     * @param label
     * @return
     */
    private LinearLayout buildButton(String label,View.OnClickListener listener){

        LinearLayout toReturn = new LinearLayout(this);

        Button button = new Button(this);
        button.setText(label);
        button.setOnClickListener(listener);
        button.setBackgroundResource(R.drawable.rounded_shape);
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setPadding(15, 10, 15, 10);
        button.setMinWidth(300);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30,30,30,30);

        toReturn.setLayoutParams(layoutParams);
        toReturn.setGravity(Gravity.CENTER);
        toReturn.addView(button);

        return toReturn;
    }

    /**
     * Create programatically a input text
     * @return
     */
    private EditText buildEditText(Long id){
        EditText toReturn = new EditText(this);
        toReturn.setTag(id);
        // set value if is modified
        if(survey.getInstanceId() != null){
            isModify=true;
            toReturn.setText(survey.getAnswer(id));
        }

        return toReturn;
    }

    /**
     * Create programatically a input numeric
     * @return
     */
    private EditText buildEditTextNumeric(Long id){
        EditText toReturn = buildEditText(id);
        toReturn.setInputType(InputType.TYPE_CLASS_NUMBER);
        return toReturn;
    }

    /**
     * Create programatically a input multiline
     * @return
     */
    private EditText buildEditTextMultiline(Long id){
        EditText toReturn = buildEditText(id);
        toReturn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT ));
        toReturn.setGravity(Gravity.TOP);
        toReturn.setSingleLine(false);
        toReturn.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        toReturn.setMaxLines(5);
        toReturn.setLines(5);

        return toReturn;
    }

    private EditText buildEditText(View.OnClickListener listener,Long id){
        EditText toReturn = new EditText(this);
        toReturn.setOnClickListener(listener);
        toReturn.setFocusable(false);
        toReturn.setTag(id);
        // set value if is modified
        if(survey.getInstanceId() != null){
            isModify=true;
            toReturn.setText(survey.getAnswer(id));
        }
        return toReturn;
    }


    /**
     * Create programatically a spinner
     * @return
     */
    private Spinner buildSpinner(List<IdOptionValue> responses,Long id){
        Spinner toReturn = new Spinner(this);
        toReturn.setTag(id);
        setLayoutParams(toReturn);
        // TODO seleccionar la opción ingresada en la creación
        toReturn.setAdapter(new SurveyAdapterOptionalType(this, new ArrayList<IdOptionValue>(responses)));
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
        toReturn.setTextColor(ContextCompat.getColor(this, R.color.text_color));
        return toReturn;
    }

    /**
     * Create programtically a ImageView
     * @param id
     * @return ImageView with text @param id
     */
    private ImageView buildImageView(Long id) {
        ImageView toReturn = new ImageView(this);
        toReturn.setId(id.intValue());
        toReturn.setTag(id);
        setLayoutParams(toReturn);
        //TODO pintar la imagen que se puso durante la creación
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

        if (view instanceof TextView ) {
            layoutWRAP.setMargins(0,15,0,15);
            view.setLayoutParams(layoutWRAP);
        } else if (view instanceof LinearLayout || view instanceof EditText) {
            view.setLayoutParams(layoutMATCH);
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

    /**
     * Back button event
     */
    @Override
    public void onBackPressed() {
        exitFormAlert();
    }
    /**
     * Back button event
     */
    public void exitFormAlert(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.survey_back)
                .setPositiveButton(getString(R.string.survey_back_discard), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(getString(R.string.common_cancel), null)
                .show();
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
