package com.example.oneread.ui.profile;

import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BaseContract;

import okhttp3.MultipartBody;

public interface ProfileContract {
    interface View extends BaseContract.View {

        void loadUser(User user);

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void updateUserProfile(MultipartBody.Part[] body);

        void updatePassword(String password);

        void verifyEmail();

        void getUser();

    }
}
