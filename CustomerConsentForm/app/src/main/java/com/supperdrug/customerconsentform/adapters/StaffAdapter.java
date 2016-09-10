package com.supperdrug.customerconsentform.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.dialogs.EditStaffDialog;
import com.supperdrug.customerconsentform.models.Staff;

import java.util.List;

/**
 * Created by Waseem on 09/08/2016.
 */
public class StaffAdapter extends ArrayAdapter<Staff> {
    private Context context;

    private static class ViewHolder {
        TextView forename;
        TextView surname;
        TextView username;
        TextView emailAddress;
        TextView staffId;
        ImageButton edit;
        ImageButton delete;

    }

    public StaffAdapter(Context context, int resource, List<Staff> objects) {
        super(context, resource, objects);
        this.context =context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Staff staff = getItem(position);

/*        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_record, parent, false);

        }*/

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.staff_record2, parent, false);
            viewHolder.forename = (TextView) convertView.findViewById(R.id.rec_forename_text);
            viewHolder.surname = (TextView) convertView.findViewById(R.id.rec_surname_text);
            viewHolder.username = (TextView) convertView.findViewById(R.id.rec_staff_id_text);
            viewHolder.emailAddress = (TextView) convertView.findViewById(R.id.rec_email_address_text);
            viewHolder.staffId = (TextView) convertView.findViewById(R.id.rec_staff_id_text);
            viewHolder.edit = (ImageButton) convertView.findViewById(R.id.rec_btn_edit);
            viewHolder.delete = (ImageButton) convertView.findViewById(R.id.rec_btn_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        viewHolder.forename.setText(staff.getForename());
        viewHolder.surname.setText(staff.getSurname());
        viewHolder.username.setText(staff.getUsername());
        viewHolder.staffId.setText(staff.getId());
        viewHolder.emailAddress.setText(staff.getEmailAddress());
        viewHolder.edit.setFocusable(false);
        viewHolder.edit.setFocusableInTouchMode(false);
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Edit Button Clicked");
                new EditStaffDialog(staff,v,context);
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
        // Return the completed view to render on screen
        return convertView;
    }
}

