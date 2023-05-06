package com.example.oneread.ui.listbook.search;

import androidx.lifecycle.LiveData;
import com.example.oneread.data.db.HistorySearch;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Genre;
import com.example.oneread.ui.base.BaseContract;
import com.google.gson.JsonObject;

import java.util.List;

public interface SearchContract {
    interface View extends BaseContract.View {

        void setTopSearch(List<Book> books);

        void setHistorySearch(LiveData<List<HistorySearch>> historySearch);

        void setGenres(List<Genre> genres);

        void setBooks(List<Book> books);

    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void searchBook(JsonObject filter);

        void getTopSearch();

        void loadHistorySearch();

        void getGenres();

        void insertHistorySearch(HistorySearch historySearch);

        void deleteHistorySearch(HistorySearch historySearch);

    }
}
