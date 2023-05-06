package com.example.oneread.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryRead implements Parcelable {

    @SerializedName("book")
    @Expose
    private Book book;
    @SerializedName("chapter")
    @Expose
    private Chapter chapter;
    @SerializedName("time")
    @Expose
    private String time;

    public HistoryRead() {
    }

    public HistoryRead(Book book, Chapter chapter, String time) {
        this.book = book;
        this.chapter = chapter;
        this.time = time;
    }

    protected HistoryRead(Parcel in) {
        book = in.readParcelable(Book.class.getClassLoader());
        chapter = in.readParcelable(Chapter.class.getClassLoader());
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(book, flags);
        dest.writeParcelable(chapter, flags);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HistoryRead> CREATOR = new Creator<HistoryRead>() {
        @Override
        public HistoryRead createFromParcel(Parcel in) {
            return new HistoryRead(in);
        }

        @Override
        public HistoryRead[] newArray(int size) {
            return new HistoryRead[size];
        }
    };

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
