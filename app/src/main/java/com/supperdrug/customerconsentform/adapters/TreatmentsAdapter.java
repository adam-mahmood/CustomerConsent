package com.supperdrug.customerconsentform.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.models.Customer;
import com.supperdrug.customerconsentform.models.CustomerTreatment;
import com.supperdrug.customerconsentform.utilities.Utility;

import java.util.List;

/**
 * Created by adammahmood on 24/07/2016.
 */
public class TreatmentsAdapter extends ArrayAdapter<CustomerTreatment> {
    // View lookup cache
    private static class ViewHolder {
        TextView treatementName;
        CheckBox isSelected;
        TextView tested;


    }
    public TreatmentsAdapter(Context context, int resource, List<CustomerTreatment> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CustomerTreatment customerTreatment = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.treatment_record, parent, false);
            viewHolder.treatementName = (TextView) convertView.findViewById(R.id.rec_treatment_name_text);
            viewHolder.isSelected = (CheckBox) convertView.findViewById(R.id.rec_treatment_selected);
            viewHolder.tested = (TextView) convertView.findViewById(R.id.rec_treatment_tested_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        viewHolder.treatementName.setText(customerTreatment.getTreatmentName());
        viewHolder.isSelected.setChecked(customerTreatment.isSelected());
        String tested = (Utility.isNotNull(customerTreatment.getTestedOn())) ? customerTreatment.getTestedOn() : "Not Tested";
        viewHolder.tested.setText(tested);

        // Return the completed view to render on screen
        return convertView;
    }
}
