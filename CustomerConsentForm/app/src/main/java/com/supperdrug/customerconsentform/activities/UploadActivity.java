package com.supperdrug.customerconsentform.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.adapters.CustomerAdapter;
import com.supperdrug.customerconsentform.models.Customer;
import com.supperdrug.customerconsentform.models.Staff;

import org.json.JSONArray;
import org.json.JSONException;

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
    private ArrayList<String> selectedTreatments;

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

    private View mProgressView;
    private View mUploadFormView;

    private TextView errMsg;
    private byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        intent = getIntent();
        cus =(Customer) intent.getExtras().getParcelable("customer");
        staff =(Staff) intent.getExtras().getParcelable("staff");
        selectedTreatments = intent.getStringArrayListExtra("selectedTreatments");
        byteArray = intent.getByteArrayExtra("signature_byte_array");

        // Set up the login form.
        findViewsById();



    }

    private Bitmap decodeByteArray() {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }

    private void findViewsById() {

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

        layoutTreatments = (RelativeLayout)findViewById(R.id.upload_treatments);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        for (int i = 0;i < selectedTreatments.size();i++){
            checkedText = new CheckedTextView(this);
            checkedText.setText(selectedTreatments.get(i));
            checkedText.setId(i);
            checkedText.setLayoutParams(layout);
            checkedText.setClickable(false);
            layoutTreatments.addView(checkedText);
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

    }
}
