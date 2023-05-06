package com.example.oneread.ui.read;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BasePresenter;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;

public class ChapterPresenter <V extends ChapterContract.View> extends BasePresenter<V>
            implements ChapterContract.Presenter <V> {

    private static final String TAG = "ChapterPresenter";

    @Inject
    public ChapterPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getChapterDetailOnline(Chapter chapter) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            if (chapter != null && (chapter.getImages() == null || chapter.getImages().size() == 0)) {
                if (getView().isNetworkConnected()) {
                    try {
                        getView().showLoading();
                        getCompositeDisposable().add(getDataManager().requestChapter(chapter.getBookEndpoint(), chapter.getChapterEndpoint())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(response -> {
                                    getView().setChapter(response.getData().get(0));
                                    getView().hideLoading();
                                }, err -> {
                                    getView().hideLoading();
                                    if (err instanceof HttpException) {
                                        HttpException response = (HttpException) err;
                                        String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                                        getView().onError(message);
                                    } else {
                                        Log.e(TAG, err.getMessage());
                                        err.printStackTrace();
                                        getView().onError(err.getMessage());
                                    }
                                }));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                        getView().hideLoading();
                    }
                } else {
                    getView().showMessage(R.string.network_required);
                }
            } else {
                getView().setChapter(chapter);
            }
        } else {
            if (chapter != null && (chapter.getImages() == null || chapter.getImages().size() == 0)) {
                if (getView().isNetworkConnected()) {
                    try {
                        getView().showLoading();
                        getCompositeDisposable().add(getDataManager().requestChapter(user.getAuthorizeToken(), chapter.getBookEndpoint(), chapter.getChapterEndpoint())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(response -> {
                                    getView().setChapter(response.getData().get(0));
                                    getView().hideLoading();
                                }, err -> {
                                    getView().hideLoading();
                                    if (err instanceof HttpException) {
                                        HttpException response = (HttpException) err;
                                        String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                                        getView().onError(message);
                                    } else {
                                        Log.e(TAG, err.getMessage());
                                        err.printStackTrace();
                                        getView().onError(err.getMessage());
                                    }
                                }));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                        getView().hideLoading();
                    }
                } else {
                    getView().showMessage(R.string.network_required);
                }
            } else {
                getView().setChapter(chapter);
            }
        }
    }

    @Override
    public void getChapterDetailOffline(Chapter chapter) {
        getView().setChapter(getDataManager().getDetailDownloadChapter(chapter.getChapterEndpoint(), chapter.getBookEndpoint()));
    }
}
