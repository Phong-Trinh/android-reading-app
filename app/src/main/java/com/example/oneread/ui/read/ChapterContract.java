package com.example.oneread.ui.read;

import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseContract;

public interface ChapterContract {
    interface View extends BaseContract.View {

        void setChapter(Chapter chapter);

    }
    interface Presenter <V extends View> extends BaseContract.Presenter <V>{

        void getChapterDetailOnline(Chapter chapter);

        void getChapterDetailOffline(Chapter chapter);

    }
}
