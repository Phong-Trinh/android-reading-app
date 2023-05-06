package com.example.oneread.ui.download;

import android.annotation.SuppressLint;
import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.db.DownloadBook;
import com.example.oneread.data.db.DownloadChapter;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BasePresenter;
import com.example.oneread.utils.AppConstants;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DownloadChapterPresenter <V extends DownloadChapterContract.View> extends BasePresenter <V>
            implements DownloadChapterContract.Presenter <V> {

    private static final String TAG = "DownloadChapterPresenter";

    @Inject
    public DownloadChapterPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }


    @SuppressLint("LongLogTag")
    @Override
    public void downloadChapterImage(List<Chapter> chapters) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                for (Chapter chapter : chapters) {
                    getCompositeDisposable().add(getDataManager().requestChapter(chapter.getBookEndpoint(), chapter.getChapterEndpoint())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                chapter.setImages(response.getData().get(0).getImages());
                                getCompositeDisposable().add(getDataManager().downloadImages(chapter.getImages(),
                                        AppConstants.DOWNLOAD_PATH +
                                                chapter.getBookEndpoint() + File.separator +
                                                chapter.getChapterEndpoint() + File.separator)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(uris -> {
                                            chapter.setImages(uris);
                                            getDataManager().insertDownloadChapter(new DownloadChapter(chapter.getChapterEndpoint(),
                                                    chapter.getBookEndpoint(), chapter.getTitle(), chapter.getTime(), chapter.getImages()));
                                            getView().hideLoading();
                                        }, err -> {
                                            getView().hideLoading();
                                            Log.e(TAG, err.getMessage());
                                            err.printStackTrace();
                                            getView().onError(err.getMessage());
                                        }));
//                                chapter.setImages(response.getData().get(0).getImages());
//                                chapter.setImages(getDataManager().downloadImages(chapter.getImages(),
//                                        AppConstants.DOWNLOAD_PATH +
//                                                chapter.getBookEndpoint() + File.separator +
//                                                chapter.getChapterEndpoint() + File.separator));
//                                getDataManager().insertDownloadChapter(new DownloadChapter(chapter.getChapterEndpoint(),
//                                        chapter.getBookEndpoint(), chapter.getTitle(), chapter.getTime(), chapter.getImages()));
//                                getView().hideLoading();
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
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                System.out.println(e.getMessage());
                e.printStackTrace();
                getView().hideLoading();
            }
        } else {
            getView().showMessage(R.string.network_required);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void downloadChapterText(List<Chapter> chapters) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                for (Chapter chapter : chapters) {
                    getCompositeDisposable().add(getDataManager().requestChapter(chapter.getBookEndpoint(), chapter.getChapterEndpoint())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                chapter.setImages(response.getData().get(0).getImages());
                                chapter.setImages(getDataManager().downloadTexts(chapter.getImages(),
                                        AppConstants.DOWNLOAD_PATH +
                                                chapter.getBookEndpoint() + File.separator +
                                                chapter.getChapterEndpoint() + File.separator));
                                getDataManager().insertDownloadChapter(new DownloadChapter(chapter.getChapterEndpoint(),
                                        chapter.getBookEndpoint(), chapter.getTitle(), chapter.getTime(), chapter.getImages()));
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
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                System.out.println(e.getMessage());
                e.printStackTrace();
                getView().hideLoading();
            }
        } else {
            getView().showMessage(R.string.network_required);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void downloadBookInfo(Book book) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().downloadImages(Arrays.asList(book.getThumb(), book.getTheme()),
                        AppConstants.DOWNLOAD_PATH + book.getEndpoint() + File.separator + "info/")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            book.setThumb(response.get(0));
                            book.setTheme(response.get(1));
                            getDataManager().insertDownloadBook(new DownloadBook(book.getEndpoint(), book.toString()));
                            Book b = new Gson().fromJson(book.toString(), Book.class);
                            getView().hideLoading();
                        }, err -> {
                            getView().hideLoading();
                            Log.e(TAG, err.getMessage());
                            err.printStackTrace();
                            getView().onError(err.getMessage());
                        }));

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                System.out.println(e.getMessage());
                e.printStackTrace();
                getView().hideLoading();
            }
        } else {
            getView().showMessage(R.string.network_required);
        }
    }

    @Override
    public void loadDownloadedChapter(String bookEndpoint) {
        getView().setDownloadChapter(getDataManager().getAllChapterDownloaded(bookEndpoint));
    }
}
