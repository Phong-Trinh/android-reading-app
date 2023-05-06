package com.example.oneread.ui.main.notify;

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

public class NotificationPresenter <V extends NotificationContract.View> extends BasePresenter<V> implements NotificationContract.Presenter<V> {

    private static final String TAG = "NotificationPresenter";

    @Inject
    public NotificationPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getAllNotifications() {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestAllNotification(user.getAuthorizeToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setNotifications(response.getData());
                        }, err -> {
                            try {
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
                                System.out.println(e.getMessage());
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                        }));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteReadNotification() {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().deleteAllReadNotification(user.getAuthorizeToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().removeReadNotification(response.getData());
                        }, err -> {
                            try {
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
                                System.out.println(e.getMessage());
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                        }));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readNotification(String endpoint) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().showMessage(R.string.login_required);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestSingleNotification(user.getAuthorizeToken(), endpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().updateNotification(response.getData().get(0));
                        }, err -> {
                            try {
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
                                System.out.println(e.getMessage());
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                        }));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
