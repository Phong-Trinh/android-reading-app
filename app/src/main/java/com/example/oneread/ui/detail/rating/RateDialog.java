package com.example.oneread.ui.detail.rating;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.ui.base.BaseDialog;

import javax.inject.Inject;

@SuppressLint("NonConstantResourceId")
public class RateDialog extends BaseDialog implements RateContract.View{

    private static final String TAG = "RateDialog";

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

    public interface Callback {
        void onRating(float rating);
    }

    @Inject
    RatePresenter<RateContract.View> presenter;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Callback callback;
    private String bookEndpoint;
    
    public static RateDialog newInstance(String bookEndpoint) {
        RateDialog rateDialog = new RateDialog();
        Bundle args = new Bundle();
        args.putString("bookEndpoint", bookEndpoint);
        // put any arguments to bundle here
        rateDialog.setArguments(args);
        return rateDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookEndpoint = getArguments().getString("bookEndpoint");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rating_dialog, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(this);

        return view;
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp(View view) {
        ratingBar.setRating(5);
        btnCancel.setOnClickListener(l -> {
            dismissDialog(TAG);
        });
        btnSubmit.setOnClickListener(l -> {
            presenter.onSubmit(ratingBar.getRating(), bookEndpoint);
        });
    }

    @Override
    public void setRating(float rating) {
        callback.onRating(rating);
        dismissDialog(TAG);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    public void setCallBack(Callback callback) {
        this.callback = callback;
    }
}
