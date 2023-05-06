package com.example.oneread.ui.detail.info;

import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.HistoryRead;
import com.example.oneread.ui.base.BaseContract;

import java.util.List;

public interface AboutContract {
    interface View extends BaseContract.View {

        void openRateDialog();

        void setBook(Book book);

        void setRelateBooks(List<Book> relateBooks);

        void setHistoryRead(HistoryRead historyRead);

        void setButtonFollow(boolean isFollowed);

    }

    interface Presenter <V extends AboutContract.View> extends BaseContract.Presenter<V> {

        void getRelateBooks(String bookEndpoint);

        void getHistoryRead(String bookEndpoint);

        void followBook(String bookEndpoint);

        void unfollowBook(String bookEndpoint);

        void checkIfUserFollowed(String bookEndpoint);

    }
}
