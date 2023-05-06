package com.example.oneread.ui.bookcase;

import com.example.oneread.data.network.model.Book;
import com.example.oneread.ui.base.BaseContract;

import java.util.List;

public interface BookCaseContract {
    interface View extends BaseContract.View {

        void setBooks(List<Book> books);

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void loadDownloadBooks();

        void deleteDownloadBook(String endpoint);
    }
}
