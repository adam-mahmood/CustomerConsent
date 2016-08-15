package com.supperdrug.customerconsentform.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.adapters.CustomerAdapter;
import com.supperdrug.customerconsentform.models.Customer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Waseem on 03/08/2016.
 */

public class ManageStaffActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,WebService {

    private static  final String TAG = ManageStaffActivity.class.getName();
    private ListView staff_Names;
    private View staff_View;
    private Intent searchCustomerResultsIntent;
    private JSONArray staffRecordsJsonArray;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_staff);
        searchCustomerResultsIntent = getIntent();
        String rec = searchCustomerResultsIntent.getStringExtra("customerRecords");

        try {
            staffRecordsJsonArray = new JSONArray(rec);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot Create JSONArray");
        }
        ArrayList<Customer> arrayCustomers = Customer.fromJson(staffRecordsJsonArray);
        System.out.println(arrayCustomers);
        CustomerAdapter cusAdapter = new CustomerAdapter(this,1,arrayCustomers);
        staff_Names.setAdapter(cusAdapter);
      /*  staff_Names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  cus =(Customer) parent.getItemAtPosition(position);
                //Toast.makeText(getBaseContext(), cus.toString(), Toast.LENGTH_LONG).show();
                //   getCustomerTreatments(cus);
                // Set up the login form.*/


    }

    private void populateStaffListView()
    {
        // need array list of staff names
        // also need jason object to retrieve staff information


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
    public void invokeWS(RequestParams params) {

    }
}
