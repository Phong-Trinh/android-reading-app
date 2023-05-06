package com.example.oneread.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chapter implements Parcelable {

    @SerializedName("chapter_endpoint")
    @Expose
    private String chapterEndpoint;
    @SerializedName("book_endpoint")
    @Expose
    private String bookEndpoint;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("images")
    @Expose
    private List<String> images;

    public Chapter() {
        images = new ArrayList<>();
    }

    protected Chapter(Parcel in) {
        chapterEndpoint = in.readString();
        bookEndpoint = in.readString();
        title = in.readString();
        time = in.readString();
        images = in.createStringArrayList();
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getChapterEndpoint() {
        return chapterEndpoint;
    }

    public void setChapterEndpoint(String chapterEndpoint) {
        this.chapterEndpoint = chapterEndpoint;
    }

    public String getBookEndpoint() {
        return bookEndpoint;
    }

    public void setBookEndpoint(String bookEndpoint) {
        this.bookEndpoint = bookEndpoint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(chapterEndpoint);
        parcel.writeString(bookEndpoint);
        parcel.writeString(title);
        parcel.writeString(time);
        parcel.writeStringList(images);
    }
}
