package com.supperdrug.customerconsentform.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.dialogs.EditCustomerDialog;
import com.supperdrug.customerconsentform.listeners.EditTextWatcher;
import com.supperdrug.customerconsentform.models.Customer;

import java.util.List;

/**
 * Created by adammahmood on 24/07/2016.
 */
public class CustomerAdapter extends ArrayAdapter<Customer>  {

    private AlertDialog dialog;

    // View lookup cache
    private static class ViewHolder {
        TextView forename;
        TextView surname;
        TextView address;
        TextView emailAddress;
        TextView phoneNumber;
        ImageButton edit;
        ImageButton delete;
        ImageButton info;

    }



    private Context context;

    public EditTextWatcher watcher ;

    public CustomerAdapter(Context context, int resource, List<Customer> objects, Context context1) {
        super(context, resource, objects);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Customer customer = getItem(position);

/*        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_record, parent, false);

        }*/

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.customer_record2, parent, false);
            viewHolder.forename = (TextView) convertView.findViewById(R.id.rec_forename_text);
            viewHolder.surname = (TextView) convertView.findViewById(R.id.rec_surname_text);
            viewHolder.address = (TextView) convertView.findViewById(R.id.rec_address_text);
            viewHolder.emailAddress = (TextView) convertView.findViewById(R.id.rec_email_address_text);
            viewHolder.edit = (ImageButton) convertView.findViewById(R.id.rec_btn_edit);
            viewHolder.delete = (ImageButton) convertView.findViewById(R.id.rec_btn_delete);
            viewHolder.info = (ImageButton) convertView.findViewById(R.id.rec_btn_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        viewHolder.forename.setText(customer.getForename());
        viewHolder.surname.setText(customer.getSurname());
        viewHolder.address.setText(customer.getAddress() + "," + customer.getCity() + "," + customer.getCountry());
        viewHolder.emailAddress.setText(customer.getEmailAddress());
        viewHolder.edit.setFocusable(false);
        viewHolder.edit.setFocusableInTouchMode(false);
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Edit Button Clicked");
                new EditCustomerDialog(customer,v,context);
            }
        });
        viewHolder.delete.setFocusable(false);
        viewHolder.delete.setFocusableInTouchMode(false);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Delete Button Clicked");
            }
        });

        viewHolder.info.setFocusable(false);
        viewHolder.info.setFocusableInTouchMode(false);
        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Info Button Clicked");
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }


    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
