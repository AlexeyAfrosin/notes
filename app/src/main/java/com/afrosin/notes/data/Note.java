package com.afrosin.notes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Note implements Parcelable {
    private String id;
    private String name;
    private String text;
    private Date dateCreated;

    public Note(String name, String text, Date dateCreated) {
        this.name = name;
        this.text = text;
        this.dateCreated = dateCreated;
    }

    protected Note(Parcel in) {
        name = in.readString();
        text = in.readString();
        dateCreated = new Date(in.readLong());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateCreated() {
        if (dateCreated == null) {
            return (Date) Calendar.getInstance().getTime();
        }
        return dateCreated;
    }

    public String getDateCreatedStr() {
        if (dateCreated != null) {
            return new SimpleDateFormat("dd-MM-yyyy").format(dateCreated);
        }
        return " ";

    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getText());
        parcel.writeLong(getDateCreated().getTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
