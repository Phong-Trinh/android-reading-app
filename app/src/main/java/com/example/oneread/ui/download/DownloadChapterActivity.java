package com.example.oneread.ui.download;

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

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DownloadChapterActivity extends BaseActivity implements DownloadChapterContract.View,
        View.OnClickListener, DownloadChapterAdapter.Callback {

    private static final String TAG = "DownloadChapterActivity";

    @Inject
    DownloadChapterPresenter<DownloadChapterContract.View> presenter;

    @BindView(R.id.btn_check_all)
    ImageView btnCheckAll;
    @BindView(R.id.btn_download)
    ImageView btnDownload;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private DownloadChapterAdapter adapter;
    private Book book;
    private List<Boolean> listChapterDownloaded;
    private List<Boolean> listChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_chapter);

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
        listChapterDownloaded = new ArrayList<>(Arrays.asList(new Boolean[book.getChapters().size()]));
        Collections.fill(listChapterDownloaded, Boolean.FALSE);
        listChecked = new ArrayList<>(Arrays.asList(new Boolean[book.getChapters().size()]));
        Collections.fill(listChecked, Boolean.FALSE);
        presenter.loadDownloadedChapter(book.getEndpoint());
        adapter = new DownloadChapterAdapter(this, book.getChapters(), listChapterDownloaded, listChecked);
        adapter.setCallback(this);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnCheckAll.setTag(R.drawable.ic_uncheck);

    }

    private void setupView() {
        btnCheckAll.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void setDownloadChapter(LiveData<List<DownloadChapter>> downloadChapter) {
        downloadChapter.observe(this, downloadChapters -> {
            btnCheckAll.setImageResource(R.drawable.ic_uncheck);
            btnCheckAll.setTag(R.drawable.ic_uncheck);
            for (int i=0; i<listChapterDownloaded.size(); i++) {
                listChapterDownloaded.set(i, false);
                listChecked.set(i, false);
                for (int j=0; j<downloadChapters.size(); j++) {
                    if (book.getChapters().get(i).getChapterEndpoint().equals(downloadChapters.get(j).chapter_endpoint)) {
                        listChapterDownloaded.set(i, true);
                        break;
                    }
                }
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
            case R.id.btn_download:
                if (isWriteExternalStorage()) downloadSelectedChapters();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 69) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadSelectedChapters();
            } else {
                showMessage("Write permission denied!");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void downloadSelectedChapters() {
        List<Chapter> listChapterSelected = new ArrayList<>();
        for(int i=0; i<listChecked.size(); i++) {
            if (listChecked.get(i)) listChapterSelected.add(book.getChapters().get(i));
        }
        if (listChapterSelected.size() > 0) {
            presenter.downloadBookInfo(book);
            if (book.getType().equals("Comic")) presenter.downloadChapterImage(listChapterSelected);
            else presenter.downloadChapterText(listChapterSelected);
        }
    }

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

        recyclerView.getAdapter().notifyDataSetChanged();
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