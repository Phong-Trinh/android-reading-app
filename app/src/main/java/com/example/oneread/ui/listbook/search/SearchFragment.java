package com.example.oneread.ui.listbook.search;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.db.HistorySearch;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Genre;
import com.example.oneread.ui.base.BaseFragment;
import com.example.oneread.ui.base.RoundBookAdapter;
import com.example.oneread.ui.listbook.ListBookActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;

import javax.inject.Inject;
import java.util.*;

@SuppressLint("NonConstantResourceId")
public class SearchFragment extends BaseFragment implements SearchContract.View, View.OnClickListener {

    private static final String TAG = "SearchFragment";

    @Inject
    SearchPresenter<SearchContract.View> presenter;

    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_esc)
    ImageView btnEsc;
    @BindView(R.id.history_layout)
    FlowLayout historyLayout;
    @BindView(R.id.genre_layout)
    FlowLayout genreLayout;
    @BindView(R.id.recycler_top_search)
    RecyclerView recyclerViewTopSearch;


    private List<Book> topSearch;
    private List<Genre> genres;
    private List<Boolean> selectedGenres;
    private List<TextView> genreItems;



    public SearchFragment() {}

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(this);
        return view;
    }

    @Override
    protected void setup(View view) {
        btnEsc.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        topSearch = new ArrayList<>();
        genres = new ArrayList<>();
        selectedGenres = new ArrayList<>();
        genreItems = new ArrayList<>();


        recyclerViewTopSearch.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewTopSearch.setAdapter(new RoundBookAdapter(getContext(), topSearch));
        presenter.getTopSearch();
        presenter.getGenres();
        presenter.loadHistorySearch();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTopSearch(List<Book> books) {
        topSearch.clear();
        topSearch.addAll(books);
        recyclerViewTopSearch.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setHistorySearch(LiveData<List<HistorySearch>> historySearch) {
        historySearch.observe(this, historySearches -> {
            if(historySearches.size() > 10){
                presenter.deleteHistorySearch(historySearches.get(0));
            }
            historyLayout.removeAllViews();
            for(int i=0; i<historySearches.size(); i++) {
                TextView textView = new TextView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                textView.setId(i);
                textView.setLayoutParams(params);
                textView.setBackground(getResources().getDrawable(R.drawable.text_item));
                textView.getBackground().setAlpha(200);
                textView.setPadding(20,10,20,10);
                textView.setText(historySearches.get(i).search);
                textView.setTextColor(Color.WHITE);
                historyLayout.addView(textView, 0);

                textView.setOnClickListener(v -> {
                    edtSearch.setText(textView.getText());
                });
            }
        });
    }

    @Override
    public void setGenres(List<Genre> genres) {
        this.genres.clear();
        this.genres.addAll(genres);
        genreLayout.removeAllViews();
        for(int i=0; i<genres.size(); i++) {
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            textView.setId(i);
            textView.setLayoutParams(params);
            textView.setBackground(getResources().getDrawable(R.drawable.text_item));
            textView.getBackground().setAlpha(200);
            textView.setPadding(20,10,20,10);
            textView.setText(genres.get(i).getTitle());
            textView.setTextColor(Color.WHITE);
            genreLayout.addView(textView, 0);

            selectedGenres.add(false);

            textView.setOnClickListener(v -> {
                if (selectedGenres.get(textView.getId())) {
                    textView.setBackground(getResources().getDrawable(R.drawable.text_item));
                    selectedGenres.set(textView.getId(), false);
                } else {
                    textView.setBackground(getResources().getDrawable(R.drawable.highlight_text_item));
                    selectedGenres.set(textView.getId(), true);
                }
            });
        }
    }

    @Override
    public void setBooks(List<Book> books) {
        ((ListBookActivity) getBaseActivity()).setBooks(books);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void hideErrorView() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_esc) {
            ((ListBookActivity) getBaseActivity()).closeFragmentSearch();
        } else if (view.getId() == R.id.btn_search) {
            JsonObject filter = new JsonObject();
            filter.addProperty("title", edtSearch.getText().toString().trim());
            presenter.insertHistorySearch(new HistorySearch(edtSearch.getText().toString().trim()));
            edtSearch.setText("");

            List<String> genresEndpoint = new ArrayList<>();
            for (int i=0; i<selectedGenres.size(); i++) {
                if(selectedGenres.get(i)) {
                    genresEndpoint.add(genres.get(i).getEndpoint());
                    ((TextView) Objects.requireNonNull(getView()).findViewById(i)).setBackground(getResources().getDrawable(R.drawable.text_item));
                    selectedGenres.set(i, false);
                }
            }
            filter.add("genres", new Gson().toJsonTree(genresEndpoint.toArray(new String[0])));

            presenter.searchBook(filter);
            ((ListBookActivity) getBaseActivity()).closeFragmentSearch();
        }
    }


}
