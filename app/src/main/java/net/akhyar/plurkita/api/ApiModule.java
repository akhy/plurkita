package net.akhyar.plurkita.api;

import com.google.gson.Gson;

import net.akhyar.plurkita.AppPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import dagger.Module;
import dagger.Provides;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import oauth.signpost.signature.QueryStringSigningStrategy;
import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import se.akerfeldt.signpost.retrofit.SigningOkClient;

/**
 * @author akhyar
 */
@Module(library = true, complete = false,
        injects = {
                AuthApi.class,
                TimelineApi.class
        })
public class ApiModule {

    private String consumerKey;
    private String consumerSecret;

    public ApiModule(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }


    @Provides
    AuthApi provideAuthApi(AppPreferences pref) {
        OAuthConsumer oAuthConsumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        oAuthConsumer.setTokenWithSecret(pref.getOAuthToken(), pref.getOAuthTokenSecret());
        oAuthConsumer.setSigningStrategy(new QueryStringSigningStrategy());

        Converter converter = new Converter() {
            @Override
            public String fromBody(TypedInput typedInput, Type type) throws ConversionException {
                try {
                    InputStreamReader isr = new InputStreamReader(typedInput.in());
                    BufferedReader reader = new BufferedReader(isr);
                    return reader.readLine();
                } catch (IOException e) {
                    throw new ConversionException("");
                }
            }

            @Override
            public TypedOutput toBody(Object o) {
                return null;
            }
        };

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://www.plurk.com/OAuth")
                .setClient(new SigningOkClient(oAuthConsumer))
                .setConverter(converter)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return adapter.create(AuthApi.class);
    }

    @Provides
    TimelineApi provideTimelineApi(Gson gson, AppPreferences pref) {
        OAuthConsumer oAuthConsumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        oAuthConsumer.setTokenWithSecret(pref.getOAuthToken(), pref.getOAuthTokenSecret());
        oAuthConsumer.setSigningStrategy(new AuthorizationHeaderSigningStrategy());

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://www.plurk.com/APP/Timeline")
                .setClient(new SigningOkClient(oAuthConsumer))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();

        return adapter.create(TimelineApi.class);
    }
}
