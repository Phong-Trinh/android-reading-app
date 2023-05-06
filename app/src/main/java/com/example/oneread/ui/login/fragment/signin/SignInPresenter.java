package com.example.oneread.ui.login.fragment.signin;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BasePresenter;
import com.example.oneread.ui.login.LoginActivity;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;
import java.util.Objects;

public class SignInPresenter <V extends SignInContract.View> extends BasePresenter<V> implements SignInContract.Presenter<V> {

    private static final String TAG = "SignInPresenter";

    @Inject
    public SignInPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onLoginClick(String username, String password) {
        if (username == null || username.isEmpty()) {
            getView().onError(R.string.empty_username);
            return;
        }
        if (password == null || password.isEmpty()) {
            getView().onError(R.string.empty_password);
            return;
        }
        getView().showLoading();
        try {
            getCompositeDisposable().add(getDataManager().requestLogin(new User(username, password))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        getDataManager().saveUser(response.getData().get(0).getUser(), response.getData().get(0).getAccessToken());
                        getView().showMessage(response.getMessage());
                        ((LoginActivity) ((SignInFragment) getView()).getBaseActivity()).onLoginSuccess();
                        getView().hideLoading();
                    }, err -> {
                        if (err instanceof HttpException) {
                            HttpException response = (HttpException) err;
                            String message = String.valueOf(JsonParser.parseString(Objects.requireNonNull(Objects.requireNonNull(response.response()).errorBody()).string()).getAsJsonObject().get("message"));
                            getView().onError(message);
                        } else {
                            Log.e(TAG, err.getMessage());
                            err.printStackTrace();
                            getView().onError(err.getMessage());
                        }
                        getView().hideLoading();
                    }));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            getView().hideLoading();
        }
    }

    @Override
    public void onSignUpClick() {
        ((LoginActivity) ((SignInFragment) getView()).getBaseActivity()).openSignUpPage();
    }
}
