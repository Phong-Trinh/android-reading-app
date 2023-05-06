package com.example.oneread.ui.login.fragment.signin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.oneread.R;
import com.example.oneread.ui.base.BaseFragment;

import javax.inject.Inject;

@SuppressLint("NonConstantResourceId")
public class SignInFragment extends BaseFragment implements SignInContract.View {

    private static final String TAG = "SignInFragment";

    @Inject
    SignInPresenter<SignInContract.View> signInPresenter;

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.cardview)
    CardView cardLogin;

    public SignInFragment() {}

    @Override
    public void onDestroyView() {
        signInPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        signInPresenter.onAttach(this);
        return view;
    }

    @Override
    protected void setup(View view) {
        cardLogin.getBackground().setAlpha(200);
    }

    @OnClick({R.id.btn_register})
    void openRegisterPage() {
        signInPresenter.onSignUpClick();
    }

    @OnClick(R.id.btn_login)
    void onButtonLoginClick() {
        signInPresenter.onLoginClick(username.getText().toString(), password.getText().toString());
    }

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
}
