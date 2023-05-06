package com.example.oneread.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.room.Room;
import com.example.oneread.App;
import com.example.oneread.data.db.AppDatabase;
import com.example.oneread.data.network.IServiceAPI;
import com.example.oneread.di.anotation.ApplicationContext;
import com.example.oneread.di.anotation.CachedThreadPool;
import com.example.oneread.di.anotation.FixedThreadPool;
import com.example.oneread.utils.AppConstants;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Module
public class ApplicationModule {
    App application;

    public ApplicationModule(App application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    public Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {return application;}

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreference() {
        return application.getSharedPreferences("SharePrefs", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder().baseUrl(AppConstants.baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public IServiceAPI provideIServiceAPI(Retrofit retrofit) {
        return retrofit.create(IServiceAPI.class);
    }

    @Provides
    @Singleton
    @FixedThreadPool
    ExecutorService provideFixedThreadExecutorService() {
        return Executors.newFixedThreadPool(AppConstants.NUMBER_OF_THREADS);
    }

    @Provides
    @Singleton
    @CachedThreadPool
    ExecutorService provideCachedThreadExecutorService() {return Executors.newCachedThreadPool();}

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, AppConstants.DB_NAME)
                .fallbackToDestructiveMigration().build();
    }

}
