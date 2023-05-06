package com.example.oneread.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class DownloadBook {
    @PrimaryKey
    @NonNull
    public String endpoint;
    public String detail;


    public DownloadBook(String endpoint, String detail) {
        this.endpoint = endpoint;
        this.detail = detail;

    }

    @Ignore
    public DownloadBook() {

    }
}
