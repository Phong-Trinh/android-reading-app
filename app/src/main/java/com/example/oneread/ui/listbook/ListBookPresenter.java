package com.example.oneread.ui.listbook;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.ui.base.BasePresenter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;

public class ListBookPresenter <V extends ListBookContract.View> extends BasePresenter<V>
        implements ListBookContract.Presenter<V>{

    private static final String TAG = "ListBookPresenter";

    @Inject
    public ListBookPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
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
}
