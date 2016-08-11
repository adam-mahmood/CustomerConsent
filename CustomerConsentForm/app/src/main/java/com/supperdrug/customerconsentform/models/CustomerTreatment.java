package com.supperdrug.customerconsentform.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.supperdrug.customerconsentform.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by adammahmood on 24/07/2016.
 */
public class CustomerTreatment implements Parcelable {
    private String treatmentName;
    private String testedOn;
    private  String customerForename;
    private  String customerSurname;
    private boolean isSelected;
    private String treatment_id;

    public CustomerTreatment(String testedOn, String treatmentName, String forename, String surname, String treatment_id) {
        this.testedOn = testedOn;
        this.treatmentName = treatmentName;
        this.customerForename =forename;
        this.customerSurname = surname;
        this.treatment_id = treatment_id;
    }
    // Constructor to convert JSON object into a Java class instance
    public CustomerTreatment(JSONArray object) throws JSONException {
        this.testedOn = (Utility.isNotNull(object.getString(3)))?object.getString(3):"Not Tested";
        this.treatmentName = object.getString(0);
        this.customerForename =  object.getString(1);
        this.customerSurname =  object.getString(2);
        this.treatment_id = object.getString(4);

    }

    protected CustomerTreatment(Parcel in) {
        treatmentName = in.readString();
        testedOn = in.readString();
        customerForename = in.readString();
        customerSurname = in.readString();
        isSelected = in.readByte() != 0;
        treatment_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(treatmentName);
        dest.writeString(testedOn);
        dest.writeString(customerForename);
        dest.writeString(customerSurname);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(treatment_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomerTreatment> CREATOR = new Creator<CustomerTreatment>() {
        @Override
        public CustomerTreatment createFromParcel(Parcel in) {
            return new CustomerTreatment(in);
        }

        @Override
        public CustomerTreatment[] newArray(int size) {
            return new CustomerTreatment[size];
        }
    };

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

    public String getTreatmentId() {
        return treatment_id;
    }

    public void setTreatment_id(String treatment_id) {
        this.treatment_id = treatment_id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerTreatment that = (CustomerTreatment) o;

        if (!treatmentName.equals(that.treatmentName)) return false;
        if (!customerForename.equals(that.customerForename)) return false;
        if (!customerSurname.equals(that.customerSurname)) return false;
        return treatment_id.equals(that.treatment_id);

    }

    @Override
    public int hashCode() {
        int result = treatmentName.hashCode();
        result = 31 * result + customerForename.hashCode();
        result = 31 * result + customerSurname.hashCode();
        result = 31 * result + treatment_id.hashCode();
        return result;
    }
}
