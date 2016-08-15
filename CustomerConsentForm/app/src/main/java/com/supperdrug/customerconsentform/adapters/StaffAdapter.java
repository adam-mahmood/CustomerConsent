package com.supperdrug.customerconsentform.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.models.Staff;

import java.util.List;

/**
 * Created by Waseem on 09/08/2016.
 */
public class StaffAdapter extends ArrayAdapter<Staff> {
    private static class ViewHolder {
        TextView forename;
        TextView surname;
        TextView address;
        TextView emailAddress;
        TextView phoneNumber;

    }

    public StaffAdapter(Context context, int resource, List<Staff> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Staff staff = getItem(position);

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

        viewHolder.forename.setText(staff.getForename());
        viewHolder.surname.setText(staff.getSurname());
      //  viewHolder.address.setText(staff.getAddress() + "," + staff.getCity() + "," + staff.getCountry());
        viewHolder.emailAddress.setText(staff.getEmailAddress());
        // Return the completed view to render on screen
        return convertView;
    }
}

