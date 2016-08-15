package com.supperdrug.customerconsentform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.httpclients.CustomerConsentFormRestClient;
import com.supperdrug.customerconsentform.models.CustomerTreatment;
import com.supperdrug.customerconsentform.models.Staff;
import com.supperdrug.customerconsentform.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adammahmood on 25/07/2016.
 */
public class SignatureAndAgreementActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,WebService {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private static  final String TAG = SignatureAndAgreementActivity.class.getName();

    private Intent intent;

    private boolean isSigned;

    private String staffName;

    private Staff staff;

    private Bitmap signature;

    private ByteArrayOutputStream bs;
    // UI references.

    private Button saveAndComplete;
    private Button clearSignature;
    private SignaturePad signaturePad;
    private TextView therapistName;
    private TextView date;
    private View mProgressView;
    private View mSignatureFormView;

    private TextView errMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature_and_agreement);
        intent = getIntent();
        staffName = intent.getStringExtra("Staff_Name");
        staff = intent.getExtras().getParcelable("staff");
        findViewsById();

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }
            @Override
            public void onSigned() {
                isSigned = true;
            }

            @Override
            public void onClear() {
                isSigned =false;
            }
        });

        clearSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });
        saveAndComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSigned ){
                    if (Utility.isNotNull(therapistName.getText().toString())){
                        navigateToUploadActivity();
                    }else{
                        Toast.makeText(getBaseContext(), "Therapist Name cannot be blank!", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getBaseContext(), "Please Sign!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void navigateToUploadActivity() {
        Intent uploadIntent = new Intent(getApplicationContext(),UploadActivity.class);
        uploadIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bs == null){
            bs = new ByteArrayOutputStream();
        }
        if (signature == null){
            signature = signaturePad.getTransparentSignatureBitmap();
        }
        signature.compress(Bitmap.CompressFormat.PNG, 50, bs);
        uploadIntent.putExtra("signature_byte_array",bs.toByteArray());
        uploadIntent.putExtra("staff",staff);
        uploadIntent.putExtra("customer",intent.getExtras().getParcelable("customer"));
        uploadIntent.putParcelableArrayListExtra("selectedTreatments2",intent.getExtras().getParcelableArrayList("selectedTreatments2"));
        startActivity(uploadIntent);
    }


    private void findViewsById() {
        signaturePad = (SignaturePad) findViewById(R.id.signature_pad) ;

        saveAndComplete = (Button) findViewById(R.id.signature_save_and_complete);

        clearSignature = (Button) findViewById(R.id.signature_clear);

        therapistName = (TextView)findViewById(R.id.signature_therapist_name_text) ;
        therapistName.setText(staff.getForename() + " " + staff.getSurname());

        date = (TextView)findViewById(R.id.signature_date_text) ;
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        date.setText(df.format(new Date()));

        mSignatureFormView = findViewById(R.id.signature_view);
        mProgressView = findViewById(R.id.signature_progress);
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    @Override
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
                        JSONArray CustomerJson = obj.getJSONArray("results");
                        // Navigate to Customer Records Screen
                        //navigatetoSearchCustomerResultsActivity(CustomerJson);

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

            mSignatureFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignatureFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignatureFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSignatureFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
