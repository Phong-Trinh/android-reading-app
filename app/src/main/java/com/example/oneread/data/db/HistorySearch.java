package com.example.oneread.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class HistorySearch {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "search")
    public String search;

    public HistorySearch(String search) {
        this.search = search;
    }

    @Ignore
    public HistorySearch(){

    }
}
