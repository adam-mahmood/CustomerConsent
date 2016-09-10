package com.supperdrug.customerconsentform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.httpclients.CustomerConsentFormRestClient;
import com.supperdrug.customerconsentform.models.SearchQuery;
import com.supperdrug.customerconsentform.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Waseem on 30/08/2016.
 */
public  class SearchStaffActivity  extends AppCompatActivity implements View.OnClickListener,WebService  {

    private static  final String TAG = SearchStaffActivity.class.getName();

    private Intent staffIntent;

    private SearchQuery searcForQuery;
    // UI references.

    private View mProgressView;
    private View mSearchStaffFormView;
    private EditText staffForename;
    private EditText staffSurname;
    private EditText staffEmail;
    private EditText staffContactNumber;
    private EditText staffDob;
    private DatePickerDialog dobPickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button searchStaff;
    private TextView errMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_staff);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        staffIntent = getIntent();

        // Set up the login form.
        findViewsById();
        setDateTimeField();
    }

    private void setDateTimeField() {
        staffDob.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        dobPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                staffDob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    private void findViewsById() {
        searchStaff = (Button) findViewById(R.id.btnSearch);
        searchStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStaff(view);
            }
        });


        staffForename = (EditText)findViewById(R.id.register_staff_forename) ;
        staffSurname = (EditText)findViewById(R.id.register_surname) ;
        staffEmail = (EditText)findViewById(R.id.register_staff_email) ;
        staffContactNumber = (EditText)findViewById(R.id.register_staff_contact_number) ;
        staffDob = (EditText)findViewById(R.id.register_staff_dob);
        staffDob.setInputType(InputType.TYPE_NULL);
        errMsg = (TextView) findViewById(R.id.search_staff_error);
        mSearchStaffFormView = findViewById(R.id.search_staff_view);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void searchStaff(View view) {
        String email = staffEmail.getText().toString();
        String forname = staffForename.getText().toString();
        String surname = staffSurname.getText().toString();
        String dob = staffDob.getText().toString();
        String number = staffContactNumber.getText().toString();
        searcForQuery = new SearchQuery(email,forname,surname,dob,number);
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();

        if (!Utility.isNotNull(email)  && !Utility.isNotNull(forname) && !Utility.isNotNull(surname) && !Utility.isNotNull(dob) && !Utility.isNotNull(number) ){
            Toast.makeText(getApplicationContext(), "Cannot perform search on a blank form. Fill at least one field", Toast.LENGTH_LONG).show();
        }else {
            params.add("email_address",email);

            params.add("surname",surname);
            params.add("forename",forname);
            params.add("contact_number",number);
            params.add("date_of_birth",dob);

            if (Utility.isNotNull(email)){
                if(Utility.validate(email)){
                    invokeWS(params);
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
                }
            }else if(  Utility.isNotNull(forname) || Utility.isNotNull(surname) || Utility.isNotNull(dob) || Utility.isNotNull(number)){
                invokeWS(params);
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
                        Toast.makeText(getApplicationContext(), "Staff Records Found!", Toast.LENGTH_LONG).show();
                        JSONArray staffJson = obj.getJSONArray("result");
                        System.out.println(staffJson);
                        // Navigate to Customer Records Screen
                        navigatetoSearchCustomerResultsActivity(staffJson);

                    }
                    // Else display error message
                    else{
                        errMsg.setText(obj.getString("message"));
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
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
                    Toast.makeText(getApplicationContext(), "Unexpected Error occured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        };
        //------------------server changes for edit staff need implementing here(  CustomerConsentFormRestClient.get("superdrug/searchstaff",params ,responsehandler);) please delete me when done .-----------------------------
        CustomerConsentFormRestClient.get("superdrug/searchstaff",params ,responsehandler);
    }

    private void navigatetoSearchCustomerResultsActivity(JSONArray staffJson) {
        Intent searchCustomerResultsIntent = new Intent(getApplicationContext(),SearchCustomerResultsActivity.class);
        searchCustomerResultsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        searchCustomerResultsIntent.putExtra("staffRecords",staffJson.toString());
        searchCustomerResultsIntent.putExtra("searchForQuery", searcForQuery);
        searchCustomerResultsIntent.putExtra("Staff_Name",staffIntent.getStringExtra("Staff_Name"));
        searchCustomerResultsIntent.putExtra("staff",staffIntent.getExtras().getParcelable("staff"));
        startActivity(searchCustomerResultsIntent);
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

            mSearchStaffFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSearchStaffFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearchStaffFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSearchStaffFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        dobPickerDialog.show();
    }

}
