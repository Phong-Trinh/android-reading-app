package com.example.oneread.ui.detail.chapter;

import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseContract;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseContract;

import java.util.List;

public interface ListChapterContract {
    interface View extends BaseContract.View {

        void setChapters(List<Chapter> chapters);

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void getChaptersOnline(String bookEndpoint);

        void getChaptersOffline(String bookEndpoint);

    }
}
