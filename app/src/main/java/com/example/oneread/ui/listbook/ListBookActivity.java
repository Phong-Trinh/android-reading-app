package com.example.oneread.ui.listbook;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.db.HistorySearch;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Genre;
import com.example.oneread.ui.base.BaseActivity;
import com.example.oneread.ui.listbook.search.SearchFragment;
import com.example.oneread.utils.NetworkUtils;
import com.google.gson.JsonObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("NonConstantResourceId")
public class ListBookActivity extends BaseActivity implements ListBookContract.View, View.OnClickListener {

    @Inject
    ListBookPresenter<ListBookContract.View> presenter;


    @BindView(R.id.btn_go_back)
    ImageView btnGoBack;
    @BindView(R.id.search)
    CardView cardSearch;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    private List<Book> books = new ArrayList<>();
    private SearchFragment searchFragment;
    private JsonObject filter;
    private final String LIST_BOOK = "LIST_BOOK";
    private final String LIST_BOOK_STATE = "LIST_BOOK_STATE";
    private Parcelable listState;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);

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
        outState.putParcelableArrayList(LIST_BOOK, (ArrayList<? extends Parcelable>) books);
        outState.putParcelable(LIST_BOOK_STATE, Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            books = savedInstanceState.getParcelableArrayList(LIST_BOOK);
            listState = savedInstanceState.getParcelable(LIST_BOOK_STATE);
        }
    }

    @Override
    protected void setup() {
        setupView();

        filter = new JsonObject();
        searchFragment = (SearchFragment)  getSupportFragmentManager().findFragmentByTag("SearchFragment");
        closeFragmentSearch();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new ListBookAdapter(this, books));
        if (books.size() == 0) {
            presenter.searchBook(filter);
        } else {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void setupView() {
        btnGoBack.setOnClickListener(this);
        cardSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                openFragmentSearch();
                break;
            case R.id.btn_go_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void showEmptyView() {

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
    public void openFragmentSearch() {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        searchFragment = (SearchFragment)  getSupportFragmentManager().findFragmentByTag("SearchFragment");
//        if (searchFragment == null) {
//            searchFragment = new SearchFragment();
//            fragmentTransaction.add(R.id.frm_search, searchFragment, "SearchFragment");
//            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            mainLayout.setVisibility(View.GONE);
//        } else {
//            fragmentTransaction.remove(searchFragment);
//            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//            mainLayout.setVisibility(View.VISIBLE);
//        }
//        fragmentTransaction.commit();

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_attach, R.anim.fragment_detach)
                .show(searchFragment).commit();
        mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void closeFragmentSearch() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_attach, R.anim.fragment_detach)
                .hide(searchFragment).commit();
        mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mainLayout.getVisibility() == View.GONE) closeFragmentSearch();
    }
}