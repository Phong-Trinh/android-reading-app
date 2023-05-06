package com.example.oneread.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface DownloadChapterDao {
    @Query("SELECT chapter_endpoint, book_endpoint, title, time FROM downloadChapter " +
            "WHERE book_endpoint = :book_endpoint ORDER BY time DESC")
    LiveData<List<DownloadChapter>> getAll(String book_endpoint);

    @Query("SELECT chapter_endpoint, book_endpoint, title, time FROM downloadChapter " +
            "WHERE book_endpoint = :book_endpoint ORDER BY time DESC")
    List<DownloadChapter> getList(String book_endpoint);

    @Query("SELECT * FROM downloadChapter WHERE chapter_endpoint = :chapter_endpoint AND book_endpoint = :book_endpoint")
    DownloadChapter getDetail(String chapter_endpoint, String book_endpoint);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DownloadChapter downloadChapter);

    @Query("DELETE FROM downloadchapter WHERE chapter_endpoint = :chapter_endpoint AND book_endpoint = :book_endpoint")
    void delete(String chapter_endpoint, String book_endpoint);

    @Query("DELETE FROM downloadchapter WHERE book_endpoint = :book_endpoint")
    void delete(String book_endpoint);

    @Update
    void update(DownloadChapter downloadChapter);
}
