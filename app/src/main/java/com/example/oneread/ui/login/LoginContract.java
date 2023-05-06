package com.example.oneread.ui.login;


import com.example.oneread.ui.base.BaseContract;

public interface LoginContract {
    interface View extends BaseContract.View {
        void openSignInPage();

        void openSignUpPage();

        void onLoginSuccess();
    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void onSignUpSuccess();

    }
}
