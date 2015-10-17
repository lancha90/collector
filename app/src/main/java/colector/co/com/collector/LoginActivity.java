package colector.co.com.collector;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import colector.co.com.collector.http.AsyncResponse;
import colector.co.com.collector.http.BackgroundTask;
import colector.co.com.collector.http.ResourceNetwork;
import colector.co.com.collector.model.Survey;
import colector.co.com.collector.model.request.GetSurveysRequest;
import colector.co.com.collector.model.request.LoginRequest;
import colector.co.com.collector.model.response.ErrorResponse;
import colector.co.com.collector.model.response.GetSurveysResponse;
import colector.co.com.collector.model.response.LoginResponse;
import colector.co.com.collector.persistence.dao.SurveyDAO;
import colector.co.com.collector.session.AppSession;
import colector.co.com.collector.settings.AppSettings;
import colector.co.com.collector.utils.Utilities;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);

    }


    public void doLogin(View view){

        if(etUsername.getText().toString().trim().equals("") || etPassword.getText().toString().trim().equals("")){
            Toast.makeText(this,getString(R.string.login_error_empty),Toast.LENGTH_LONG).show();
        }else if(!Utilities.isNetworkConnected(this)) {

            Toast.makeText(this, getString(R.string.common_internet_not_available), Toast.LENGTH_LONG).show();
            List<Survey> offlineSurvey = new SurveyDAO(this).getSurveyAvailable();

            if(offlineSurvey != null && offlineSurvey.size() > 0) {
                AppSession.getInstance().setSurveyAvailable(offlineSurvey);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, getString(R.string.common_internet_not_available), Toast.LENGTH_LONG).show();
            }

        }else {

            LoginRequest toSend = new LoginRequest(etUsername.getText().toString(), etPassword.getText().toString());
            AsyncResponse callback = new AsyncResponse() {
                @Override
                public void callback(Object output) {

                    if(output instanceof LoginResponse){
                        LoginResponse response = (LoginResponse) output;

                        if(response.getResponseCode().equals(AppSettings.HTTP_OK) ){
                            // Invoke the survey synchronize
                            getSurveys();

                        }else{
                            Toast.makeText(LoginActivity.this,response.getResponseDescription(),Toast.LENGTH_LONG).show();
                        }

                    }else if(output instanceof ErrorResponse){
                        ErrorResponse response =(ErrorResponse) output;
                        Toast.makeText(LoginActivity.this,response.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            };

            BackgroundTask bt = new BackgroundTask(this, toSend, new LoginResponse(), callback);
            bt.execute(ResourceNetwork.URL_LOGIN);

        }

    }



    private void getSurveys(){

        GetSurveysRequest toSend = new GetSurveysRequest(AppSession.getInstance().getToken());
        AsyncResponse callback = new AsyncResponse() {
            @Override
            public void callback(Object output) {

                if(output instanceof GetSurveysResponse){

                    GetSurveysResponse response = (GetSurveysResponse) output;

                    if(response.getResponseCode().equals(AppSettings.HTTP_OK)){
                        SurveyDAO dao = new SurveyDAO(LoginActivity.this);
                        dao.synchronizeSurvey(response.getResponseData());
                        AppSession.getInstance().setSurveyAvailable(response.getResponseData());

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this,response.getResponseDescription(),Toast.LENGTH_LONG).show();
                    }


                }else if(output instanceof ErrorResponse){
                    ErrorResponse response =(ErrorResponse) output;
                    Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        };

        BackgroundTask bt = new BackgroundTask(this, toSend, new GetSurveysResponse(), callback);
        bt.execute(ResourceNetwork.URL_SURVEY);
    }

}
