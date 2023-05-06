package com.example.oneread.ui.detail;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.ui.base.BasePresenter;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;
import java.util.List;

public class DetailPresenter <V extends DetailContract.View> extends BasePresenter<V>
        implements DetailContract.Presenter<V> {

    private static final String TAG = "DetailPresenter";

    @Inject
    public DetailPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getOnlineBook(String book_endpoint) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().requestBook(book_endpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setBook(response.getData().get(0));
                            getView().hideLoading();
                        }, err -> {
                            getView().hideLoading();
                            if (err instanceof HttpException) {
                                HttpException response = (HttpException) err;
                                String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                                getView().onError(message);
                            } else {
                                Log.e(TAG, err.getMessage());
                                err.printStackTrace();
                                getView().onError(err.getMessage());
                            }
                        }));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                getView().hideLoading();
            }
        } else {
            getView().showMessage(R.string.network_required);
        }
    }

    @Override
    public void getOfflineBook(String book_endpoint) {
        getView().showLoading();
        getView().setBook(getDataManager().getDetailDownloadBook(book_endpoint));
        getView().hideLoading();
    }

    @Override
    public void getChaptersOnline(String bookEndpoint) {
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestChapters(bookEndpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setChapters(response.getData());
                        }, err -> {
                            if (err instanceof HttpException) {
                                HttpException response = (HttpException) err;
                                String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                                getView().onError(message);
                            } else {
                                Log.e(TAG, err.getMessage());
                                err.printStackTrace();
                                getView().onError(err.getMessage());
                            }
                        }));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        } else {
            getView().showMessage(R.string.network_required);
        }
    }

    @Override
    public void getChaptersOffline(String bookEndpoint) {
        getView().setChapters(getDataManager().getChapterOfDownloadedBook(bookEndpoint));
    }
}
