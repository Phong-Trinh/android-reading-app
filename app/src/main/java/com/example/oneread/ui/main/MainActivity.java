package com.example.oneread.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.HistoryRead;
import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BaseActivity;
import com.example.oneread.ui.base.RectBookAdapter;
import com.example.oneread.ui.base.RoundBookAdapter;
import com.example.oneread.ui.bookcase.BookCaseActivity;
import com.example.oneread.ui.listbook.ListBookActivity;
import com.example.oneread.ui.login.LoginActivity;
import com.example.oneread.ui.main.adapter.HistoryReadAdapter;
import com.example.oneread.ui.main.adapter.SliderAdapter;
import com.example.oneread.ui.main.notify.NotificationDialog;
import com.example.oneread.ui.profile.ProfileActivity;
import com.example.oneread.utils.AppConstants;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("NonConstantResourceId")
public class MainActivity  extends BaseActivity implements MainContract.View, View.OnClickListener {

    @Inject
    MainPresenter<MainContract.View> presenter;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.avatar)
    RoundedImageView avatar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_logout)
    TextView btnLogout;
    @BindView(R.id.search)
    CardView cardSearch;
    @BindView(R.id.slider)
    ViewPager2 sliderView;
    @BindView(R.id.layout_following)
    LinearLayout layoutFollowing;
    @BindView(R.id.list_following)
    RecyclerView listFollowing;
    @BindView(R.id.layout_suggest)
    LinearLayout layoutSuggest;
    @BindView(R.id.list_suggest)
    RecyclerView listSuggest;
    @BindView(R.id.layout_recent)
    LinearLayout layoutRecent;
    @BindView(R.id.list_recent)
    RecyclerView listRecent;
    @BindView(R.id.notification)
    CardView cardNotification;

    private RoundedImageView navAvatar;
    private TextView navUsername;
    private Switch btnSwitchThemeMode;
    private NotificationDialog notificationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    protected void onResume() {
        super.onResume();
        presenter.onUserLoggedIn();
    }

    @Override
    protected void setup() {
        setupView();
        setupNavMenu();
        presenter.onNavMenuCreated();
        presenter.getTrending();
        presenter.getFollowing();
        presenter.getSuggest();
        presenter.getRecent();

        listFollowing.setLayoutManager(new GridLayoutManager(this, 3));
        listFollowing.setAdapter(new RectBookAdapter(this, new ArrayList<>()));
        listSuggest.setLayoutManager(new GridLayoutManager(this, 3));
        listSuggest.setAdapter(new RectBookAdapter(this, new ArrayList<>()));
        listRecent.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        listRecent.setAdapter(new HistoryReadAdapter(this, new ArrayList<>()));

        notificationDialog = new NotificationDialog();
    }

    private void setupView() {
        avatar.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        cardSearch.setOnClickListener(this);
        cardNotification.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                openNavigationDrawer();
                break;
            case R.id.btn_login:
                openLoginActivity();
                break;
            case R.id.btn_logout:
                presenter.onOptionLogoutClick();
                break;
            case R.id.btn_switch_theme_mode:
                presenter.switchThemeMode();
                break;
            case R.id.search:
                openSearchActivity();
                break;
            case R.id.notification:
                notificationDialog.show(getSupportFragmentManager());
            default:
                break;
        }
    }

    private void setupNavMenu() {
        View header = navigationView.getHeaderView(0);
        navAvatar = header.findViewById(R.id.avatar);
        navUsername = header.findViewById(R.id.username);
        Menu menu = navigationView.getMenu();
        btnSwitchThemeMode = (Switch) menu.findItem(R.id.btn_switch_theme_mode).getActionView();
        btnSwitchThemeMode.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btn_about_me:
                    presenter.onOptionAboutClick();
                    return true;
                case R.id.btn_lib:
                    presenter.onOptionBookCaseClick();
                    return true;
                default:
                    return false;
            }
        });
    }


    @Override
    public void openLoginActivity() {
        startActivityForResult(new Intent(this, LoginActivity.class), AppConstants.LOGIN_REQUEST_CODE);
    }

    @Override
    public void openSearchActivity() {
        startActivity(new Intent(this, ListBookActivity.class));
    }

    @Override
    public void openBookCaseActivity() {
        startActivity(new Intent(this, BookCaseActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            presenter.onUserLoggedIn();
        }
    }

    @Override
    public void showAboutFragment() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public void updateUserProfile(User user) {
        if (user != null) {
            Glide.with(this).load(user.getAvatar()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(avatar);
            Glide.with(this).load(user.getAvatar()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(navAvatar);
            navUsername.setText(user.getUsername());
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            Glide.with(this).load(R.drawable.avatar).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(avatar);
            Glide.with(this).load(R.drawable.avatar).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(navAvatar);
            navUsername.setText("");
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setThemeMode(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
        btnSwitchThemeMode.setChecked(mode == AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    public void openNavigationDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void closeNavigationDrawer() {
        if (drawerLayout!= null && drawerLayout.isOpen())
            drawerLayout.close();
    }

    @Override
    public void setTrending(List<Book> books) {
        SliderAdapter adapter = new SliderAdapter(this, books);

        sliderView.setAdapter(adapter);
        sliderView.setNestedScrollingEnabled(false);
        sliderView.setClipToPadding(false);
        sliderView.setClipChildren(false);
        sliderView.setOffscreenPageLimit(2);
        sliderView.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        autoSlideViewPager();

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(60));
        compositePageTransformer.addTransformer((page, position) -> {
            float MIN_SCALE = 0.85f;
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();

            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                page.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                page.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setAlpha(1f);
        });

        sliderView.setPageTransformer(compositePageTransformer);
    }

    private void autoSlideViewPager() {
        if (sliderView == null) return;
        int c = sliderView.getAdapter().getItemCount();
        int i = sliderView.getCurrentItem() + 1;
        if (i >= c) i = 0;
        sliderView.setCurrentItem(i);
        sliderView.postDelayed(this::autoSlideViewPager, 4000);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setSuggest(List<Book> books) {
        if (books == null) {
            layoutSuggest.setVisibility(View.GONE);
        } else {
            layoutSuggest.setVisibility(View.VISIBLE);
            ((RectBookAdapter) Objects.requireNonNull(listSuggest.getAdapter())).getBooks().clear();
            ((RectBookAdapter) Objects.requireNonNull(listSuggest.getAdapter())).getBooks().addAll(books);
            ((RectBookAdapter) Objects.requireNonNull(listSuggest.getAdapter())).notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setFollowing(List<Book> books) {
        if (books == null) {
            layoutFollowing.setVisibility(View.GONE);
        } else {
            layoutFollowing.setVisibility(View.VISIBLE);
            ((RectBookAdapter) Objects.requireNonNull(listFollowing.getAdapter())).getBooks().clear();
            ((RectBookAdapter) Objects.requireNonNull(listFollowing.getAdapter())).getBooks().addAll(books);
            ((RectBookAdapter) Objects.requireNonNull(listFollowing.getAdapter())).notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setRecent(List<HistoryRead> historyReads) {
        if (historyReads == null) {
            layoutRecent.setVisibility(View.GONE);
        } else {
            layoutRecent.setVisibility(View.VISIBLE);
            ((HistoryReadAdapter) Objects.requireNonNull(listRecent.getAdapter())).getHistoryReads().clear();
            ((HistoryReadAdapter) Objects.requireNonNull(listRecent.getAdapter())).getHistoryReads().addAll(historyReads);
            ((HistoryReadAdapter) Objects.requireNonNull(listRecent.getAdapter())).notifyDataSetChanged();
        }
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }


}