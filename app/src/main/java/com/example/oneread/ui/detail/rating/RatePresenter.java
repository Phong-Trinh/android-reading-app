package com.example.oneread.ui.detail.rating;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BasePresenter;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;

public class RatePresenter <V extends RateContract.View> extends BasePresenter<V> implements RateContract.Presenter<V> {

    private static final String TAG = "RatePresenter";

    @Inject
    public RatePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onSubmit(float rating, String bookEndpoint) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().showMessage(R.string.login_required);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().rateBook(user.getAuthorizeToken(), rating, bookEndpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setRating(response.getData().get(0).getRating());
                            getView().hideLoading();
                        }, err -> {
                            try {
                                getView().hideLoading();
                                if (err instanceof HttpException) {
                                    HttpException response = (HttpException) err;
                                    String message = (JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message")).getAsString();
                                    getView().onError(message);
                                } else {
                                    Log.e(TAG, err.getMessage());
                                    err.printStackTrace();
                                    getView().onError(err.getMessage());
                                }
                            } catch (Exception e) {
                                getView().hideLoading();
                                System.out.println(e.getMessage());
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                        }));
            } catch (Exception e) {
                getView().hideLoading();
                System.out.println(e.getMessage());
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        } else {
            getView().showMessage(R.string.network_required);
        }
    }
}
