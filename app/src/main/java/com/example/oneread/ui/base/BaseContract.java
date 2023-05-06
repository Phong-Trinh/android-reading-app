package com.example.oneread.ui.base;

import androidx.annotation.StringRes;

public interface BaseContract {

    interface View {

        void showLoading();

        void hideLoading();

        void showLoadingView();

        void hideLoadingView();

        void showErrorView();

        void hideErrorView();

        void showEmptyView();

        void hideEmptyView();

        void openLoginActivityOnTokenExpire();

        void onError(@StringRes int resId);

        void onError(String message);

        void showMessage(String message);

        void showMessage(@StringRes int resId);

        boolean isNetworkConnected();

        boolean isWriteExternalStorage();

        void hideKeyBoard();

    }

    interface DialogView extends View {

        void dismissDialog(String tag);

    }

    interface Presenter<V extends View> {
        void onAttach(V view);

        void onDetach();
    }
}
