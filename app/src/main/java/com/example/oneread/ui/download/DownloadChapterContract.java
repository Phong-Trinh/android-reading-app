package com.example.oneread.ui.download;

import androidx.lifecycle.LiveData;
import com.example.oneread.data.db.DownloadChapter;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseContract;

import java.util.List;

public interface DownloadChapterContract {
    interface View extends BaseContract.View {
        void setDownloadChapter(LiveData<List<DownloadChapter>> downloadChapter);
    }

    interface Presenter <V extends View> extends BaseContract.Presenter <V> {

        void downloadChapterText(List<Chapter> chapters);

        void downloadChapterImage(List<Chapter> chapters);

        void downloadBookInfo(Book book);

        void loadDownloadedChapter(String bookEndpoint);

    }
}
