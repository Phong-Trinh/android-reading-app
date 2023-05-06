package com.example.oneread;

import android.app.Application;
import com.example.oneread.data.DataManager;
import com.example.oneread.di.component.ApplicationComponent;
import com.example.oneread.di.component.DaggerApplicationComponent;
import com.example.oneread.di.module.ApplicationModule;

import javax.inject.Inject;

public class App extends Application {

    @Inject
    DataManager dataManager;

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplicationComponent();
    }

    private void initApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
