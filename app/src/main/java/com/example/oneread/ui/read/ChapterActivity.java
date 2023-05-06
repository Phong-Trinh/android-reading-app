package com.example.oneread.ui.read;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseActivity;
import com.example.oneread.ui.detail.chapter.ListChapterAdapter;
import com.example.oneread.ui.detail.chapter.ListChapterFragment;
import com.example.oneread.ui.download.DownloadChapterActivity;
import com.example.oneread.ui.read.fragment.comic.ChapterComicFragment;
import com.example.oneread.ui.read.fragment.novel.ChapterNovelFragment;
import com.example.oneread.utils.MODE;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

import javax.inject.Inject;

@SuppressLint("NonConstantResourceId")
public class ChapterActivity extends BaseActivity implements ChapterContract.View, View.OnClickListener,
        ListChapterAdapter.Callback {

    private static final String TAG = "ChapterActivity";

    @Inject
    ChapterPresenter<ChapterContract.View> presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.prev)
    ImageView btnPrev;
    @BindView(R.id.next)
    ImageView btnNext;
    @BindView(R.id.chapters)
    ImageView btnChapters;
    @BindView(R.id.comment)
    ImageView btnComment;
    @BindView(R.id.list_chapter_background)
    BlurView listChapterBackground;
    @BindView(R.id.root)
    ViewGroup root;
    @BindView(R.id.layout_list_chapter)
    RelativeLayout layoutListChapter;
    @BindView(R.id.layout_outside)
    LinearLayout layoutOutside;

    private int position;
    private Book book;
    private MODE mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

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

    @Override
    protected void setup() {
        setupView();

        root = findViewById(R.id.root);
        listChapterBackground.setupWith(root)
                .setFrameClearDrawable(getWindow().getDecorView().getBackground())
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(25f).setBlurAutoUpdate(true);

        position = getIntent().getIntExtra("position", 0);
        book = getIntent().getParcelableExtra("book");
        mode = (MODE) getIntent().getSerializableExtra("mode");
        toolbar.getBackground().setAlpha(200);

        getSupportFragmentManager().beginTransaction().add(R.id.frm_content, new ListChapterFragment(book, mode, this)).commit();
        hideListChapter();

        if (mode == MODE.ONLINE) {
            presenter.getChapterDetailOnline(book.getChapters().get(position));
        } else {
            presenter.getChapterDetailOffline(book.getChapters().get(position));
        }
    }

    private void setupView() {
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnChapters.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        layoutOutside.setOnClickListener(this);
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev:
                if (mode == MODE.ONLINE) {
                    presenter.getChapterDetailOnline(book.getChapters().get(++position));
                } else {
                    presenter.getChapterDetailOffline(book.getChapters().get(++position));
                }
                break;
            case R.id.next:
                if (mode == MODE.ONLINE) {
                    presenter.getChapterDetailOnline(book.getChapters().get(--position));
                } else {
                    presenter.getChapterDetailOffline(book.getChapters().get(--position));
                }
                break;
            case R.id.layout_outside:
                hideListChapter();
                break;
            case R.id.chapters:
                showListChapter();
                break;
            default:
                break;
        }
    }

    @Override
    public void setChapter(Chapter chapter) {
        hideListChapter();
        this.book.getChapters().set(position, chapter);
        btnNext.setVisibility(position != 0 ? View.VISIBLE : View.INVISIBLE);
        btnPrev.setVisibility(position != book.getChapters().size() - 1 ? View.VISIBLE : View.INVISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_chapter_content,
                        (book.getType().equals("Comic") ? ChapterComicFragment.newInstance(book.getChapters().get(position).getImages(), mode)
                                : ChapterNovelFragment.newInstance(book.getChapters().get(position).getImages(), mode)))
                .commit();
    }

    private void showListChapter() {
        layoutListChapter.setVisibility(View.VISIBLE);
    }

    private void hideListChapter() {
        layoutListChapter.setVisibility(View.GONE);
    }

    @Override
    public void onOfflineChapterClick(int position) {
        this.position = position;
        presenter.getChapterDetailOffline(book.getChapters().get(this.position));
    }

    @Override
    public void onOnlineChapterClick(int position) {
        this.position = position;
        presenter.getChapterDetailOnline(book.getChapters().get(this.position));
    }
}