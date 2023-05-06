package com.example.oneread.ui.detail.info;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.HistoryRead;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BasePresenter;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import javax.inject.Inject;
import java.util.List;

public class AboutPresenter <V extends AboutContract.View> extends BasePresenter<V>
        implements AboutContract.Presenter<V> {

    private static final String TAG = "AboutPresenter";

    @Inject
    public AboutPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }


    @Override
    public void getRelateBooks(String bookEndpoint) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().requestRelateBooks(bookEndpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setRelateBooks(response.getData());
                            getView().hideLoading();
                        }, err -> {
                            try {
                                getView().hideLoading();
                                if (err instanceof HttpException) {
                                    HttpException response = (HttpException) err;
                                    String message = (JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message")).getAsString();
                                    getView().onError(message);
                                } else {
                                    Log.e(TAG, err.getMessage());
                                    err.printStackTrace();
                                    getView().onError(err.getMessage());
                                }
                            } catch (Exception e) {
                                getView().hideLoading();
                                System.out.println(e.getMessage());
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                        }));
            } catch (Exception e) {
                getView().hideLoading();
                System.out.println(e.getMessage());
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        } else {
//            getView().showMessage(R.string.network_required);
        }
    }

    @Override
    public void getHistoryRead(String bookEndpoint) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestHistoryRead(user.getAuthorizeToken(), bookEndpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            List<HistoryRead> historyReads = response.getData();
                            getView().setHistoryRead(historyReads.size() > 0 ? historyReads.get(0) : null);
                        }, err -> {
                            try {
                                if (err instanceof HttpException) {
                                    HttpException response = (HttpException) err;
                                    String message = (JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message")).getAsString();
                                    getView().onError(message);
                                } else {
                                    Log.e(TAG, err.getMessage());
                                    err.printStackTrace();
                                    getView().onError(err.getMessage());
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                        }));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        } else {
//            getView().showMessage(R.string.network_required);
        }

    }

    @Override
    public void followBook(String bookEndpoint) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().showMessage(R.string.login_required);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().followBook(getDataManager().getCurrentUser().getAuthorizeToken(), bookEndpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().showMessage(response.getMessage());
                            getView().setButtonFollow(true);

                        }, err -> {
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
            }
        } else {
            getView().showMessage(R.string.network_required);
        }
    }

    @Override
    public void unfollowBook(String bookEndpoint) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().showMessage(R.string.login_required);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().unfollowBook(getDataManager().getCurrentUser().getAuthorizeToken(), bookEndpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().showMessage(response.getMessage());
                            getView().setButtonFollow(false);

                        }, err -> {
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
            }
        } else {
            getView().showMessage(R.string.network_required);
        }
    }

    @Override
    public void checkIfUserFollowed(String bookEndpoint) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().setButtonFollow(false);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getCompositeDisposable().add(getDataManager().requestListBookFollowing(getDataManager().getCurrentUser().getAuthorizeToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            boolean isFollowed = false;
                            List<Book> books = response.getData();
                            for (Book book : books) {
                                if (book.getEndpoint().equals(bookEndpoint)) {
                                    isFollowed = true;
                                    break;
                                }
                            }
                            getView().setButtonFollow(isFollowed);

                        }, err -> {
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
            }
        } else {
//            getView().showMessage(R.string.network_required);
        }
    }
}
