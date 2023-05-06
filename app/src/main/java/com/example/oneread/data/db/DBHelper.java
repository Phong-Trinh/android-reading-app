package com.example.oneread.data.db;

import androidx.lifecycle.LiveData;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.di.anotation.FixedThreadPool;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Singleton
public class DBHelper {

    private AppDatabase appDatabase;
    private ExecutorService executor;
    private HistorySearchDao historySearchDao;
    private DownloadBookDao downloadBookDao;
    private DownloadChapterDao downloadChapterDao;

    @Inject
    public DBHelper(AppDatabase appDatabase, @FixedThreadPool ExecutorService executor) {
        this.appDatabase = appDatabase;
        this.executor = executor;
        historySearchDao = appDatabase.historySearchDao();
        downloadBookDao = appDatabase.downloadBookDao();
        downloadChapterDao = appDatabase.downloadChapterDao();
    }

    public LiveData<List<HistorySearch>> getHistorySearch() {
        return historySearchDao.getAll();
    }

    public void insertHistorySearch(HistorySearch historySearch) {
        executor.execute(() -> {
            historySearchDao.insert(historySearch);
        });
    }

    public void deleteHistorySearch(HistorySearch historySearch) {
        executor.execute(() -> {
            historySearchDao.delete(historySearch);
        });
    }

    public List<Book> getAllBookDownloaded() {
        List<Book> list = new ArrayList<>();
        Future<List<DownloadBook>> future = executor.submit(() -> downloadBookDao.getAll());
        List<DownloadBook> downloadBooks = null;
        try {
            downloadBooks = future.get();
            for (DownloadBook downloadBook : downloadBooks) {
                Book book = new Gson().fromJson(downloadBook.detail, Book.class);
                list.add(book);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Book getDetailDownloadBook(String endpoint) {
        Future<DownloadBook> future = executor.submit(() -> downloadBookDao.getDetail(endpoint));
        try {
            DownloadBook downloadBook = future.get();
            return new Gson().fromJson(downloadBook.detail, Book.class);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertDownloadBook(DownloadBook downloadBook) {
        executor.execute(() -> {
            downloadBookDao.insert(downloadBook);
        });
    }

    public void updateDownloadBook(DownloadBook downloadBook) {
        executor.execute(() -> {
            downloadBookDao.update(downloadBook);
        });
    }

    public void deleteDownloadBook(String endpoint) {
        try {
            executor.submit(() -> {
                downloadBookDao.delete(endpoint);
                downloadChapterDao.delete(endpoint);
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<DownloadChapter>> getAllChapterDownloaded(String book_endpoint) {
        return downloadChapterDao.getAll(book_endpoint);
    }

    public List<Chapter> getChapterOfDownloadedBook(String book_endpoint) {
        List<Chapter> list = new ArrayList<>();
        Future<List<DownloadChapter>> future = executor.submit(() -> downloadChapterDao.getList(book_endpoint));
        List<DownloadChapter> downloadChapters = null;
        try {
            downloadChapters = future.get();
            for (DownloadChapter downloadChapter : downloadChapters) {
                Chapter chapter = new Chapter();
                chapter.setChapterEndpoint(downloadChapter.chapter_endpoint);
                chapter.setBookEndpoint(downloadChapter.book_endpoint);
                chapter.setTitle(downloadChapter.title);
                chapter.setTime(downloadChapter.time);
                list.add(chapter);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Chapter getDetailDownloadChapter(String chapter_endpoint, String book_endpoint) {
        Future<DownloadChapter> future = executor.submit(() -> downloadChapterDao.getDetail(chapter_endpoint, book_endpoint));
        try {
            DownloadChapter downloadChapter = future.get();
            Chapter chapter = new Chapter();
            chapter.setChapterEndpoint(downloadChapter.chapter_endpoint);
            chapter.setBookEndpoint(downloadChapter.book_endpoint);
            chapter.setTitle(downloadChapter.title);
            chapter.setTime(downloadChapter.time);
            chapter.setImages(downloadChapter.images);
            return chapter;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertDownloadChapter(DownloadChapter downloadChapter) {
        executor.execute(() -> {
            downloadChapterDao.insert(downloadChapter);
        });
    }

    public void updateDownloadChapter(DownloadChapter downloadChapter) {
        executor.execute(() -> {
            downloadChapterDao.update(downloadChapter);
        });
    }

    public void deleteDownloadChapter(String chapter_endpoint, String book_endpoint) {
        try {
            executor.submit(() -> downloadChapterDao.delete(chapter_endpoint, book_endpoint)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
