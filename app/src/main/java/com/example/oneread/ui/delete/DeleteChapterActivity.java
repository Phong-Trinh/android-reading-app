package com.example.oneread.ui.delete;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.db.DownloadChapter;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseActivity;
import com.example.oneread.utils.AppConstants;

import javax.inject.Inject;
import java.util.*;

@SuppressLint("NonConstantResourceId")
public class DeleteChapterActivity extends BaseActivity implements DeleteChapterContract.View,
        View.OnClickListener, DeleteChapterAdapter.Callback {

    private static final String TAG = "DeleteChapterActivity";

    @Inject
    DeleteChapterPresenter<DeleteChapterContract.View> presenter;

    @BindView(R.id.btn_check_all)
    ImageView btnCheckAll;
    @BindView(R.id.btn_delete)
    ImageView btnDelete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private Book book;
    private List<Boolean> listChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_chapter);

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

        book = getIntent().getParcelableExtra("book");
        listChecked = new ArrayList<>(Arrays.asList(new Boolean[book.getChapters().size()]));
        Collections.fill(listChecked, Boolean.FALSE);
        presenter.loadDownloadedChapter(book.getEndpoint());
        DeleteChapterAdapter adapter = new DeleteChapterAdapter(this, book.getChapters(), listChecked);
        adapter.setCallback(this);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnCheckAll.setTag(R.drawable.ic_uncheck);

    }

    private void setupView() {
        btnCheckAll.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setDownloadChapter(LiveData<List<DownloadChapter>> downloadChapter) {
        downloadChapter.observe(this, downloadChapters -> {
            book.getChapters().clear();
            listChecked.clear();
            btnCheckAll.setImageResource(R.drawable.ic_uncheck);
            btnCheckAll.setTag(R.drawable.ic_uncheck);
            for (DownloadChapter value : downloadChapters) {
                Chapter chapter = new Chapter();
                chapter.setChapterEndpoint(value.chapter_endpoint);
                chapter.setBookEndpoint(value.book_endpoint);
                chapter.setTitle(value.title);
                chapter.setTime(value.time);
                chapter.setImages(value.images);
                book.getChapters().add(chapter);
                listChecked.add(false);
            }
            if (recyclerView.getAdapter() != null) recyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_check_all:
                setCheckedAll();
                break;
            case R.id.btn_delete:
                if (isWriteExternalStorage()) deleteSelectedChapters();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstants.REQUEST_WRITE_EXTERNAL_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                deleteSelectedChapters();
            } else {
                showMessage("Write permission denied!");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void deleteSelectedChapters() {
        List<Chapter> listChapterSelected = new ArrayList<>();
        for(int i=0; i<listChecked.size(); i++) {
            if (listChecked.get(i)) listChapterSelected.add(book.getChapters().get(i));
        }
        if (listChapterSelected.size() > 0) {
            presenter.deleteChapter(listChapterSelected);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setCheckedAll() {
        if(((int) btnCheckAll.getTag()) == R.drawable.ic_uncheck){
            btnCheckAll.setImageResource(R.drawable.ic_check);
            btnCheckAll.setTag(R.drawable.ic_check);
            for (int i=0; i<listChecked.size(); i++) {
                listChecked.set(i, true);
            }
        }else{
            btnCheckAll.setImageResource(R.drawable.ic_uncheck);
            btnCheckAll.setTag(R.drawable.ic_uncheck);
            for (int i=0; i<listChecked.size(); i++) {
                listChecked.set(i, false);
            }
        }

        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onChapterItemClick(int position, boolean isChecked) {
        listChecked.set(position, isChecked);
        if (!isChecked && ((int) btnCheckAll.getTag()) == R.drawable.ic_check) {
            btnCheckAll.setImageResource(R.drawable.ic_uncheck);
            btnCheckAll.setTag(R.drawable.ic_uncheck);
        }
    }
}