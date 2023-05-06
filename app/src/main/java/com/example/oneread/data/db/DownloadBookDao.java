package com.example.oneread.data.db;

import androidx.room.*;

import java.util.List;

@Dao
public interface DownloadBookDao {
    @Query("SELECT * FROM downloadBook")
    List<DownloadBook> getAll();

    @Query("SELECT * FROM downloadBook WHERE endpoint = :endpoint")
    DownloadBook getDetail(String endpoint);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DownloadBook downloadBook);

    @Query("DELETE FROM downloadbook WHERE endpoint = :endpoint")
    void delete(String endpoint);

    @Update
    void update(DownloadBook downloadBook);
}
