package com.example.oneread.ui.main;

import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
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
import java.util.Arrays;


public class MainPresenter<V extends MainContract.View> extends BasePresenter<V>
        implements MainContract.Presenter<V> {

    private static final String TAG = "MainPresenter";

    @Inject
    public MainPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onOptionAboutClick() {
        if (getDataManager().getCurrentUser() == null) return;
        getView().showAboutFragment();
        new Handler().postDelayed(() -> getView().closeNavigationDrawer(), 200);
    }

    @Override
    public void onOptionBookCaseClick() {
        getView().openBookCaseActivity();
        new Handler().postDelayed(() -> getView().closeNavigationDrawer(), 200);
    }

    @Override
    public void onOptionLoginClick() {
        getView().openLoginActivity();
    }

    @Override
    public void onOptionLogoutClick() {
        getDataManager().removeCurrentUser();
        getView().updateUserProfile(null);
    }

    @Override
    public void onUserLoggedIn() {
        getView().updateUserProfile(getDataManager().getCurrentUser());
        getFollowing();
        getSuggest();
        getRecent();
    }

    @Override
    public void onViewInitialized() {

    }

    @Override
    public void onNavMenuCreated() {
        getView().updateUserProfile(getDataManager().getCurrentUser());
        getView().setThemeMode(getDataManager().getNightMode());
    }

    @Override
    public void switchThemeMode() {
        if (getDataManager().getNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            getDataManager().setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDataManager().setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        getView().setThemeMode(getDataManager().getNightMode());

    }

    @Override
    public void getTrending() {
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestTrending()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setTrending(response.getData());
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
        }
    }

    @Override
    public void getSuggest() {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().setSuggest(null);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestSuggestBooks(user.getAuthorizeToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setSuggest(response.getData());
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
    public void getFollowing() {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().setFollowing(null);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestListBookFollowing(user.getAuthorizeToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setFollowing(response.getData());
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
        }
    }

    @Override
    public void getRecent() {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().setRecent(null);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestAllHistoryRead(user.getAuthorizeToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setRecent(response.getData());
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
        }
    }
}
