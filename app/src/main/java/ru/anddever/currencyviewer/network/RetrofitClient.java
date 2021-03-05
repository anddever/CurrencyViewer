package ru.anddever.currencyviewer.network;

import android.os.Build;

import androidx.viewbinding.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String CBR_XML_DAILY_URL = "cbr-xml-daily.ru";

    private final ServerApi serverApi;
    private final OkHttpClient client;
    private volatile static RetrofitClient retrofitClient;

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY :
                HttpLoggingInterceptor.Level.NONE);

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();

        String protocol = Build.VERSION.SDK_INT < 22 ? "https://" : "http://";

        StringBuffer baseUrlBuilder = new StringBuffer(protocol);
        baseUrlBuilder.append(CBR_XML_DAILY_URL).append("/");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlBuilder.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.serverApi = retrofit.create(ServerApi.class);
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            synchronized (ServerApi.class) {
                if (retrofitClient == null) {
                    retrofitClient = new RetrofitClient();
                }
            }
        }
        return retrofitClient;
    }

    public ServerApi getServerApi() {
        return this.serverApi;
    }

    public OkHttpClient getClient() {
        return this.client;
    }
}