package com.example.oneread.ui.detail.info;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.data.network.model.HistoryRead;
import com.example.oneread.ui.base.BaseFragment;
import com.example.oneread.ui.base.RectBookAdapter;
import com.example.oneread.ui.base.RoundBookAdapter;
import com.example.oneread.ui.detail.DetailActivity;
import com.example.oneread.ui.detail.rating.RateDialog;
import com.nex3z.flowlayout.FlowLayout;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("NonConstantResourceId")
public class AboutFragment extends BaseFragment implements AboutContract.View, View.OnClickListener, RateDialog.Callback  {

    private static final String TAG = "AboutFragment";

    @Inject
    AboutPresenter<AboutContract.View> presenter;

    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.rate_count)
    TextView rateCount;
    @BindView(R.id.following)
    TextView following;
    @BindView(R.id.genre_layout)
    FlowLayout genreLayout;
    @BindView(R.id.view)
    TextView view;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.btn_follow)
    ImageView btnFollow;
    @BindView(R.id.btn_rating)
    ImageView btnRating;
    @BindView(R.id.btn_read)
    AppCompatButton btnRead;
    @BindView(R.id.btn_continue_read)
    AppCompatButton btnContinueRead;
    @BindView(R.id.list_relate_book)
    RecyclerView listRelateBook;
    @BindView(R.id.recent_chapter)
    TextView recentChapter;

    private Book book;
    private HistoryRead historyRead;
    private List<Book> relateBooks;

    public AboutFragment (Book book) {
        this.book = book;
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(this);
        return view;
    }

    @Override
    protected void setup(View v) {
        setListener();
        setBook(book);

        relateBooks = new ArrayList<>();
        listRelateBook.setNestedScrollingEnabled(false);
        listRelateBook.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        listRelateBook.setNestedScrollingEnabled(false);
        listRelateBook.setAdapter(new RectBookAdapter(getContext(), relateBooks));
    }

    private void setListener() {
        btnFollow.setOnClickListener(this);
        btnRating.setOnClickListener(this);
        btnContinueRead.setOnClickListener(this);
        btnRead.setOnClickListener(this);
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
    public void openRateDialog() {
        RateDialog rateDialog = RateDialog.newInstance(book.getEndpoint());
        rateDialog.setCallBack(this);
        rateDialog.show(getParentFragmentManager());
    }

    @Override
    public void setBook(Book book) {
        this.book = book;
        rating.setText(String.valueOf(book.getRating()));
        rateCount.setText(new StringBuilder().append(book.getRateCount()).append(" lượt đánh giá"));
        following.setText(book.getFollow());
        view.setText(book.getView());
        desc.setText(book.getDescription());
        for(int i=0; i<book.getGenres().size(); i++){
            TextView genre = new TextView(getBaseActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            genre.setLayoutParams(params);
            genre.setBackground(getResources().getDrawable(R.drawable.text_item));
            genre.getBackground().setAlpha(200);
            genre.setPadding(20,10,20,10);
            genre.setText(book.getGenres().get(i).getTitle());
            genre.setTextColor(Color.WHITE);
            genreLayout.addView(genre);
        }
        presenter.getRelateBooks(book.getEndpoint());
        presenter.getHistoryRead(book.getEndpoint());
        presenter.checkIfUserFollowed(book.getEndpoint());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setRelateBooks(List<Book> relateBooks) {
        this.relateBooks.clear();
        this.relateBooks.addAll(relateBooks);
        Objects.requireNonNull(listRelateBook.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void setHistoryRead(HistoryRead historyRead) {
        this.historyRead = historyRead;
        if (historyRead != null) recentChapter.setText(historyRead.getChapter().getTitle());
    }

    @Override
    public void setButtonFollow(boolean isFollowed) {
        if (isFollowed) {
            btnFollow.setImageResource(R.drawable.ic_followed);
            btnFollow.setTag(R.drawable.ic_followed);
        } else {
            btnFollow.setImageResource(R.drawable.ic_follow);
            btnFollow.setTag(R.drawable.ic_follow);
        }
    }

    @Override
    public void onRating(float rating) {
        this.rating.setText(String.valueOf(rating));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_follow:
                if (((int) btnFollow.getTag()) == R.drawable.ic_follow) {
                    presenter.followBook(book.getEndpoint());
                } else {
                    presenter.unfollowBook(book.getEndpoint());
                }
                break;
            case R.id.btn_rating:
                openRateDialog();
                break;
            case R.id.btn_read:
                ((DetailActivity) getBaseActivity()).openChapter(0);
                break;
            case R.id.btn_continue_read:
                if (historyRead != null) {
                    for(int i=0; i<book.getChapters().size(); i++) {
                        if (book.getChapters().get(i).getChapterEndpoint()
                                .equals(historyRead.getChapter().getChapterEndpoint())) {
                            ((DetailActivity) getBaseActivity()).openChapter(i);
                            break;
                        }
                    }
                } else ((DetailActivity) getBaseActivity()).openChapter(0);
                break;
            default:
                break;
        }
    }
}
