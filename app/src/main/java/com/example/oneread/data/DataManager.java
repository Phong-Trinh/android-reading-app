package com.example.oneread.data;

import android.os.Environment;
import androidx.lifecycle.LiveData;
import com.example.oneread.data.db.DBHelper;
import com.example.oneread.data.db.DownloadBook;
import com.example.oneread.data.db.DownloadChapter;
import com.example.oneread.data.db.HistorySearch;
import com.example.oneread.data.file.FileHelper;
import com.example.oneread.data.network.ApiHelper;
import com.example.oneread.data.network.model.*;
import com.example.oneread.data.prefs.PrefsHelper;
import com.example.oneread.utils.AppConstants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;

@Singleton
public class DataManager {
    private static final String TAG = "DataManager";
    private final PrefsHelper prefsHelper;
    private final ApiHelper apiHelper;
    private final DBHelper dbHelper;
    private final FileHelper fileHelper;

    @Inject
    public DataManager(PrefsHelper prefsHelper,
                       ApiHelper apiHelper, DBHelper dbHelper, FileHelper fileHelper) {
        this.prefsHelper = prefsHelper;
        this.apiHelper = apiHelper;
        this.dbHelper = dbHelper;
        this.fileHelper = fileHelper;
    }








    public void saveUser(User user, String accessToken) {
        user.setAccessToken(accessToken);
        setCurrentUser(user);
        setAccessToken(accessToken);
    }

    public User getCurrentUser() {
        return prefsHelper.getCurrentUser();
    }

    public void setCurrentUser(User user) {
        prefsHelper.setCurrentUser(user);
    }

    public void removeCurrentUser() {
        prefsHelper.removeCurrentUser();
    }

    public String getAccessToken() {
        return prefsHelper.getAccessToken();
    }

    public void setAccessToken(String accessToken) {
        prefsHelper.setAccessToken(accessToken);
    }

    public void removeAccessToken() {
        prefsHelper.removeAccessToken();
    }

    public int getNightMode() {
        return prefsHelper.getNightMode();
    }

    public void setNightMode(int mode) {
        prefsHelper.setNightMode(mode);
    }





    /*=========================API=========================*/

    public Observable<Response<User>> requestInfoUser(String username) {
        return apiHelper.getInfoUser(username);
    }

    public Observable<Response<User>> updateUserInfo(String auth, MultipartBody.Part[] body) {
        return apiHelper.updateUserInfo(auth, body);
    }

    public Observable<Response<User>> updatePassword(String auth, JsonObject password) {
        return apiHelper.updatePassword(auth, password);
    }

    public Observable<Response<User>> verifyEmail(String auth) {
        return apiHelper.verifyEmail(auth);
    }

    public Observable<Response<Book>> requestListBookFollowing(String auth) {
        return apiHelper.getListBookFollowing(auth);
    }

    public Observable<Response<HistoryRead>> requestAllHistoryRead(String auth) {
        return apiHelper.getAllHistoryRead(auth);
    }

    public Observable<Response<HistoryRead>> requestHistoryRead(String auth, String bookEndpoint) {
        return apiHelper.getHistoryRead(auth, bookEndpoint);
    }

    public Observable<Response<LoginResponse>> requestLogin(User user) {
        return apiHelper.login(user);
    }

    public Observable<Response<String>> requestRegister(User user) {return apiHelper.register(user);}

    public Observable<Response<Book>> followBook(String auth, String book_endpoint) {return apiHelper.followBook(auth, book_endpoint);}

    public Observable<Response<Book>> unfollowBook(String auth, String book_endpoint) {return apiHelper.unfollowBook(auth, book_endpoint);}

    public Observable<Response<HistoryRead>> deleteAllHistoryRead(String auth) {return apiHelper.deleteAllHistoryRead(auth);}

    public Observable<Response<HistoryRead>> deleteSingleHistoryRead(String auth, String book_endpoint) {return apiHelper.deleteSingleHistoryRead(auth, book_endpoint);}




    public Observable<Response<Book>> searchBook(String filter) {return apiHelper.searchBook(filter);}

    public Observable<Response<Book>> requestSuggestBooks(String auth) {return apiHelper.getSuggestBooks(auth);}

    public Observable<Response<Book>> requestTrending() {return apiHelper.getTrending();}

    public Observable<Response<Book>> requestTopSearch() {return apiHelper.topSearch();}

    public Observable<Response<Book>> requestRelateBooks(String bookEndpoint) {return apiHelper.relateBooks(bookEndpoint);}

    public Observable<Response<Book>> requestBook(String bookEndpoint) {return apiHelper.book(bookEndpoint);}

    public Observable<Response<Book>> rateBook(String auth, float rating, String bookEndpoint) {
        return apiHelper.rateBook(auth, rating, bookEndpoint);
    }




    public Observable<Response<Genre>> requestGenre() {return apiHelper.genres();}





    public Observable<Response<Chapter>> requestChapters(String book_endpoint) {return apiHelper.chapters(book_endpoint);}

    public Observable<Response<Chapter>> requestChapter(String book_endpoint, String chapter_endpoint) {return apiHelper.chapter(book_endpoint, chapter_endpoint);}

    public Observable<Response<Chapter>> requestChapter(String auth, String book_endpoint, String chapter_endpoint) {
        return apiHelper.chapter(auth, book_endpoint, chapter_endpoint);
    }


    public Observable<Response<Comment>> getComments(String book_endpoint) {return apiHelper.getComments(book_endpoint);}

    public Observable<Response<Comment>> getReplies(Integer comment_id) {return apiHelper.getReplies(comment_id);}

    public Observable<Response<Comment>> addComment(String auth, String book_endpoint, MultipartBody.Part[] body) {
        return apiHelper.addComment(auth, book_endpoint, body);
    }


    public Observable<Response<Notify>> requestAllNotification(String auth) {return apiHelper.notifications(auth);}

    public Observable<Response<Notify>> requestSingleNotification(String auth, String notifyEndpoint) {return apiHelper.readNotification(auth, notifyEndpoint);}

    public Observable<Response<Notify>> deleteAllReadNotification(String auth) {return apiHelper.deleteAllReadNotification(auth);}



    /*===============================================*/

    public Observable<List<String>> downloadImages(List<String> imageURLs, String path) {
        return fileHelper.downloadImages(imageURLs, path);
    }

    public List<String> downloadTexts(List<String> texts, String path) {
        return fileHelper.downloadTexts(texts, path);
    }

    public LiveData<List<HistorySearch>> getHistorySearch() {
        return dbHelper.getHistorySearch();
    }

    public void insertHistorySearch(HistorySearch historySearch) {
        dbHelper.insertHistorySearch(historySearch);
    }

    public void deleteHistorySearch(HistorySearch historySearch) {
        dbHelper.deleteHistorySearch(historySearch);
    }

    public List<Book> getAllBookDownloaded() {return dbHelper.getAllBookDownloaded();}

    public Book getDetailDownloadBook(String endpoint) {
        return dbHelper.getDetailDownloadBook(endpoint);
    }

    public void insertDownloadBook(DownloadBook downloadBook) {
        dbHelper.insertDownloadBook(downloadBook);
    }

    public void updateDownloadBook(DownloadBook downloadBook) {
        dbHelper.updateDownloadBook(downloadBook);
    }

    public void deleteDownloadBook(String endpoint) {
        dbHelper.deleteDownloadBook(endpoint);
        fileHelper.deleteDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + AppConstants.DOWNLOAD_PATH + endpoint));
    }

    public LiveData<List<DownloadChapter>> getAllChapterDownloaded(String book_endpoint) {
        return dbHelper.getAllChapterDownloaded(book_endpoint);
    }

    public List<Chapter> getChapterOfDownloadedBook(String book_endpoint) {
        return dbHelper.getChapterOfDownloadedBook(book_endpoint);
    }

    public Chapter getDetailDownloadChapter(String chapter_endpoint, String book_endpoint) {
        return dbHelper.getDetailDownloadChapter(chapter_endpoint, book_endpoint);
    }

    public void insertDownloadChapter(DownloadChapter downloadChapter) {
        dbHelper.insertDownloadChapter(downloadChapter);
    }

    public void updateDownloadChapter(DownloadChapter downloadChapter) {
        dbHelper.updateDownloadChapter(downloadChapter);
    }

    public void deleteDownloadChapter(String chapter_endpoint, String book_endpoint) {
        dbHelper.deleteDownloadChapter(chapter_endpoint, book_endpoint);
        fileHelper.deleteDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS
                + AppConstants.DOWNLOAD_PATH + book_endpoint + File.separator + chapter_endpoint));
    }

}
