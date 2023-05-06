package com.example.oneread.ui.detail.rating;

import com.example.oneread.ui.base.BaseContract;

public interface RateContract {

    interface View extends BaseContract.DialogView {

        void setRating(float rating);

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void onSubmit(float rating, String bookEndpoint);

    }

}
