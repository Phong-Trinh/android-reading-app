package com.example.oneread.ui.bookcase;

import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.ui.base.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;

import javax.inject.Inject;
import java.util.List;

public class BookCasePresenter<V extends BookCaseContract.View> extends BasePresenter<V>
        implements BookCaseContract.Presenter<V> {

    private static final String TAG = "BookCasePresenter";

    @Inject
    public BookCasePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadDownloadBooks() {
        getView().showLoading();
        List<Book> books = getDataManager().getAllBookDownloaded();
        if (books != null && books.size() > 0) {
            getView().setBooks(books);
        } else getView().showEmptyView();
        getView().hideLoading();
    }

    @Override
    public void deleteDownloadBook(String endpoint) {
        getView().showLoading();
        getDataManager().deleteDownloadBook(endpoint);
        List<Book> books = getDataManager().getAllBookDownloaded();
        if (books != null && books.size() > 0) {
            getView().setBooks(books);
        } else getView().showEmptyView();
        getView().hideLoading();
    }
}
