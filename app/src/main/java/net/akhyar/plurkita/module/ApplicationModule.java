package net.akhyar.plurkita.module;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.akhyar.plurkita.AppPreferences;
import net.akhyar.plurkita.Application;
import net.akhyar.plurkita.AuthorizeActivity;
import net.akhyar.plurkita.BaseActivity;
import net.akhyar.plurkita.TimelineActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author akhyar
 */
@Module(library = true, complete = false,
        injects = {
                Application.class,
                BaseActivity.class,
                TimelineActivity.class,
                AuthorizeActivity.class
        })
public class ApplicationModule {

    private static final String PREF_KEY = "plurkita";
    private Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    AppPreferences provideAppPreferences(Context context) {
        return new AppPreferences(context.getSharedPreferences(PREF_KEY, 0));
    }

    @Provides
    @Singleton
    AlarmManager provideAlarmManager() {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager() {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
