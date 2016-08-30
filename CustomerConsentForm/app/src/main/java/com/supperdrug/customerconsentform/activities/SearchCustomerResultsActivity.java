package com.supperdrug.customerconsentform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.adapters.CustomerAdapter;
import com.supperdrug.customerconsentform.httpclients.CustomerConsentFormRestClient;
import com.supperdrug.customerconsentform.models.Customer;
import com.supperdrug.customerconsentform.models.SearchQuery;
import com.supperdrug.customerconsentform.models.Staff;
import com.supperdrug.customerconsentform.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adammahmood on 23/07/2016.
 */
public class SearchCustomerResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private static  final String TAG = SearchCustomerActivity.class.getName();

    private Intent searchCustomerResultsIntent;

    private Customer cus;

    final Context context = this;

    private JSONArray customerRecordsJsonArray;
    // UI references.

    private TextView forename;
    private TextView suraname;
    private TextView dob;
    private TextView email_address;
    private TextView contactNumber;
    private TextView id;


    private ListView customerRecords;
    private View mProgressView;
    private View mSearchCustomerFormView;

    private TextView errMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_records);

        searchCustomerResultsIntent = getIntent();

        // Set up the login form.
        findViewsById();

        String rec = searchCustomerResultsIntent.getStringExtra("customerRecords");

        try {
            customerRecordsJsonArray = new JSONArray(rec);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot Create JSONArray");
        }
        ArrayList<Customer> arrayCustomers = Customer.fromJson(customerRecordsJsonArray);
        CustomerAdapter cusAdapter = new CustomerAdapter(this,1,arrayCustomers,context);
        customerRecords.setAdapter(cusAdapter);
        customerRecords.setChoiceMode( ListView.CHOICE_MODE_SINGLE);
        customerRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cus =(Customer) parent.getItemAtPosition(position);
                //Toast.makeText(getBaseContext(), cus.toString(), Toast.LENGTH_LONG).show();
                getCustomerTreatments(cus);
            }
        });

    }

    private void getCustomerTreatments(Customer cus) {
        String id = String.valueOf(cus.getCustomerId());
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        // When Customer ID is not Null
        if(Utility.isNotNull(id)){
            params.put("customer_id", id);
            // Invoke RESTful Web Service with Http parameters
            Log.i(TAG,"Invoking Web Services");
            invokeWS(params);
        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Customer ID Invalid", Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToTreatementsActivity(JSONArray customerJson) {
        Intent treatmentsIntent = new Intent(getApplicationContext(),TreatmentsResultsActivity.class);
        Bundle mBundle = new Bundle();
        treatmentsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        treatmentsIntent.putExtra("customerTreatments",customerJson.toString());
        treatmentsIntent.putExtra("Staff_Name",searchCustomerResultsIntent.getStringExtra("Staff_Name"));
        Staff staff = (Staff)searchCustomerResultsIntent.getExtras().getParcelable("staff");
        treatmentsIntent.putExtra("staff",staff);
        mBundle.putParcelable("customer",cus);
        treatmentsIntent.putExtras(mBundle);
        startActivity(treatmentsIntent);
    }


    private void findViewsById() {
        SearchQuery query = searchCustomerResultsIntent.getExtras().getParcelable("searchForQuery");
        forename = (TextView)findViewById(R.id.customer_records_forename_text) ;
        forename.setText(query.getForename());
        suraname = (TextView)findViewById(R.id.customer_records_surname_text) ;
        suraname.setText(query.getSurname());
        email_address = (TextView)findViewById(R.id.customer_records_email_address_text);
        email_address.setText(query.getEmail());
        id = (TextView)findViewById(R.id.customer_records_id_text);
        dob = (TextView)findViewById(R.id.customer_records_dob_text) ;
        dob.setText(query.getDob());
        customerRecords = (ListView)findViewById(R.id.customer_records_list_view) ;
        mSearchCustomerFormView = findViewById(R.id.customer_records_view);
        mProgressView = findViewById(R.id.customer_records_progress);
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params){
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
                        Toast.makeText(getApplicationContext(), "Retrieving Customer Treatments!", Toast.LENGTH_LONG).show();
                        JSONArray CustomerTreatmentsJson = obj.getJSONArray("results");
                        // Navigate to Customer Treatments Screen
                        navigateToTreatementsActivity(CustomerTreatmentsJson);

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
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        };
        CustomerConsentFormRestClient.get("superdrug/customertreatments",params ,responsehandler);
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

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}
