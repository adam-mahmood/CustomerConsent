package com.supperdrug.customerconsentform.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class Staff implements Parcelable {

    private String username;
    private final String branchId;
    private String forename;
    private String surname;
    private String dob;
    private String gender;
    private final String id;
    private String address;
    private String city;
    private String postCode;
    private String phoneNumber;
    private String regDate;
    //private final Date dob;
    private String emailAddress;
    private  boolean isAdmin;

    public Staff(String id, String forename, String surname, String emailAddress, String gender, boolean isAdmin, String dob, String address, String city, String postCode, String phoneNumber, String regDate, String branchId, String username) {
        this.id = id;
        this.forename = forename;
        this.surname =surname;
        this.dob = dob;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.isAdmin = isAdmin;
        this.address = address;
        this.city = city;
        this.postCode = postCode;
        this.phoneNumber = phoneNumber;
        this.regDate = regDate;
        this.branchId = branchId;
        this.username = username;
    }

    /**
     * Use when reconstructing User object from parcel
     * This will be used only by the 'CREATOR'
     * @param in a parcel to read this object
     */
    public Staff(Parcel in) {
        this.forename = in.readString();
        this.surname = in.readString();
        this.gender = in.readString();
        this.dob = in.readString();
        this.id = in.readString();
        this.emailAddress = in.readString();
        this.isAdmin =  in.readInt() == 0? false : true;
        this.address = in.readString();
        this.city =in.readString();
        this.postCode =in.readString();
        this.phoneNumber = in.readString();
        this.regDate = in.readString();
        this.branchId = in.readString();
        this.username = in.readString();
    }
    /**
     * Actual object serialization happens here, Write object content
     * to parcel one by one, reading should be done according to this write order
     * @param dest parcel
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(forename);
        dest.writeString(surname);
        dest.writeString(gender);
        dest.writeString(dob);
        dest.writeString(id);
        dest.writeString(emailAddress);
        dest.writeInt(isAdmin ? 1 : 0);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(postCode);
        dest.writeString(phoneNumber);
        dest.writeString(regDate);
        dest.writeString(branchId);
        dest.writeString(username);

    }
    public Staff(JSONArray object) throws JSONException {
        this.id = object.getString(0);
        this.forename = object.getString(1);
        this.surname = object.getString(2);
        this.gender = object.getString(3);
        this.emailAddress = object.getString(4);
        this.dob = object.getString(5);
        this.phoneNumber = object.getString(6);
        this.isAdmin = object.getString(7).equalsIgnoreCase("true")?true:false;
        this.username = object.getString(8);
        this.regDate = object.getString(9);
        this.branchId = object.getString(10);
        this.address = object.getString(11);
        this.city = object.getString(12);
        this.postCode =  object.getString(13);


    }
    public String getId() {
        return id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    /**
     * Define the kind of object that you gonna parcel,
     * You can use hashCode() here
     */
    @Override
    public int describeContents() {
        return hashCode();
    }


    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     *
     * If you don’t do that, Android framework will through exception
     * Parcelable protocol requires a Parcelable.Creator object called CREATOR
     */
    public static final Parcelable.Creator<Staff> CREATOR = new Parcelable.Creator<Staff>() {

        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        if (!(o instanceof Staff)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Staff staff = (Staff) o;

        // Compare the data members and return accordingly
        return this.id == staff.id && this.forename == staff.forename && this.surname == staff.surname && this.emailAddress == staff.emailAddress;
    }

    public static ArrayList<Staff> fromJson(JSONArray jsonObjects) {
        ArrayList<Staff> users = new ArrayList<Staff>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new Staff(jsonObjects.getJSONArray(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
