package com.supperdrug.customerconsentform.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Staff implements Parcelable {

    private final String forename;
    private final String surname;
    private final String dob;
    private final String gender;
    private final String id;
    //private final Date dob;
    private final String emailAddress;
    private final boolean isAdmin;

    public Staff(String id, String forename, String surname, String emailAddress, String gender, boolean isAdmin,String dob) {
        this.forename = forename;
        this.surname =surname;
        this.dob = dob;
        this.gender = gender;
        this.id = id;
        this.emailAddress = emailAddress;
        this.isAdmin = isAdmin;
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

}
