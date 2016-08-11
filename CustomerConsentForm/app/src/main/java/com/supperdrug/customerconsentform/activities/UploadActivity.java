package com.supperdrug.customerconsentform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.httpclients.CustomerConsentFormRestClient;
import com.supperdrug.customerconsentform.models.Customer;
import com.supperdrug.customerconsentform.models.CustomerTreatment;
import com.supperdrug.customerconsentform.models.Staff;
import com.supperdrug.customerconsentform.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by adammahmood on 27/07/2016.
 */
public class UploadActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener,WebService {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private static  final String TAG = SearchCustomerActivity.class.getName();

    private Intent intent;

    private Customer cus;
    private Staff staff;
    private ArrayList<CustomerTreatment> selectedTreatments2;
    int[] treatmentIds;
    private JSONArray customerRecordsJsonArray;
    // UI references.

    private TextView customerName;
    private TextView therapistName;
    private TextView date;
    private TextView dob;
    private TextView email_address;
    private TextView contactNumber;
    private TextView id;
    private TextView address;
    private RelativeLayout layoutTreatments;
    private CheckedTextView checkedText;
    private TextView treatmentsLabel;
    private RelativeLayout layoutSignature;
    private ImageView signatureView;
    private View mProgressView;
    private View mUploadFormView;
    private Button upload;

    private TextView errMsg;
    private byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        intent = getIntent();
        cus =(Customer) intent.getExtras().getParcelable("customer");
        staff =(Staff) intent.getExtras().getParcelable("staff");
        selectedTreatments2 = intent.getExtras().getParcelableArrayList("selectedTreatments2");
        treatmentIds= new int[selectedTreatments2.size()];
        byteArray = intent.getByteArrayExtra("signature_byte_array");

        // Set up the login form.
        findViewsById();



    }

    private Bitmap decodeByteArray() {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }

    private void findViewsById() {

        mUploadFormView = findViewById(R.id.upload_scroll_view);
        mProgressView = findViewById(R.id.upload_progress);
        therapistName = (TextView)findViewById(R.id.upload_therapist_name_text);
        therapistName.setText(staff.getForename() + " " + staff.getSurname());
        date = (TextView) findViewById(R.id.upload_date_text);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        date.setText(df.format(new Date()));

        customerName = (TextView) findViewById(R.id.upload_customer_name_text);
        customerName.setText(cus.getForename() + " " + cus.getSurname());
        dob = (TextView)findViewById(R.id.upload_dob_text);
        dob.setText(cus.getDob());
        email_address = (TextView) findViewById(R.id.upload_email_address_text);
        email_address.setText(cus.getEmailAddress());
        contactNumber = (TextView)findViewById(R.id.upload_phone_number_text);
        contactNumber.setText(cus.getPhoneNumber());
        id = (TextView) findViewById(R.id.upload_customer_id_text);
        //id.setText(cus.getCustomerId());
        address = (TextView) findViewById(R.id.upload_customer_address_text);
        address.setText(cus.getAddress() + "," + cus.getCity() + "," + cus.getCountry());

        treatmentsLabel = (TextView)findViewById(R.id.upload_treatments_label) ;

        layoutTreatments = (RelativeLayout)findViewById(R.id.upload_treatments);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        layout.addRule(RelativeLayout.BELOW,R.id.upload_treatments_label);
        layout.setMargins(30,0,0,0);
        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
        int checkMarkDrawableResId = value.resourceId;

        for (int i = 0;i < selectedTreatments2.size();i++){
            if (i >0){
                layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                layout.addRule(RelativeLayout.RIGHT_OF,i);
                layout.addRule(RelativeLayout.BELOW,R.id.upload_treatments_label);
                layout.setMargins(30,0,0,0);
                if (i>=4){
                    layout.addRule(RelativeLayout.BELOW,1);
                    layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
                }
            }
            checkedText = new CheckedTextView(this);
            checkedText.setText(selectedTreatments2.get(i).getTreatmentName());
            checkedText.setTextSize(12);
            checkedText.setId(i+1);
            checkedText.setLayoutParams(layout);
            checkedText.setClickable(false);
            checkedText.setChecked(true);
            checkedText.setCheckMarkDrawable(checkMarkDrawableResId);
            treatmentIds[i] = Integer.parseInt(selectedTreatments2.get(i).getTreatmentId());
        }
        layoutSignature = (RelativeLayout) findViewById(R.id.upload_signature_layout);
        signatureView = (ImageView)findViewById(R.id.upload_signature_bitmap);
        signatureView.setImageBitmap(decodeByteArray());
        upload = (Button)findViewById(R.id.btn_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(v);
            }
        });
    }

    private void upload(View v) {
        String customerId = String.valueOf(cus.getCustomerId());
        String staffId = staff.getId();
        RequestParams params = new RequestParams();

        if (!Utility.isNotNull(staffId) && !Utility.isNotNull(String.valueOf(customerId)) && selectedTreatments2.isEmpty() ){
            Toast.makeText(getApplicationContext(), "Cannot upload without staff or customer details", Toast.LENGTH_LONG).show();
        }else {
            params.add("customer_id",customerId);
            params.add("staff_id",staffId);
            params.put("treatment_ids", treatmentIds);
            params.add("upload_date", String.valueOf(date.getText()));
            params.put("signature_byte_array", byteArray);
            invokeWS(params);

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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void invokeWS(RequestParams params) {
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
                        Toast.makeText(getApplicationContext(), "Upload Successfull!", Toast.LENGTH_LONG).show();
                        JSONArray CustomerJson = obj.getJSONArray("results");
                        // Navigate to Customer Records Screen
                        //navigatetoSearchCustomerResultsActivity(CustomerJson);
                        navigateToLogoutView();

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
        CustomerConsentFormRestClient.post("superdrug/upload",params ,responsehandler);
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

            mUploadFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mUploadFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mUploadFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mUploadFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void navigateToLogoutView() {
        Intent logoutIntent = new Intent(getApplicationContext(),LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // createCustomerIntent.putExtra("Staff",staff);
        startActivity(logoutIntent);

    }
}
