package com.example.oneread.ui.profile;

import android.util.Log;

import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BasePresenter;
import com.example.oneread.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.HttpException;

public class ProfilePresenter<V extends ProfileContract.View> extends BasePresenter<V>
        implements ProfileContract.Presenter<V>{

    private static final String TAG = "ProfilePresenter";

    @Inject
    public ProfilePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void updateUserProfile(MultipartBody.Part[] body) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().updateUserInfo(getDataManager().getCurrentUser().getAuthorizeToken(), body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            User user = response.getData().get(0);
                            getDataManager().saveUser(user, getDataManager().getAccessToken());
                            getView().loadUser(user);
                            getView().showMessage(response.getMessage());
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

    @Override
    public void updatePassword(String password) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                JsonObject jsonPassword = new JsonObject();
                jsonPassword.addProperty("password", password);
                getCompositeDisposable().add(getDataManager().updatePassword(getDataManager().getCurrentUser().getAuthorizeToken(), jsonPassword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().showMessage(response.getMessage());
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

    @Override
    public void verifyEmail() {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().verifyEmail(getDataManager().getCurrentUser().getAuthorizeToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getDataManager().getCurrentUser().setStatus(2);
                            getView().showMessage(response.getMessage());
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

    @Override
    public void getUser() {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().requestInfoUser(getDataManager().getCurrentUser().getUsername())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            User user = response.getData().get(0);
                            getDataManager().saveUser(user, getDataManager().getAccessToken());
                            getView().loadUser(user);
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
