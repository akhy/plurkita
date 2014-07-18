package net.akhyar.plurkita.module;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import net.akhyar.plurkita.BuildConfig;
import net.akhyar.plurkita.model.DateTimeTypeConverter;

import org.joda.time.DateTime;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * @author akhyar
 */
@Module(library = true, complete = false,
        injects = {
                Timber.Tree.class
        })
public class UtilityModule {

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getName().startsWith("_");
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    Timber.Tree provideTimberTree() {
        return BuildConfig.DEBUG ? new Timber.DebugTree() : new Timber.HollowTree();
    }

    @Provides
    Picasso providePicasso(Context context) {
        return new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(context))
                .build();
    }
}
