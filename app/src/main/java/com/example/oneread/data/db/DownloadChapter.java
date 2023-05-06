package com.example.oneread.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.List;

@Entity(primaryKeys = {"chapter_endpoint", "book_endpoint"})
public class DownloadChapter {

    @NonNull
    public String chapter_endpoint;
    @NonNull
    public String book_endpoint;
    public String time;
    public String title;
    public List<String> images;

    public DownloadChapter(String chapter_endpoint, String book_endpoint, String title, String time, List<String> images) {
        this.chapter_endpoint = chapter_endpoint;
        this.book_endpoint = book_endpoint;
        this.title = title;
        this.time = time;
        this.images = images;
    }

    @Ignore
    public DownloadChapter() {

    }
}
