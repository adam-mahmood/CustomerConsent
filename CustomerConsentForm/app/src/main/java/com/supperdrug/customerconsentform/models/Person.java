package com.supperdrug.customerconsentform.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adammahmood on 24/07/2016.
 */
public abstract class Person implements Parcelable {
    private final String forename;
    private final String surname;
    private final String dob;
    private final String gender;


    public Person(String forename, String surname, String dob, String gender) {
        this.forename = forename;
        this.surname =surname;
        this.dob = dob;
        this.gender = gender;
    }

    public Person(Parcel in) {
        this.forename = in.readString();
        this.surname = in.readString();
        this.gender = in.readString();
        this.dob = in.readString();
    }

    public String getForename() {
        return forename;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getSurname() {
        return surname;
    }
}
