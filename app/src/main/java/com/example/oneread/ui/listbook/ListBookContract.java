package com.example.oneread.ui.listbook;

import androidx.lifecycle.LiveData;
import com.example.oneread.data.db.HistorySearch;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Genre;
import com.example.oneread.ui.base.BaseContract;
import com.google.gson.JsonObject;

import java.util.List;

public interface ListBookContract {
    interface View extends BaseContract.View {

        void setBooks(List<Book> books);

        void openFragmentSearch();

        void closeFragmentSearch();

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void searchBook(JsonObject filter);

    }
}
