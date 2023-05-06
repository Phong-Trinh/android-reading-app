package com.example.oneread.data.network;


import com.example.oneread.data.network.model.*;
import com.google.gson.JsonObject;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApiHelper {

    private IServiceAPI iServiceAPI;

    @Inject
    public ApiHelper(IServiceAPI iServiceAPI) {
        this.iServiceAPI = iServiceAPI;
    }

    public Observable<Response<User>> getInfoUser(String username) {
        return iServiceAPI.userInfo(username);
    }

    public Observable<Response<User>> updateUserInfo(String auth, MultipartBody.Part[] body) {
        return iServiceAPI.updateUserInfo(auth, body);
    }

    public Observable<Response<User>> updatePassword(String auth, JsonObject password) {
        return iServiceAPI.updatePassword(auth, password);
    }

    public Observable<Response<User>> verifyEmail(String auth) {
        return iServiceAPI.verifyEmail(auth);
    }

    public Observable<Response<Book>> getListBookFollowing(String auth) {
        return iServiceAPI.listBookFollowing(auth);
    }

    public Observable<Response<HistoryRead>> getAllHistoryRead(String auth) {
        return iServiceAPI.listHistoryRead(auth);
    }

    public Observable<Response<HistoryRead>> getHistoryRead(String auth, String bookEndpoint) {
        return iServiceAPI.historyRead(auth, bookEndpoint);
    }

    public Observable<Response<LoginResponse>> login(User user) {
        return iServiceAPI.login(user);
    }

    public Observable<Response<String>> register(User user) {
        return iServiceAPI.register(user);
    }

    public Observable<Response<Book>> followBook(String auth, String book_endpoint) {return iServiceAPI.followBook(auth, book_endpoint);}

    public Observable<Response<Book>> unfollowBook(String auth, String book_endpoint) {return iServiceAPI.unfollowBook(auth, book_endpoint);}

    public Observable<Response<HistoryRead>> deleteAllHistoryRead(String auth) {return iServiceAPI.deleteAllHistoryRead(auth);}

    public Observable<Response<HistoryRead>> deleteSingleHistoryRead(String auth, String bookEndpoint) {return iServiceAPI.deleteSingleHistoryRead(auth, bookEndpoint);}




    public Observable<Response<Book>> searchBook(String filter) {
        return iServiceAPI.books(filter);
    }

    public Observable<Response<Book>> getSuggestBooks(String auth) {
        return iServiceAPI.suggestBooks(auth);
    }

    public Observable<Response<Book>> getTrending() {return iServiceAPI.trending();}

    public Observable<Response<Book>> topSearch() {
        return iServiceAPI.getTopSearch();
    }

    public Observable<Response<Book>> relateBooks(String bookEndpoint) {return iServiceAPI.relateBooks(bookEndpoint);}

    public Observable<Response<Book>> book(String book_endpoint) {return iServiceAPI.detailBook(book_endpoint);}

    public Observable<Response<Book>> rateBook(String auth, float rating, String book_endpoint) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("rating", rating);
        return iServiceAPI.rateBook(auth, jsonObject, book_endpoint);
    }



    public Observable<Response<Genre>> genres() {return iServiceAPI.getGenres();}


    public Observable<Response<Chapter>> chapters(String book_endpoint) {return iServiceAPI.chapters(book_endpoint);}

    public Observable<Response<Chapter>> chapter(String book_endpoint, String chapter_endpoint) {return iServiceAPI.detailChapter(book_endpoint, chapter_endpoint);}

    public Observable<Response<Chapter>> chapter(String auth, String book_endpoint, String chapter_endpoint) {
        return iServiceAPI.detailChapter(auth, book_endpoint, chapter_endpoint);
    }



    public Observable<Response<Comment>> getComments(String book_endpoint) {return iServiceAPI.comments(book_endpoint);}

    public Observable<Response<Comment>> getReplies(Integer comment_id) {return iServiceAPI.detailComment(comment_id);}

    public Observable<Response<Comment>> addComment(String auth, String book_endpoint, MultipartBody.Part[] body) {
        return iServiceAPI.addComment(auth, book_endpoint, body);
    }




    public Observable<Response<Notify>> notifications(String auth) {return iServiceAPI.notifications(auth);}

    public Observable<Response<Notify>> readNotification(String auth, String notifyEndpoint) {return iServiceAPI.detailNotification(auth, notifyEndpoint);}

    public Observable<Response<Notify>> deleteAllReadNotification(String auth) {
        return iServiceAPI.deleteAllReadNotification(auth);
    }
}
