package com.example.oneread.di.component;

import com.example.oneread.di.anotation.ActivityScope;
import com.example.oneread.di.module.ActivityModule;
import com.example.oneread.ui.bookcase.BookCaseActivity;
import com.example.oneread.ui.delete.DeleteChapterActivity;
import com.example.oneread.ui.detail.DetailActivity;
import com.example.oneread.ui.detail.chapter.ListChapterFragment;
import com.example.oneread.ui.detail.comment.CommentFragment;
import com.example.oneread.ui.detail.info.AboutFragment;
import com.example.oneread.ui.detail.rating.RateDialog;
import com.example.oneread.ui.download.DownloadChapterActivity;
import com.example.oneread.ui.listbook.ListBookActivity;
import com.example.oneread.ui.listbook.search.SearchFragment;
import com.example.oneread.ui.login.LoginActivity;
import com.example.oneread.ui.login.fragment.signin.SignInFragment;
import com.example.oneread.ui.login.fragment.signup.SignUpFragment;
import com.example.oneread.ui.main.MainActivity;
import com.example.oneread.ui.main.notify.NotificationDialog;
import com.example.oneread.ui.profile.ProfileActivity;
import com.example.oneread.ui.read.ChapterActivity;
import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(SignInFragment signInFragment);

    void inject(SignUpFragment signUpFragment);

    void inject(ListBookActivity listBookActivity);

    void inject(SearchFragment searchFragment);

    void inject(DetailActivity detailActivity);

    void inject(RateDialog rateDialog);

    void inject(AboutFragment aboutFragment);

    void inject(ListChapterFragment chapterFragment);

    void inject(CommentFragment commentFragment);

    void inject(DownloadChapterActivity downloadChapterActivity);

    void inject(DeleteChapterActivity deleteChapterActivity);

    void inject(ChapterActivity chapterActivity);

    void inject(BookCaseActivity bookCaseActivity);

    void inject(NotificationDialog notificationDialog);

    void inject(ProfileActivity profileActivity);
}
