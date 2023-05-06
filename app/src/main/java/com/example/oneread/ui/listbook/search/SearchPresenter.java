package com.example.oneread.ui.listbook.search;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.db.HistorySearch;
import com.example.oneread.ui.base.BasePresenter;
import com.example.oneread.ui.listbook.search.SearchContract;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;

public class SearchPresenter <V extends SearchContract.View> extends BasePresenter<V>
        implements SearchContract.Presenter<V> {

    private static final String TAG = "SearchPresenter";

    @Inject
    public SearchPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void searchBook(JsonObject filter) {
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().searchBook(String.valueOf(filter))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setBooks(response.getData());
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
    public void getTopSearch() {
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestTopSearch()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setTopSearch(response.getData());
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
    public void loadHistorySearch() {
        getView().setHistorySearch(getDataManager().getHistorySearch());
    }

    @Override
    public void getGenres() {
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestGenre()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setGenres(response.getData());
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
    public void insertHistorySearch(HistorySearch historySearch) {
        if (!historySearch.search.equals("")) getDataManager().insertHistorySearch(historySearch);
    }

    @Override
    public void deleteHistorySearch(HistorySearch historySearch) {
        if (!historySearch.search.equals("")) getDataManager().deleteHistorySearch(historySearch);
    }
}
