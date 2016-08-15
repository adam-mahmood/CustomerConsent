package com.supperdrug.customerconsentform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by adammahmood on 22/07/2016.
 */
@SuppressWarnings("deprecation")
public class SearchCustomerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener,WebService {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private static  final String TAG = SearchCustomerActivity.class.getName();

    private Intent staffIntent;

    private SearchQuery searcForQuery;
    // UI references.

    private View mProgressView;
    private View mSearchCustomerFormView;
    private EditText customerId;
    private EditText customerForename;
    private EditText customerSurname;
    private EditText customerEmail;
    private EditText customerContactNumber;
    private EditText customerDob;
    private DatePickerDialog dobPickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button searchCustomer;
    private TextView errMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_customer);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        staffIntent = getIntent();

        // Set up the login form.
        findViewsById();
        setDateTimeField();
    }

    private void setDateTimeField() {
        customerDob.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        dobPickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                customerDob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    private void findViewsById() {
        searchCustomer = (Button) findViewById(R.id.btnSearch);
        searchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCustomer(view);
            }
        });

        customerId = (EditText)findViewById(R.id.customer_id) ;
        customerForename = (EditText)findViewById(R.id.register_forename) ;
        customerSurname = (EditText)findViewById(R.id.register_surname) ;
        customerEmail = (EditText)findViewById(R.id.register_email) ;
        customerContactNumber = (EditText)findViewById(R.id.register_contact_number) ;
        customerDob = (EditText)findViewById(R.id.register_dob);
        customerDob.setInputType(InputType.TYPE_NULL);
        errMsg = (TextView) findViewById(R.id.search_error);
        mSearchCustomerFormView = findViewById(R.id.search_customer_view);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void searchCustomer(View view) {
        String email = customerEmail.getText().toString();
        String id = customerId.getText().toString();
        String forname = customerForename.getText().toString();
        String surname = customerSurname.getText().toString();
        String dob = customerDob.getText().toString();
        String number = customerContactNumber.getText().toString();
        searcForQuery = new SearchQuery(email,id,forname,surname,dob,number);
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();

        if (!Utility.isNotNull(email) && !Utility.isNotNull(id) && !Utility.isNotNull(forname) && !Utility.isNotNull(surname) && !Utility.isNotNull(dob) && !Utility.isNotNull(number) ){
            Toast.makeText(getApplicationContext(), "Cannot perform search on a blank form. Fill at least one field", Toast.LENGTH_LONG).show();
        }else {
            params.add("email_address",email);
            params.add("customer_id",id);
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
            }else if( Utility.isNotNull(id) || Utility.isNotNull(forname) || Utility.isNotNull(surname) || Utility.isNotNull(dob) || Utility.isNotNull(number)){
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
                        Toast.makeText(getApplicationContext(), "Customer Records Found!", Toast.LENGTH_LONG).show();
                        JSONArray CustomerJson = obj.getJSONArray("result");
                        System.out.println(CustomerJson);
                        // Navigate to Customer Records Screen
                        navigatetoSearchCustomerResultsActivity(CustomerJson);

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
        CustomerConsentFormRestClient.get("superdrug/searchcustomer",params ,responsehandler);
    }

    private void navigatetoSearchCustomerResultsActivity(JSONArray customerJson) {
        Intent searchCustomerResultsIntent = new Intent(getApplicationContext(),SearchCustomerResultsActivity.class);
        searchCustomerResultsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        searchCustomerResultsIntent.putExtra("customerRecords",customerJson.toString());
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

            mSearchCustomerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSearchCustomerFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearchCustomerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSearchCustomerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onClick(View view) {
        dobPickerDialog.show();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}
