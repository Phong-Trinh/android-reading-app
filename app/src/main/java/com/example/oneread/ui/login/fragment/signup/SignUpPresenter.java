package com.example.oneread.ui.login.fragment.signup;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BasePresenter;
import com.example.oneread.ui.login.LoginActivity;
import com.example.oneread.utils.CommonUtils;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;
import java.util.Objects;

public class SignUpPresenter <V extends SignUpContract.View> extends BasePresenter<V> implements SignUpContract.Presenter<V> {

    private static final String TAG = "SignUpPresenter";

    @Inject
    public SignUpPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onRegisterClick(String username, String password, String email) {
        if (username == null || username.isEmpty()) {
            getView().onError(R.string.empty_username);
            return;
        }
        if (password == null || password.isEmpty()) {
            getView().onError(R.string.empty_password);
            return;
        }
        if (email == null || email.isEmpty()) {
            getView().onError(R.string.empty_email);
            return;
        }
        if (!CommonUtils.isValidPassword(password)) {
            getView().onError(R.string.invalid_password);
            return;
        }
        if (!CommonUtils.isValidEmailAddress(email)) {
            getView().onError(R.string.invalid_email);
            return;
        }
        getView().showLoading();
        try {
            getCompositeDisposable().add(getDataManager().requestRegister(new User(username, password, email))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response-> {
                        getView().showMessage(response.getMessage());
                        ((LoginActivity) ((SignUpFragment) getView()).getBaseActivity()).openSignInPage();
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
    public void onSignInClick() {
        ((LoginActivity) ((SignUpFragment) getView()).getBaseActivity()).openSignInPage();
    }
}
