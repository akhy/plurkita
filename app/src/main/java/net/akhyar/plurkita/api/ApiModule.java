package net.akhyar.plurkita.api;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import net.akhyar.plurkita.AppPreferences;
import net.akhyar.plurkita.api.util.SignedHeaderClient;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.PlurkApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * @author akhyar
 */
@Module(library = true, complete = false,
        injects = {
                AuthApi.class,
                TimelineApi.class,
                ResponseApi.class
        })
public class ApiModule {

    private String consumerKey;
    private String consumerSecret;

    public ApiModule(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    @Provides
    AuthApi provideAuthApi(OAuthService service, AppPreferences pref) {
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


        Token token = (pref.getOAuthToken() == null || pref.getOAuthTokenSecret() == null)
                ? Token.empty()
                : new Token(pref.getOAuthToken(), pref.getOAuthTokenSecret());

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://www.plurk.com/OAuth")
                .setClient(new SignedHeaderClient(service, token))
                .setConverter(converter)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return adapter.create(AuthApi.class);
    }

    @Provides
    OAuthService provideOAuthService() {
        return new ServiceBuilder().provider(PlurkApi.Mobile.class)
                .apiKey(consumerKey)
                .apiSecret(consumerSecret)
                .build();
    }

    @Provides
    SignedHeaderClient provideSignedHeaderClient(OAuthService service, AppPreferences pref) {
        return new SignedHeaderClient(service, new Token(pref.getOAuthToken(), pref.getOAuthTokenSecret()));
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    RestAdapter provideDefaultRestAdapter(Gson gson, SignedHeaderClient client) {
        return new RestAdapter.Builder()
                .setEndpoint("http://www.plurk.com/APP")
                .setClient(client)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    @Provides
    TimelineApi provideTimelineApi(RestAdapter restAdapter) {
        return restAdapter.create(TimelineApi.class);
    }

    @Provides
    ResponseApi provideResponseApi(RestAdapter restAdapter) {
        return restAdapter.create(ResponseApi.class);
    }


}
