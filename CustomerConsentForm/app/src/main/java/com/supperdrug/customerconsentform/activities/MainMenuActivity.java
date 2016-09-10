package com.supperdrug.customerconsentform.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.models.Staff;

/**
 * Created by adammahmood on 31/07/2016.
 */
public class MainMenuActivity extends AppCompatActivity {
    private Intent staffIntent;

    private Staff staff;
    // UI references.

    private View mProgressView;
    private View mMainMenuAdminFormView;
    private ImageButton searchCustomer;
    private ImageButton newCustomer;
    private ImageButton signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        staffIntent = getIntent();
        staff = staffIntent.getExtras().getParcelable("staff");
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
        searchCustomerIntent.putExtra("Staff_Name",staff.getForename() + " " + staff.getSurname());
        searchCustomerIntent.putExtra("staff",staff);
        startActivity(searchCustomerIntent);
    }
    private void navigateToLogoutView(View view) {
        Intent logoutIntent = new Intent(getApplicationContext(),LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // createCustomerIntent.putExtra("Staff",staff);
        startActivity(logoutIntent);

    }
    private void navigateToCreateCustomerView(View view) {
        Intent createCustomerIntent = new Intent(getApplicationContext(),CreateCustomerActivity.class);
        createCustomerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        createCustomerIntent.putExtra("staff",staff);
        createCustomerIntent.putExtra("Staff_Name",staff.getForename() + " " + staff.getSurname());
        // createCustomerIntent.putExtra("Staff",staff);
        startActivity(createCustomerIntent);
    }
}
