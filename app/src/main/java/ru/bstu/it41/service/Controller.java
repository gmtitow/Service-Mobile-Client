package ru.bstu.it41.service;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.bstu.it41.service.any.DataStore;

import static ru.bstu.it41.service.any.UserRequest.URL_HOST;
import static ru.bstu.it41.service.any.UserRequest.URL_SCHEME;

/**
 * Created by NorD on 16.11.2017.
 */

public class Controller implements Serializable{

    private static OkHttpClient client = null;

    public Controller(final Context context){

        //PreferenceManager.getDefaultSharedPreferences(context).edit().remove(PREF_COOKIES).apply();

        if(client == null) {
            DataStore.clearPreferences(context);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            client = builder.followRedirects(false).cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    if (cookieStore.get(url.host()) != null) {
                        List<Cookie> curCookies = cookieStore.get(url.host());
                        List<Cookie> result = new ArrayList<Cookie>();
                        result.addAll(curCookies);
                        int cur_size = curCookies.size();
                        for (int j = 0; j < cookies.size(); j++) {
                            for (int i = 0; i < cur_size; i++) {
                                if (result.get(i).name().equals(cookies.get(j).name())) {
                                    result.remove(i);
                                    cur_size -= 1;
                                    i--;
                                }
                            }
                            result.add(cookies.get(j));
                            //curCookies.add(cookies.get(j));
                        }
                        cookieStore.put(url.host(), result);
                    } else
                        cookieStore.put(url.host(), cookies);


                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            }).connectTimeout(15, TimeUnit.SECONDS).build();
        }
    }
    static final HttpUrl url = new HttpUrl.Builder().scheme(URL_SCHEME).host(URL_HOST).build();

    public static serviceAPI getStaticAPI(){
            ScalarsConverterFactory factory = ScalarsConverterFactory.create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(factory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        serviceAPI api = retrofit.create(serviceAPI.class);
        return api;
    }

    public static serviceAPI getGsonAPI(){
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        serviceAPI  api = retrofit.create(serviceAPI.class);
        return api;
    }

    public static serviceAPI getApi(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        serviceAPI itableApi=retrofit.create(serviceAPI.class);
        return itableApi;
    }

}