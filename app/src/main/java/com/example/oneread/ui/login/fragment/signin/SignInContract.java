package com.example.oneread.ui.login.fragment.signin;

import com.example.oneread.ui.base.BaseContract;

public interface SignInContract {
    interface View extends BaseContract.View {

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void onLoginClick(String username, String password);

        void onSignUpClick();
    }
}
