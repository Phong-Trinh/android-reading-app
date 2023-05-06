package com.example.oneread.ui.delete;


import androidx.lifecycle.LiveData;
import com.example.oneread.data.db.DownloadChapter;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseContract;

import java.util.List;

public interface DeleteChapterContract {
    interface View extends BaseContract.View {
        void setDownloadChapter(LiveData<List<DownloadChapter>> downloadChapter);
    }

    interface Presenter <V extends View> extends BaseContract.Presenter <V> {

        void deleteChapter(List<Chapter> chapters);

        void deleteBook(Book book);

        void loadDownloadedChapter(String bookEndpoint);

    }
}
