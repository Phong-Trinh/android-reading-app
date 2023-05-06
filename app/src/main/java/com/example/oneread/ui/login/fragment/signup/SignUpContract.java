package com.example.oneread.ui.login.fragment.signup;

import com.example.oneread.ui.base.BaseContract;

public interface SignUpContract {
    interface View extends BaseContract.View {

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void onRegisterClick(String username, String password, String email);

        void onSignInClick();
    }
}
