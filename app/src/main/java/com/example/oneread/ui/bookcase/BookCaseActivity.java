package com.example.oneread.ui.bookcase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.ui.base.BaseActivity;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NonConstantResourceId")
public class BookCaseActivity extends BaseActivity implements BookCaseContract.View, View.OnClickListener, RecyclerListener {

    @Inject
    BookCasePresenter<BookCaseContract.View> presenter;

    @BindView(R.id.btn_back)
    ImageView btnGoBack;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private List<Book> books = new ArrayList<>();
    private String keyListBook = "listBook";
    private String keyListBookState = "listBookState";
    private Parcelable listBookState;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_case);

        restoreState(savedInstanceState);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        setup();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private void saveState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(keyListBook, (ArrayList<? extends Parcelable>) books);
        outState.putParcelable(keyListBookState, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            books = savedInstanceState.getParcelableArrayList(keyListBook);
            listBookState = savedInstanceState.getParcelable(keyListBookState);
        }
    }

    @Override
    protected void setup() {
        setupView();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new BookCaseAdapter(this, books, this));
        recyclerView.getLayoutManager().onRestoreInstanceState(listBookState);
        if (books.size() == 0) {
            presenter.loadDownloadBooks();
        } else {
            setBooks(books);
        }
    }

    private void setupView() {
        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showEmptyView() {
        this.books.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void hideEmptyView() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setBooks(List<Book> books) {
        this.books.clear();
        this.books.addAll(books);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDeleteItemClick(int position) {
        presenter.deleteDownloadBook(books.get(position).getEndpoint());
    }
}