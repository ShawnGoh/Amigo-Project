package com.example.infosys1d_amigoproject.models;

import android.os.Parcel;
import android.os.Parcelable;

//Data retrieval model used to retrieve all the data about a user
public class Userdataretrieval implements Parcelable {

    private users_display usersdisplay;
    private users_private usersprivate;


    public Userdataretrieval(users_display usersdisplay, users_private usersprivate) {
        this.usersdisplay = usersdisplay;
        this.usersprivate = usersprivate;
    }

    public Userdataretrieval() {
    }

    protected Userdataretrieval(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Userdataretrieval> CREATOR = new Creator<Userdataretrieval>() {
        @Override
        public Userdataretrieval createFromParcel(Parcel in) {
            return new Userdataretrieval(in);
        }

        @Override
        public Userdataretrieval[] newArray(int size) {
            return new Userdataretrieval[size];
        }
    };

    @Override
    public String toString() {
        return "Userdataretrieval{" +
                "usersdisplay=" + usersdisplay +
                ", usersprivate=" + usersprivate +
                '}';
    }

    public users_display getUsersdisplay() {
        return usersdisplay;
    }

    public void setUsersdisplay(users_display usersdisplay) {
        this.usersdisplay = usersdisplay;
    }

    public users_private getUsersprivate() {
        return usersprivate;
    }

    public void setUsersprivate(users_private usersprivate) {
        this.usersprivate = usersprivate;
    }
}
