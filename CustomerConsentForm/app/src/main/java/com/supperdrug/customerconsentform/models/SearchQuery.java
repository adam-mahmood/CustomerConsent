package com.supperdrug.customerconsentform.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adammahmood on 26/07/2016.
 */
public class SearchQuery implements Parcelable {
    private String email;
    private String id ;
    private String forename ;
    private String surname;
    private String dob;
    private String contactNumber;

    protected SearchQuery(Parcel in) {
        email = in.readString();
        id = in.readString();
        forename = in.readString();
        surname = in.readString();
        dob = in.readString();
        contactNumber = in.readString();
    }

    public SearchQuery(String email, String id, String forename, String surname, String dob, String contactNumber) {
        this.email = email;
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.dob = dob;
        this.contactNumber = contactNumber;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(forename);
        dest.writeString(surname);
        dest.writeString(dob);
        dest.writeString(contactNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchQuery> CREATOR = new Creator<SearchQuery>() {
        @Override
        public SearchQuery createFromParcel(Parcel in) {
            return new SearchQuery(in);
        }

        @Override
        public SearchQuery[] newArray(int size) {
            return new SearchQuery[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
