package com.supperdrug.customerconsentform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.httpclients.CustomerConsentFormRestClient;
import com.supperdrug.customerconsentform.models.Staff;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class MainMenuAdminActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private static  final String TAG = MainMenuAdminActivity.class.getName();

    private Intent staffIntent;

    private Staff staff;
    // UI references.

    private View mProgressView;
    private View mMainMenuAdminFormView;
    private ImageButton searchCustomer;
    private ImageButton newCustomer;
    private ImageButton manageAccounts;
    private ImageButton signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_admin);
        staffIntent = getIntent();
        //staff = staffIntent.getExtras().getParcelable("staff");
        // Set up the login form.
        searchCustomer = (ImageButton) findViewById(R.id.image_button_search);
        searchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSearchCustomerView(view);
            }
        });
        newCustomer = (ImageButton) findViewById(R.id.image_button_create_new_customer);
        newCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigateToCreateCustomerView(view);
            }
        });
        manageAccounts = (ImageButton) findViewById(R.id.image_button_manage_accounts);
        manageAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToManageStaffView(view);
            }
        });
        signOut = (ImageButton) findViewById(R.id.image_button_sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogoutView(view);
            }
        });
        mMainMenuAdminFormView = findViewById(R.id.main_menu_form_view);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void navigateToSearchCustomerView(View view) {
        Intent searchCustomerIntent = new Intent(getApplicationContext(),SearchCustomerActivity.class);
        searchCustomerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        searchCustomerIntent.putExtra("Staff_Name",staff.getForename() + " " + staff.getSurname());
//        searchCustomerIntent.putExtra("staff",staff);
        startActivity(searchCustomerIntent);
    }

    private void navigateToCreateCustomerView(View view) {
        Intent createCustomerIntent = new Intent(getApplicationContext(),CreateCustomerActivity.class);
        createCustomerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //createCustomerIntent.putExtra("staff",staff);
        //createCustomerIntent.putExtra("Staff_Name",staff.getForename() + " " + staff.getSurname());
       // createCustomerIntent.putExtra("Staff",staff);
        startActivity(createCustomerIntent);
    }

    private void navigateToManageStaffView(View view) {
        Intent createStaffIntent = new Intent(getApplicationContext(),ManageStaffActivity.class);
        createStaffIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //createStaffIntent.putExtra("staff",staff);
        //createStaffIntent.putExtra("Staff_Name",staff.getForename() + " " + staff.getSurname());
        // createCustomerIntent.putExtra("Staff",staff);
        startActivity(createStaffIntent);
    }
    private void navigateToLogoutView(View view) {
        Intent logoutIntent = new Intent(getApplicationContext(),LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logoutIntent);
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
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else{
                        //errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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
        CustomerConsentFormRestClient.post("superdrug/login/authenticatestaff",params ,responsehandler);
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

            mMainMenuAdminFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mMainMenuAdminFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mMainMenuAdminFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mMainMenuAdminFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

