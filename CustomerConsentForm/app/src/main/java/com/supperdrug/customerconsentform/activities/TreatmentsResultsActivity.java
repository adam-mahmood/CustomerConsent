package com.supperdrug.customerconsentform.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.adapters.TreatmentsAdapter;
import com.supperdrug.customerconsentform.models.CustomerTreatment;
import com.supperdrug.customerconsentform.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by adammahmood on 24/07/2016.
 */
public class TreatmentsResultsActivity extends AppCompatActivity {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private static  final String TAG = TreatmentsResultsActivity.class.getName();

    private Intent treatmentsIntent;

    private String customerTreatments;

    private boolean checked;

    private ArrayList<CustomerTreatment> selectedTreatments2 = new ArrayList<>();
    // UI references.

    private Button next;
    private ListView treatments;
    private View mProgressView;
    private View mCustomerTreatmentsFormView;

    private TextView errMsg;
    private JSONArray customerTretmentsJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treatments);

        treatmentsIntent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName() + Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        customerTreatments = sharedPreferences.getString(Constants.CUSTOMER_TREATMENTS_KEY,"N/A");
        Log.i(TAG,"Customer Treatments: " + customerTreatments);
        //customerTreatments = treatmentsIntent.getStringExtra("customerTreatments");
        // Set up the login form.
        findViewsById();
        try {
            customerTretmentsJsonArray = new JSONArray(customerTreatments);
        } catch (JSONException e) {
            throw new IllegalStateException("Cannot Create JSONArray",e);
        }

        ArrayList<CustomerTreatment> arrayCustomerTreatments = CustomerTreatment.fromJson(customerTretmentsJsonArray);
        TreatmentsAdapter cusAdapter = new TreatmentsAdapter(this,0,arrayCustomerTreatments);
        treatments.setAdapter(cusAdapter);
        treatments.setItemsCanFocus(false);
        treatments.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        treatments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerTreatment cus =(CustomerTreatment) parent.getItemAtPosition(position);
                CheckBox selected =(CheckBox) view.findViewById(R.id.rec_treatment_selected);
                cus.setSelected(!cus.isSelected());
                selected.setChecked(cus.isSelected());
                if(cus.isSelected()){
                    selectedTreatments2.add(cus);
                }else {
                    if(selectedTreatments2.contains(cus)){
                        selectedTreatments2.remove(cus);
                    }
                }
                //Toast.makeText(getBaseContext(), cus.toString(), Toast.LENGTH_LONG).show();

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTreatments2.isEmpty()){
                    Toast.makeText(getBaseContext(), "Please select a treatment before proceeding ahead!", Toast.LENGTH_LONG).show();
                }else {
                    saveData(selectedTreatments2);
                    navigateToSignatureAndAgreementActivity();
                }

            }
        });
    }

    private void saveData(ArrayList<CustomerTreatment> selectedTreatments2) {
        SharedPreferences sharedpreferences = getSharedPreferences(getPackageName()+Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();

        editor.putString(Constants.SELECTED_TREATMENTS_KEY,gson.toJson(selectedTreatments2));
        editor.apply();
    }

    private void navigateToSignatureAndAgreementActivity() {
        Intent signatureIntent = new Intent(getApplicationContext(),SignatureAndAgreementActivity.class);
        signatureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //signatureIntent.putExtra("Staff_Name",treatmentsIntent.getStringExtra("Staff_Name"));
        //Staff staff = (Staff) treatmentsIntent.getExtras().getParcelable("staff");
        //signatureIntent.putExtra("staff",staff);
        //signatureIntent.putExtra("customer",cus);
        //signatureIntent.putParcelableArrayListExtra("selectedTreatments2",selectedTreatments2);
        startActivity(signatureIntent);
    }


    private void findViewsById() {
        next = (Button) findViewById(R.id.btn_treatments_next) ;
        treatments = (ListView)findViewById(R.id.treatments_records_list_view) ;
        mCustomerTreatmentsFormView = findViewById(R.id.customer_treatments_view);
        mProgressView = findViewById(R.id.customer_treatments_progress);
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

            mCustomerTreatmentsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCustomerTreatmentsFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCustomerTreatmentsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mCustomerTreatmentsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
