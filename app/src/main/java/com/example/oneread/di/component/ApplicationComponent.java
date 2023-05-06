package com.example.oneread.di.component;

import com.example.oneread.App;
import com.example.oneread.data.DataManager;
import com.example.oneread.di.module.ApplicationModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(App app);

    DataManager getDataManager();

}
