package com.example.oneread.ui.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.ui.base.PagerAdapter;
import com.example.oneread.ui.base.BaseActivity;
import com.example.oneread.ui.login.fragment.signin.SignInFragment;
import com.example.oneread.ui.login.fragment.signup.SignUpFragment;

import javax.inject.Inject;

@SuppressLint("NonConstantResourceId")
public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter<LoginContract.View> presenter;

    @Inject
    PagerAdapter pagerAdapter;

    @BindView(R.id.viewpager)
    public ViewPager2 viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        pagerAdapter = new PagerAdapter(this);
        pagerAdapter.addFragment(new SignInFragment());
        pagerAdapter.addFragment(new SignUpFragment());
        viewpager.setUserInputEnabled(false);
        viewpager.setAdapter(pagerAdapter);
    }

    @Override
    public void openSignInPage() {
        viewpager.setCurrentItem(0);
    }

    @Override
    public void openSignUpPage() {
        viewpager.setCurrentItem(1);
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }

    @Override
    public void onLoginSuccess() {
        setResult(RESULT_OK);
        finish();
    }


}