package net.akhyar.plurkita;

import com.activeandroid.ActiveAndroid;

import net.akhyar.plurkita.api.ApiModule;
import net.akhyar.plurkita.module.ApplicationModule;
import net.akhyar.plurkita.module.UtilityModule;
import net.danlew.android.joda.ResourceZoneInfoProvider;

import javax.inject.Inject;

import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * @author akhyar
 */
public class Application extends android.app.Application {

    private static ObjectGraph graph;

    public static void inject(Object object) {
        graph.inject(object);
    }

    public static <T> T getInstance(Class<T> cls) {
        return graph.get(cls);
    }

    @Inject
    EventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
        ResourceZoneInfoProvider.init(this);
        graph = ObjectGraph.create(
                new ApplicationModule(this),
                new UtilityModule(),
                new ApiModule("Fzl5AiKciFln", "1Nv4RFnUalRpTsktTM5MEmXAHdNpwGRQ")
        );
        graph.inject(this);
        eventBus.register(this);
        Timber.plant(graph.get(Timber.Tree.class));
    }

    public void onEvent(Void ignored) {
    }
}
