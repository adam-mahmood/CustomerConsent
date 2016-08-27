package com.supperdrug.customerconsentform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.httpclients.CustomerConsentFormRestClient;
import com.supperdrug.customerconsentform.models.Staff;
import com.supperdrug.customerconsentform.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Waseem on 27/07/2016.
 */
public class CreateStaffActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,WebService {

    private static  final String TAG = CreateCustomerActivity.class.getName();

    private Intent intent;
    private Staff staff;
    private String genderStr;
    private String staffOrAdimStr;

    private Button addCustomerBut;

    private View mProgressView;
    private View mFormView;

    private EditText emailAddress;
    private EditText postCode;
    private EditText surname;
    private EditText forename;
    private EditText dob;
    private EditText contactNumber;
    private EditText city;
    private EditText password;
    private EditText retypePassword;
    private EditText registrationDate;
    private EditText userName;
    private EditText address;

    private RadioGroup gender;
    private RadioGroup isAdmin;

    private TextView errMsg;

    private Spinner branchSpinner;
    List<String> branchNames = new ArrayList<String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_staff);
        intent = getIntent();
        staff = intent.getExtras().getParcelable("staff");
        findViewsById();
        spinnerData();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
          client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void findViewsById() {
        // < -------- these are all View ------->
        mFormView =  findViewById(R.id.create_staff_view);
        mProgressView = findViewById(R.id.staff_progressBar);

        // < -------- these are all EditText ------->
        emailAddress = (EditText) findViewById(R.id.create_staff_email);
        postCode = (EditText) findViewById(R.id.create_staff_post_code);
        surname = (EditText) findViewById(R.id.create_staff_surname);
        forename = (EditText) findViewById(R.id.create_staff_forename);
        userName = (EditText) findViewById(R.id.create_staff_username);
        dob = (EditText) findViewById(R.id.create_staff_dob);
        city = (EditText) findViewById(R.id.create_staff_city);
        password = (EditText) findViewById(R.id.create_staff_password);
        retypePassword = (EditText) findViewById(R.id.create_staff_retype_password);
        registrationDate = (EditText) findViewById(R.id.create_staff_registration_date);
        contactNumber = (EditText) findViewById(R.id.create_staff_phone_number);
        address = (EditText) findViewById(R.id.create_staff_address);

        // < -------- these are all TextView ------->
        errMsg = (TextView) findViewById(R.id.search_error);
        // < -------- these are all Buttons ------->
        gender = (RadioGroup) findViewById(R.id.staff_radioGrp);
        isAdmin = (RadioGroup) findViewById(R.id.staff_or_adminGrp) ;
        addCustomerBut =(Button) findViewById(R.id.create_staff_button_save);
        branchSpinner = (Spinner) findViewById(R.id.spinner);

        addCustomerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStaff(view);
            }
        });

        // create customer model here
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioF = (RadioButton) group.findViewById(R.id.staff_radioF);
                RadioButton radioM = (RadioButton)group.findViewById(R.id.staff_radioM);
                if(checkedId == radioF.getId())
                {
                    genderStr = "Female";
                }
                else if (checkedId == radioM.getId())
                {
                    genderStr = "Male";
                }
            }
        });
        isAdmin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioNoButton = (RadioButton) group.findViewById(R.id.no_radio);
                RadioButton radioYesButton = (RadioButton)group.findViewById(R.id.yes_radio);
                if(checkedId == radioNoButton.getId())
                {
                    staffOrAdimStr = "false";
                }
                else if (checkedId == radioYesButton.getId())
                {
                    staffOrAdimStr = "true";
                }
            }
        });

    }
private void spinnerData()
{
    branchNames.add("BlackBurn");
    branchNames.add("Dundee");
    branchNames.add("Rochdale");
    branchNames.add("Preston");
    branchNames.add("Bolton");
    branchNames.add("Burnley");
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branchNames);

    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // attaching data adapter to spinner
    branchSpinner.setAdapter(dataAdapter);
}

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private void createStaff(View view) {
        String _email = emailAddress.getText().toString();
        String _forename = forename.getText().toString();
        String _surname = surname.getText().toString();
        String _dob = dob.getText().toString();
        String _number = contactNumber.getText().toString();
        String _city = city.getText().toString();
        String _regiDate = registrationDate.getText().toString();
        String _postCode =  postCode.getText().toString();
        String _password = password.getText().toString();
        String _retypePassword = retypePassword.getText().toString();
        String _username = userName.getText().toString();
        String _address = address.getText().toString();
        //searcForQuery = String.format("Search Query:\n Forename = %s \t\t\t\t\t\t\t\t\t\t\t\t Surname = %s \t\t\t\t\t\t\t\t\t Customer ID = %s \n Email Address = %s \n DOB = %s \t\t\t\t\t\t\t\t\t\t\t\t\t\t Phone Number = %s",forname,surname,id,email,dob,number);
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();

        if (!Utility.isNotNull(_email) &&  !Utility.isNotNull(_forename) && !Utility.isNotNull(_surname) && !Utility.isNotNull(_dob) && !Utility.isNotNull(_number)&& !Utility.isNotNull(_postCode)&& !Utility.isNotNull(_city)
                && !Utility.isNotNull(_regiDate)  )
        {
            Toast.makeText(getApplicationContext(), "Please fill in required text field", Toast.LENGTH_LONG).show();
        }
        else {
            if (_password.equals(_retypePassword))
            {
                params.add("email_address", _email);
                params.add("surname", _surname);
                params.add("forename", _forename);
                params.add("phone_number", _number);
                params.add("address",_address);
                params.add("dob", _dob);
                params.add("post_code", _postCode);
                params.add("city", _city);
                params.add("registration_date", _regiDate);
                params.add("gender", genderStr);
                params.add("is_admin",staffOrAdimStr);
                params.add("password", _password);
                params.add("branch_name",branchSpinner.getSelectedItem().toString());
                params.add("username",_username);
                invokeWS(params);
/*                if (Utility.isNotNull(_email) ) {
                    if (Utility.validate(_email))
                    {
                        invokeWS(params);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
                    }
                }*/
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Password are not the same ", Toast.LENGTH_LONG).show();
            }
        }


    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    @Override
    public void invokeWS(final RequestParams params){
        // Show Progress Dialog
        showProgress(true);


        AsyncHttpResponseHandler responsehandler = new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                showProgress(false);
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getInt("status") == 200){
                        Log.i(TAG,"Invoking Web Services Success!");
                        Toast.makeText(getApplicationContext(), "Staff Has been created successfully!", Toast.LENGTH_LONG).show();
                        JSONArray CustomerJson = obj.getJSONArray("result");

                        // Navigate to Customer Records Screen
                        navigatetActivity(CustomerJson);

                    }
                    // Else display error message
                    else{
                        errMsg.setText(obj.getString("message"));
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Log.i(TAG,"Invoking Web Services Failed");
                Log.i(TAG,"Status Code= " +statusCode);
                System.out.println(statusCode);
                // Hide Progress Dialog
                showProgress(false);
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        };
        CustomerConsentFormRestClient.post("superdrug/createstaff",params ,responsehandler);
    }

    private void navigatetActivity(JSONArray customerJson)
    {
        Intent homeIntent = new Intent(getApplicationContext(),MainMenuAdminActivity.class);
        homeIntent.putExtra("staff",staff);
        homeIntent.putExtra("Staff_Name",staff.getForename() + " " + staff.getSurname());
        startActivity(homeIntent);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
