package com.example.oneread.ui.login.fragment.signup;

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
public class SignUpFragment extends BaseFragment implements SignUpContract.View {

    private static final String TAG = "SignUpFragment";

    @Inject
    SignUpPresenter<SignUpContract.View> signUpPresenter;

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.cardview)
    CardView cardLogin;

    public SignUpFragment() {}

    @Override
    public void onDestroyView() {
        signUpPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        signUpPresenter.onAttach(this);
        return view;
    }

    @Override
    protected void setup(View view) {
        cardLogin.getBackground().setAlpha(200);
    }

    @OnClick({R.id.btn_login})
    void openLoginPage() {
        signUpPresenter.onSignInClick();
    }

    @OnClick(R.id.btn_register)
    void onButtonLoginClick() {
        signUpPresenter.onRegisterClick(username.getText().toString(),
                password.getText().toString(), email.getText().toString());
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
