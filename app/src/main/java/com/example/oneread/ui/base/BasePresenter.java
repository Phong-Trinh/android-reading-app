package com.example.oneread.ui.base;

import com.example.oneread.data.DataManager;
import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<V extends BaseContract.View> implements BaseContract.Presenter<V> {

    private static final String TAG = "BasePresenter";
    private DataManager dataManager;
    private CompositeDisposable compositeDisposable;

    private V view;

    public BasePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        view = null;
        compositeDisposable.dispose();
    }

    public boolean isViewAttached() {
        return view != null;
    }

    public V getView() {
        return view;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }
}
