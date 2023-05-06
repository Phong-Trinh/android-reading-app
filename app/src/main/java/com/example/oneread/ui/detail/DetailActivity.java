package com.example.oneread.ui.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseActivity;
import com.example.oneread.ui.base.PagerAdapter;
import com.example.oneread.ui.delete.DeleteChapterActivity;
import com.example.oneread.ui.detail.chapter.ListChapterAdapter;
import com.example.oneread.ui.detail.chapter.ListChapterFragment;
import com.example.oneread.ui.detail.comment.CommentFragment;
import com.example.oneread.ui.detail.info.AboutFragment;
import com.example.oneread.ui.download.DownloadChapterActivity;
import com.example.oneread.ui.read.ChapterActivity;
import com.example.oneread.utils.MODE;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

import javax.inject.Inject;
import java.util.List;

@SuppressLint("NonConstantResourceId")
public class DetailActivity extends BaseActivity implements DetailContract.View, View.OnClickListener, ListChapterAdapter.Callback {

    private static final String TAG = "DetailActivity";

    @Inject
    DetailPresenter<DetailContract.View> presenter;


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.thumb)
    ImageView thumb;
    @BindView(R.id.theme)
    ImageView theme;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_download)
    ImageView btnDownload;
    @BindView(R.id.btn_delete)
    ImageView btnDelete;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tab_background)
    BlurView tabBackground;
    @BindView(R.id.content_background)
    BlurView contentBackground;
    @BindView(R.id.root)
    ViewGroup root;
    @BindView(R.id.frm_content)
    ViewPager2 viewpager;

    private Book book;
    private String bookEndpoint;
    private String KEY_BOOK = "KEY_BOOK";
    private MODE mode;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

    private void setupView() {
        btnBack.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void saveState(@NonNull Bundle outState) {
        if (book != null) outState.putParcelable(KEY_BOOK,  book);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (book == null) book = savedInstanceState.getParcelable(KEY_BOOK);
        }
    }

    @Override
    protected void setup() {
        setupView();

        pagerAdapter = new PagerAdapter(this);
        viewpager.setAdapter(pagerAdapter);

        root = findViewById(R.id.root);
        tabBackground.setupWith(root)
                .setFrameClearDrawable(getWindow().getDecorView().getBackground())
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(25f).setBlurAutoUpdate(true);
        contentBackground.setupWith(root)
                .setFrameClearDrawable(getWindow().getDecorView().getBackground())
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(25f).setBlurAutoUpdate(true);

        if (book == null) {
            mode = (MODE) getIntent().getSerializableExtra("mode");
            if (mode == MODE.ONLINE) {
                bookEndpoint = getIntent().getStringExtra("endpoint");
                presenter.getOnlineBook(bookEndpoint);
            } else {
                bookEndpoint = getIntent().getStringExtra("endpoint");
                presenter.getOfflineBook(bookEndpoint);
            }
        } else {
            setBook(book);
        }

        btnDownload.setVisibility(mode == MODE.ONLINE ? View.VISIBLE : View.GONE);
        btnDelete.setVisibility(mode == MODE.ONLINE ? View.GONE : View.VISIBLE);

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }

    @Override
    public void setBook(Book book) {
        this.book = book;
        title.setText(book.getTitle());
        if (mode == MODE.ONLINE) {
            Glide.with(this).load(book.getThumb()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(thumb);
            Glide.with(this).load(book.getTheme()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(theme);

            presenter.getChaptersOnline(book.getEndpoint());
        } else {
            Glide.with(this).load(book.getThumb()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(thumb);
            Glide.with(this).load(book.getTheme()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(theme);

            presenter.getChaptersOffline(book.getEndpoint());
        }
    }

    @Override
    public void setChapters(List<Chapter> chapters) {
        book.getChapters().addAll(chapters);

        pagerAdapter.addFragment(new AboutFragment(this.book));
        pagerAdapter.addFragment(new ListChapterFragment(this.book, mode, this));
        pagerAdapter.addFragment(new CommentFragment(this.book));
        pagerAdapter.notifyDataSetChanged();
        viewpager.setUserInputEnabled(false);
        new TabLayoutMediator(tabLayout, viewpager, ((tab, position) -> {
            if (position == 0) tab.setText("about");
            else if (position == 1) tab.setText("chapter");
            else if (position == 2) tab.setText("comment");
        })).attach();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_download:
                intent = new Intent(this, DownloadChapterActivity.class);
                intent.putExtra("book", book);
                startActivity(intent);
                break;
            case R.id.btn_delete:
                intent = new Intent(this, DeleteChapterActivity.class);
                intent.putExtra("book", book);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    public void onOfflineChapterClick(int position) {
        Intent intent = new Intent(this, ChapterActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("book", book);
        intent.putExtra("mode", MODE.OFFLINE);
        startActivity(intent);
    }

    @Override
    public void onOnlineChapterClick(int position) {
        Intent intent = new Intent(this, ChapterActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("book", book);
        intent.putExtra("mode", MODE.ONLINE);
        startActivity(intent);
    }

    public void openChapter(int position) {
        if (mode == MODE.ONLINE) {
            onOnlineChapterClick(position);
        } else onOfflineChapterClick(position);
    }
}