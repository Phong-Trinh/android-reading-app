package com.example.oneread.ui.detail;

import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseContract;

import java.util.List;

public interface DetailContract {
    interface View extends BaseContract.View {

        void setBook(Book book);

        void setChapters(List<Chapter> chapters);
    }

    interface Presenter <V extends View> extends BaseContract.Presenter <V> {

        void getOnlineBook(String bookEndpoint);

        void getOfflineBook(String bookEndpoint);

        void getChaptersOnline(String bookEndpoint);

        void getChaptersOffline(String bookEndpoint);

    }
}
