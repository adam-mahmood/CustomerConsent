package com.supperdrug.customerconsentform.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by adammahmood on 24/07/2016.
 */
public class Customer implements Parcelable {
    private final String forename;
    private final String surname;
    private final String dob;
    private final String gender;
    private int customerId;
    private String emailAddress;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    public Customer(String forname,String surname, String dob, String gender, int customerId, String emailAddress, String phoneNumber, String address, String city, String country) {
        this.forename =forname;
        this.surname = surname;
        this.dob = dob;
        this.gender = gender;
        this.customerId = customerId;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.country = country;
    }
    protected Customer(Parcel in) {
        this.forename = in.readString();
        this.surname = in.readString();
        this.gender = in.readString();
        this.dob = in.readString();
        customerId = in.readInt();
        emailAddress = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        city = in.readString();
        country = in.readString();
    }
    // Constructor to convert JSON object into a Java class instance
    public Customer(JSONArray object) throws JSONException {
        this.forename = object.getString(1);
        this.surname = object.getString(2);
        this.gender = object.getString(3);
        this.dob = object.getString(9);
        this.customerId = Integer.parseInt(object.getString(0));
        this.emailAddress = object.getString(4);
        this.phoneNumber = object.getString(8);
        this.address = object.getString(5);
        this.city = object.getString(6);
        this.country = object.getString(7);

    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Customer> fromJson(JSONArray jsonObjects) {
        ArrayList<Customer> users = new ArrayList<Customer>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new Customer(jsonObjects.getJSONArray(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(forename);
        dest.writeString(surname);
        dest.writeString(gender);
        dest.writeString(dob);
        dest.writeInt(customerId);
        dest.writeString(emailAddress);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(country);
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Customer{");
        sb.append("customerId=").append(customerId);
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getSurname() {
        return surname;
    }

    public String getForename() {
        return forename;
    }
}
