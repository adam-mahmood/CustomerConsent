package com.supperdrug.customerconsentform.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.adapters.StaffAdapter;
import com.supperdrug.customerconsentform.httpclients.CustomerConsentFormRestClient;
import com.supperdrug.customerconsentform.models.Branch;
import com.supperdrug.customerconsentform.models.Staff;
import com.supperdrug.customerconsentform.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Waseem on 03/08/2016.
 */

public class ManageStaffActivity extends AppCompatActivity implements WebService {
    private static String URL = "";
    private static final String BRANCH_URL = "superdrug/getbranches";
    private static final String STAFF_BY_BRANCH_URL = "superdrug/getstaffbybranch";

    private static  final String TAG = ManageStaffActivity.class.getName();
    private ListView staffNames;
    private View staffView;
    private Spinner branchSpinner;
    private SearchView searchStaff;
    private ImageButton addStaff;

    private JSONArray staffRecordsJsonArray;
    private final Context context =this;
    private Intent staffIntent;
    List<Branch> branchNames = new ArrayList<Branch>();
    private Staff staff;
    private Branch branch;
    private Gson gson = new Gson();


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_staff);
        findObjectsByID();
        spinnerData();
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                branch = (Branch) parentView.getItemAtPosition(position);
                System.out.println(branch.toString());
                URL = STAFF_BY_BRANCH_URL;
                RequestParams params = new RequestParams();
                params.add("branch_id",branch.getId());
                invokeWS(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        staffIntent = getIntent();
        //staff = staffIntent.getExtras().getParcelable("staff");

    }
    private void findObjectsByID()
    {
        staffNames = (ListView) findViewById(R.id.listViewStaff);
        branchSpinner = (Spinner) findViewById(R.id.spinnerBranch);
        searchStaff = (SearchView) findViewById(R.id.searchView);
        addStaff = (ImageButton) findViewById(R.id.addStaff);
        addStaff.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                navigateToCreateStaffView(v);
            }
        });
    }
    private void navigateToCreateStaffView(View view) {
        Intent createStaffIntent = new Intent(getApplicationContext(),CreateStaffActivity.class);
        createStaffIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(createStaffIntent);
    }
    private void spinnerData()
    {
        URL = BRANCH_URL;
        RequestParams params = new RequestParams();
        invokeWS(params);
    }
    private void save() {
        if (branchNames != null){
            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName() + Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  sharedPreferences.edit();

            //Serlisation
            String branchNamesJsonString = gson.toJson(branchNames);
            Log.i(TAG,branchNamesJsonString);
            editor.putString(Constants.BRANCH_NAMES_KEY,branchNamesJsonString);
            editor.apply();
        }
    }
    private void populateStaffListView()
    {
        // need array list of staff names
        // also need jason object to retrieve staff information


    }

    @Override
    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        //showProgress(true);


        AsyncHttpResponseHandler responsehandler = new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //showProgress(false);
                JSONArray jsonBranchArr;
                JSONArray resultsJson;
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    // When the JSON response has status boolean value assigned with true
                    if(obj.getInt("status") == 200){
                        Log.i(TAG,"Invoking Web Services Success!");
                        if (URL == BRANCH_URL){
                            getBranches(obj);
                            ArrayAdapter<Branch> dataAdapter = new ArrayAdapter<Branch>(context, android.R.layout.simple_spinner_item, branchNames);

                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            branchSpinner.setAdapter(dataAdapter);
                        }

                        if (URL ==  STAFF_BY_BRANCH_URL){
                            getStaff(obj);

                        }
                        // Navigate to Customer Treatments Screen
                        //navigateToTreatementsActivity(CustomerTreatmentsJson);

                    }
                    // Else display error message
                    else{
                        //errMsg.setText(obj.getString("message"));
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            private void getStaff(JSONObject obj) throws JSONException {
                Toast.makeText(getApplicationContext(), "Retrieving Staff Members!", Toast.LENGTH_LONG).show();
                staffRecordsJsonArray = obj.getJSONArray("results");
                ArrayList<Staff> arrayStaff  = Staff.fromJson(staffRecordsJsonArray);
                StaffAdapter staffAdapter = new StaffAdapter(context,1,arrayStaff);
                staffNames.setAdapter(staffAdapter);
                staffNames.setChoiceMode( ListView.CHOICE_MODE_SINGLE);
                staffNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Staff stf = (Staff) parent.getItemAtPosition(position);
                        System.out.println(stf.getForename());
                    }
                });
            }

            private void getBranches(JSONObject obj) throws JSONException {
                JSONArray resultsJson;
                JSONArray jsonBranchArr;
                Toast.makeText(getApplicationContext(), "Retrieving Branch Names!", Toast.LENGTH_LONG).show();
                resultsJson = obj.getJSONArray("results");
                for (int i=0;i<resultsJson.length();i++){
                    jsonBranchArr = (JSONArray) resultsJson.get(i);
                    branchNames.add(new Branch(jsonBranchArr.get(0).toString(),jsonBranchArr.get(1).toString()));
                }
                save();
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Log.i(TAG,"Invoking Web Services Failed");
                Log.i(TAG,"Status Code= " +statusCode);
                System.out.println(statusCode);
                // Hide Progress Dialog
               // showProgress(false);
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
        CustomerConsentFormRestClient.get(URL,params ,responsehandler);
    }


}
