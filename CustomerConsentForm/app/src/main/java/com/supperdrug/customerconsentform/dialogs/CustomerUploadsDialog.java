package com.supperdrug.customerconsentform.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.activities.WebService;
import com.supperdrug.customerconsentform.models.Customer;

/**
 * Created by Waseem on 23/09/2016.
 */
public class CustomerUploadsDialog implements WebService {
    private Customer customer;
    private Context context;
    private AlertDialog dialog;

    public CustomerUploadsDialog(Context context, Customer customer) {
        this.context = context;
        this.customer = customer;
        createDialog();
    }

    private void createDialog() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        View view = inflater.inflate(R.layout.edit_customers, null);

        AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
        adBuilder.setTitle("Edit Customer Dialog");
        adBuilder.setMessage("AlertDialog message");
        adBuilder.setView(view);

        dialog = adBuilder.create();
        dialog.show();
        findViewById(dialog,customer);
    }

    private void findViewById(final AlertDialog dialog, final Customer customer) {

    }
    @Override
    public void invokeWS(RequestParams params) {

    }
}
