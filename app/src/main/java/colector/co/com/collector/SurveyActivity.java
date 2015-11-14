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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
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

import colector.co.com.collector.adapters.OptionAdapter;
import colector.co.com.collector.adapters.SurveyAdapterMultipleType;
import colector.co.com.collector.adapters.SurveyAdapterOptionalType;
import colector.co.com.collector.model.IdOptionValue;
import colector.co.com.collector.model.IdValue;
import colector.co.com.collector.model.Question;
import colector.co.com.collector.model.ResponseAttribute;
import colector.co.com.collector.model.ResponseComplex;
import colector.co.com.collector.model.ResponseItem;
import colector.co.com.collector.model.Section;
import colector.co.com.collector.model.Survey;
import colector.co.com.collector.model.SurveySave;
import colector.co.com.collector.persistence.dao.SurveyDAO;
import colector.co.com.collector.session.AppSession;

public class SurveyActivity extends AppCompatActivity {

    private ArrayList<LinearLayout> pictureLayouts;
    private LinearLayout container;
    private Survey survey = AppSession.getInstance().getCurrentSurvey();
    private boolean isModify = false;
    static final int REQUEST_TAKE_PHOTO = 1;
    public PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        container = (LinearLayout) findViewById(R.id.survey_contaniner);
        pictureLayouts = new ArrayList<LinearLayout>();

        buildSurvey();

        container.addView(buildButton(getString(R.string.survey_save), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;
                SurveySave toInsert = new SurveySave();

                for (int i = 0; i < container.getChildCount(); i++) {

                    if (container.getChildAt(i) instanceof LinearLayout && container.getChildAt(i).getTag() == null) {
                        LinearLayout toFind = (LinearLayout) container.getChildAt(i);

                        for (int j = 0; j < toFind.getChildCount(); j++) {

                            if(toFind.getChildAt(j) instanceof LinearLayout && toFind.getChildAt(j).getTag() != null && toFind.getChildAt(j).getTag() instanceof IdValue) {

                                IdValue toInsertValue = (IdValue) toFind.getChildAt(j).getTag();
                                toInsert.getResponses().add(toInsertValue);

                            }else if(toFind.getChildAt(j) instanceof LinearLayout && toFind.getChildAt(j).getTag() != null && toFind.getChildAt(j).getTag() instanceof ResponseComplex) {

                                LinearLayout toFindDynamic = (LinearLayout) toFind.getChildAt(j);
                                for(int k=0;k<toFindDynamic.getChildCount();k++ ){

                                    boolean isResponseOK = buildObjectToSave(toFindDynamic.getChildAt(k),toInsert.getResponses());
                                    if (!isResponseOK) {
                                        isValid = false;
                                    }
                                }

                            }else {

                                boolean isResponseOK = buildObjectToSave(toFind.getChildAt(j),toInsert.getResponses());
                                if (!isResponseOK) {
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
                    if (isModify) {
                        toInsert.setInstanceId(survey.getInstanceId());
                        result = new SurveyDAO(SurveyActivity.this).modifySurveyInstance(toInsert);
                    } else {
                        result = new SurveyDAO(SurveyActivity.this).saveSurveyInstance(toInsert);
                    }
                    // Validate result to print message
                    if (result != -1) {
                        if (!isModify) {
                            Toast.makeText(SurveyActivity.this, getString(R.string.survey_save_ok, String.valueOf(result)), Toast.LENGTH_LONG).show();
                        } else {
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

    /**
     * Build an object to save survey
     */
    private boolean buildObjectToSave(View view, List<IdValue> arrayResponse){

        if (view instanceof EditText) {
            EditText toProcess = (EditText) view;

            if (toProcess != null && !toProcess.getText().toString().isEmpty()) {

                arrayResponse.add(new IdValue((Long) toProcess.getTag(), toProcess.getText().toString()));

            } else {
                return false;
            }

        }else if (view instanceof ListView) {
            ListView toProcess = (ListView) view;

            if (toProcess != null) {

                SurveyAdapterMultipleType keyValues = (SurveyAdapterMultipleType) toProcess.getAdapter();
                List<IdOptionValue> lstSelectedValues = keyValues.getTrueStatusItems();

                if (lstSelectedValues.size() > 0) {

                    for (IdOptionValue item : lstSelectedValues) {
                        arrayResponse.add(new IdValue((Long) toProcess.getTag(), String.valueOf(item.getId())));
                    }
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }else if (view instanceof Spinner) {
            Spinner toProcess = (Spinner) view;

            if (toProcess != null) {
                arrayResponse.add(new IdValue((Long) toProcess.getTag(), String.valueOf(((IdOptionValue) toProcess.getSelectedItem()).getId())));
            } else {
                return false;
            }

        } else if(view instanceof LinearLayout && view.getTag() != null) {

            LinearLayout toProcessLinear = (LinearLayout) view;
            Long idQuestion = (Long) toProcessLinear.getTag();

            // Se recorren todos los elementos de linear layout buscando los imageview de las imagenes
            for (int k = 0; k < toProcessLinear.getChildCount(); k++) {

                if (toProcessLinear.getChildAt(k) instanceof ImageView) {
                    ImageView toProcess = (ImageView) toProcessLinear.getChildAt(k);
                    if (toProcess != null && toProcess.getDrawable() != null) {
                        String base64 = getEncoded64ImageStringFromBitmap(((BitmapDrawable) toProcess.getDrawable()).getBitmap());
                        arrayResponse.add(new IdValue(idQuestion, base64));
                    } else {
                        return false;
                    }
                }

            }

        }

        return true;
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

           buildQuestion(question.getName(), question.getId(), question.getType(), question.getResponses(), question.getOptions(), question.getAtributos(), linear);
        }

        container.addView(linear);

    }

    private void buildQuestion(String label,Long id,int type,List<IdOptionValue> response, List<ResponseComplex> options, List<ResponseAttribute> atributos, LinearLayout linear){
        switch (type){
            // input text
            case 1:
                linear.addView(buildTextView(label));
                linear.addView(buildEditText(id));
                break;
            // input text multiline
            case 2:
                linear.addView(buildTextView(label));
                linear.addView(buildEditTextMultiline(id));
                break;
            // opcion o combobox
            case 3:
                linear.addView(buildTextView(label));
                linear.addView(buildSpinner(response, id));
                break;
            //Multiple opcion
            case 5:
                linear.addView(buildTextView(label));
                linear.addView(buildMultiple(response, id));
                break;
            // picture
            case 6:
                linear.addView(buildImageLinear(id));
                final Long _id = id;
                linear.addView(buildButton(label, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppSession.getInstance().setCurrentPhotoID(_id);
                        dispatchTakePictureIntent(_id);
                    }
                }));
                break;
            // date
            case 7:
                final TextView tv = buildTextView(label);
                linear.addView(tv);
                linear.addView(buildEditText(new View.OnClickListener() {
                    public void onClick(View v) {
                        DialogFragment newFragment = new TimePickerFragment((EditText) v);
                        newFragment.show(SurveyActivity.this.getFragmentManager(), "timePicker");
                    }
                }, id));
                break;
            // numeric input text
            case 8:
                linear.addView(buildTextView(label));
                linear.addView(buildEditTextNumeric(id));
                break;
            // dynamic form
            case 10:

                LinearLayout toInsertQuestion = new LinearLayout(this);
                ResponseComplex toModify = null;
                toInsertQuestion.setOrientation(LinearLayout.VERTICAL);
                toInsertQuestion.addView(buildSeparator());
                setLayoutParams(toInsertQuestion);

                linear.addView(buildTextView(label));
                linear.addView(buildButton(getString(R.string.survey_search), showPopupSearch(options, toInsertQuestion, id)));

                if(survey.getInstanceId() != null){
                    isModify=true;

                    for(ResponseComplex item : options){
                        if(item.getRecord_id().getUuid().equals(survey.getAnswer(id))){
                            toModify = item;
                            break;
                        }
                    }
                }else{
                    toInsertQuestion.setTag(new ResponseComplex());
                }

                if(toModify == null){
                    for( ResponseAttribute item : atributos ){
                        buildQuestion(item.getLabel(),item.getInput_id(),item.getType(),item.getResponses(),null,null,toInsertQuestion);
                    }
                }else{
                    fillDynamicForm(toModify,toInsertQuestion,id);
                }

                toInsertQuestion.addView(buildSeparator());
                linear.addView(toInsertQuestion);
                break;
        }
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
        layoutParams.setMargins(30, 30, 30, 30);

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
                LinearLayout.LayoutParams.MATCH_PARENT));
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
     * Create programatically a Multiple
     * @return
     */
    private ListView buildMultiple(List<IdOptionValue> responses,Long id){
        ListView toReturn = new ListView(this);
        toReturn.setTag(id);
        setLayoutParams(toReturn);

        final ScrollView scroll = (ScrollView) findViewById(R.id.container_employee_information);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (60*responses.size()));
        layoutParams.setMargins(30, 30, 30, 30);

        toReturn.setLayoutParams(layoutParams);

        SurveyAdapterMultipleType surveyAdapterMultipleType =
                new SurveyAdapterMultipleType(this, new ArrayList<IdOptionValue>(responses));

        if(survey.getInstanceId() != null){
            isModify=true;
            for (String value : survey.getListAnswers(id)){
                surveyAdapterMultipleType.setStatusById(Long.parseLong(value), true);
            }
        }else{
            surveyAdapterMultipleType.setFalseItems();
        }

        toReturn.setAdapter(surveyAdapterMultipleType);
        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll.requestDisallowInterceptTouchEvent(true);

                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_UP:
                        scroll.requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }
        };

        toReturn.setOnTouchListener(listener);

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
        toReturn.setAdapter(new SurveyAdapterOptionalType(this, new ArrayList<IdOptionValue>(responses)));

        if(survey.getInstanceId() != null){
            isModify=true;
            for (IdValue value : survey.getInstanceAnswers()){
                toReturn.setSelection(getIndexToSpinner(Long.parseLong(survey.getAnswer(id)),responses));
            }
        }
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
     * Create programtically a LinearLayout to print ImageView
     * @param id
     * @return ImageView with text @param id
     */
    private LinearLayout buildImageLinear(Long id) {
        LinearLayout toReturn  = new LinearLayout(this);
        toReturn.setOrientation(LinearLayout.HORIZONTAL);
        toReturn.setTag(id);
        setLayoutParams(toReturn);
        //TODO pintar la imagen que se puso durante la creación
        pictureLayouts.add(toReturn);
        return toReturn;
    }


    /**
     * Create programtically a ImageView
     * @return ImageView with text @param id
     */
    private ImageView buildImageView() {
        ImageView toReturn = new ImageView(this);
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
        LinearLayout.LayoutParams layoutIMAGE = new LinearLayout.LayoutParams(
                100,100);

        if (view instanceof TextView ) {
            layoutWRAP.setMargins(0,15,0,15);
            view.setLayoutParams(layoutWRAP);
        } else if (view instanceof LinearLayout || view instanceof EditText) {
            view.setLayoutParams(layoutMATCH);
        }else if (view instanceof ImageView){
            layoutIMAGE.setMargins(15,15,15,15);
            view.setLayoutParams(layoutIMAGE);
        }
    }

    private View buildSeparator(){
        View toReturn = new View(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1);
        layoutParams.setMargins(10, 5, 10, 5);
        toReturn.setLayoutParams(layoutParams);
        toReturn.setBackgroundColor(Color.rgb(51, 51, 51));
        return toReturn;
    }


    private View.OnClickListener showPopupSearch(final List<ResponseComplex> options, final LinearLayout linear,final Long idQuestion){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_search, null);
                ListView listOptions = (ListView) popupView.findViewById(R.id.search_list_option);
                popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
                popupWindow.setTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                listOptions.setAdapter(new OptionAdapter(popupView.getContext(), options));

                listOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        fillDynamicForm(options.get(position),linear,idQuestion);
                        popupWindow.dismiss();
                    }
                });

                ((Button) popupView.findViewById(R.id.search_close)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        popupWindow.dismiss();
                    }
                });
            }
        };

    }

    /**
     * to fill diferents fields from opcion choose
     */
    private void fillDynamicForm(ResponseComplex option,LinearLayout linear, Long idQuestion){

        linear.removeAllViews();

        LinearLayout toInsertQuestion = new LinearLayout(this);
        toInsertQuestion.setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(toInsertQuestion);


            linear.setTag(new IdValue(idQuestion, option.getRecord_id().getUuid()));
            for (ResponseItem data : option.getResponses()){
                TextView toInsert = new TextView(SurveyActivity.this);
                toInsert.setText(data.getLabel() + ": " + data.getValue());
                toInsert.setTextColor(ContextCompat.getColor(SurveyActivity.this, R.color.text_color));
                linear.addView(toInsert);
            }



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
    /**
     * Get position of the selected option
     * @param id of answer
     * @param answers lsit of answer
     * @return position of option selected
     */
    public int getIndexToSpinner(Long id, List<IdOptionValue> answers) {
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).getId().equals(id)) {
                return i;
            }
        }
        return 0;
    }


    /**
     * Event to button in question type picture
     * @param id
     */
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

    /**
     * Create file to take a picture
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/collector";
        File dir = new File(storageDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File image = new File(storageDir + "/" + imageFileName + ".jpg");
        AppSession.getInstance().setCurrentPhotoPath(image.getAbsolutePath());

        return image;
    }

    /**
     * Result of process to take a picture
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            setPic(AppSession.getInstance().getCurrentPhotoPath());
        }
    }

    /**
     * Set the picture in image view
     */
    private void setPic(String mCurrentPhotoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ImageView imageView = buildImageView();
        Long idImageView = getIntent().getExtras().getLong("idImageView", -1);
        if (idImageView > -1){
            imageView.setImageBitmap(bitmap);
            imageView.getLayoutParams().height = 100;
            imageView.getLayoutParams().width = 100;

            for(LinearLayout item : pictureLayouts){
                if(item.getTag() != null && AppSession.getInstance().getCurrentPhotoID() != null){
                    Long tagID = (Long) item.getTag();
                    if(tagID.equals(AppSession.getInstance().getCurrentPhotoID())){
                        item.addView(imageView);
                    }

                }
            }

        }
    }

    /**
     * Convert Bitmap to String to save information in database
     * @param bitmap
     * @return
     */
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }
}
