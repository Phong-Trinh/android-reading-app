package com.example.oneread.ui.detail.comment;

import android.util.Log;
import com.example.oneread.R;
import com.example.oneread.data.DataManager;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BasePresenter;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.HttpException;

import javax.inject.Inject;

public class CommentPresenter<V extends CommentContract.View> extends BasePresenter<V>
        implements CommentContract.Presenter<V> {

    private static final String TAG = "CommentPresenter";

    @Inject
    public CommentPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadComments(String endpoint) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().getComments(endpoint)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setComments(response.getData());
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
            getView().showEmptyView();
        }
    }

    @Override
    public void loadReplies(int id, int adapterPosition) {
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().getReplies(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().setReplies(response.getData().get(0).getReplies(), adapterPosition);
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
            getView().showMessage(R.string.network_required);
        }
    }

    @Override
    public void addComment(String bookEndpoint, MultipartBody.Part[] body) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().showMessage(R.string.login_required);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().addComment(user.getAuthorizeToken(), bookEndpoint, body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().addComment(response.getData().get(0));
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
            getView().showMessage(R.string.network_required);
        }
    }

    @Override
    public void addReply(String bookEndpoint, MultipartBody.Part[] body, int position) {
        User user = getDataManager().getCurrentUser();
        if (user == null || user.getUsername().equals("")) {
            getView().showMessage(R.string.login_required);
            return;
        }
        if (getView().isNetworkConnected()) {
            try {
                getView().showLoading();
                getCompositeDisposable().add(getDataManager().addComment(user.getAuthorizeToken(), bookEndpoint, body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            getView().addReply(response.getData().get(0), position);
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
            getView().showMessage(R.string.network_required);
        }
    }
}
