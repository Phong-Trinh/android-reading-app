package com.example.oneread.ui.delete;

import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;

import javax.inject.Inject;
import java.util.List;

public class DeleteChapterPresenter <V extends DeleteChapterContract.View> extends BasePresenter <V>
        implements DeleteChapterContract.Presenter <V> {

    private static final String TAG = "DeleteChapterPresenter";

    @Inject
    public DeleteChapterPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void deleteChapter(List<Chapter> chapters) {
        for (Chapter chapter : chapters) {
            getDataManager().deleteDownloadChapter(chapter.getChapterEndpoint(), chapter.getBookEndpoint());
        }
    }

    @Override
    public void deleteBook(Book book) {
        getDataManager().deleteDownloadBook(book.getEndpoint());
    }

    @Override
    public void loadDownloadedChapter(String bookEndpoint) {
        getView().setDownloadChapter(getDataManager().getAllChapterDownloaded(bookEndpoint));
    }
}
