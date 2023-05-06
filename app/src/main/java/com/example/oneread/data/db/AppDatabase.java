package com.example.oneread.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/*
https://developer.android.com/training/data-storage/room/migrating-db-versions
 */
@Database(version = 1,
        entities = {HistorySearch.class, DownloadChapter.class, DownloadBook.class})
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract HistorySearchDao historySearchDao();

    public abstract DownloadBookDao downloadBookDao();

    public abstract DownloadChapterDao downloadChapterDao();
}