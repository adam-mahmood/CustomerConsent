package com.supperdrug.customerconsentform.models;

import com.supperdrug.customerconsentform.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by adammahmood on 24/07/2016.
 */
public class CustomerTreatment {
    private String treatmentName;
    private String testedOn;
    private  String customerForename;
    private  String customerSurname;
    private boolean isSelected;

    public CustomerTreatment(String testedOn, String treatmentName,String forename, String surname) {
        this.testedOn = testedOn;
        this.treatmentName = treatmentName;
        this.customerForename =forename;
        this.customerSurname = surname;
    }
    // Constructor to convert JSON object into a Java class instance
    public CustomerTreatment(JSONArray object) throws JSONException {
        this.testedOn = (Utility.isNotNull(object.getString(3)))?object.getString(3):"Not Tested";
        this.treatmentName = object.getString(0);
        this.customerForename =  object.getString(1);
        this.customerSurname =  object.getString(2);

    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<CustomerTreatment> fromJson(JSONArray jsonObjects) {
        ArrayList<CustomerTreatment> users = new ArrayList<CustomerTreatment>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new CustomerTreatment(jsonObjects.getJSONArray(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }
    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public String getTestedOn() {
        return testedOn;
    }

    public void setTestedOn(String testedOn) {
        this.testedOn = testedOn;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCustomerForename() {
        return customerForename;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CustomerTreatment{");
        sb.append("treatmentName='").append(treatmentName).append('\'');
        sb.append(", testedOn='").append(testedOn).append('\'');
        sb.append(", customerForename='").append(customerForename).append('\'');
        sb.append(", customerSurname='").append(customerSurname).append('\'');
        sb.append(", isSelected=").append(isSelected);
        sb.append('}');
        return sb.toString();
    }
}
