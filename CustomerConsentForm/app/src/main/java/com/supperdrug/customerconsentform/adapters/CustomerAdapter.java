package com.supperdrug.customerconsentform.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.models.Customer;

import java.util.List;

/**
 * Created by adammahmood on 24/07/2016.
 */
public class CustomerAdapter extends ArrayAdapter<Customer> {
    // View lookup cache
    private static class ViewHolder {
        TextView forename;
        TextView surname;
        TextView address;
        TextView emailAddress;
        TextView phoneNumber;

    }

    public CustomerAdapter(Context context, int resource, List<Customer> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Customer customer = getItem(position);

/*        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_record, parent, false);

        }*/

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.customer_record, parent, false);
            viewHolder.forename = (TextView) convertView.findViewById(R.id.rec_forename_text);
            viewHolder.surname = (TextView) convertView.findViewById(R.id.rec_surname_text);
            viewHolder.address = (TextView) convertView.findViewById(R.id.rec_address_text);
            viewHolder.emailAddress = (TextView) convertView.findViewById(R.id.rec_email_address_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        viewHolder.forename.setText(customer.getForename());
        viewHolder.surname.setText(customer.getSurname());
        viewHolder.address.setText(customer.getAddress() + "," + customer.getCity() + "," + customer.getCountry());
        viewHolder.emailAddress.setText(customer.getEmailAddress());
        // Return the completed view to render on screen
        return convertView;
    }
}
