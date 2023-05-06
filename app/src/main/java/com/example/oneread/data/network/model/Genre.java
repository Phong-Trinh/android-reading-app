package com.example.oneread.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Genre implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("endpoint")
    @Expose
    private String endpoint;
    @SerializedName("description")
    @Expose
    private String description;

    public Genre() {

    }

    public Genre(Genre genre) {
        this.endpoint = genre.getEndpoint();
        this.title = genre.getTitle();
        this.description = genre.getDescription();
    }

    protected Genre(Parcel in) {
        title = in.readString();
        endpoint = in.readString();
        description = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(endpoint);
        parcel.writeString(description);
    }
}