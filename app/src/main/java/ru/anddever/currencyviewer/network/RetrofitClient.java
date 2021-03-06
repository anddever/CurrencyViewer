package ru.anddever.currencyviewer.network;

import android.os.Build;

import androidx.viewbinding.BuildConfig;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creating Retrofit client network calls using singleton pattern
 * to prevent having multiple instances of the client at the same time
 */
public class RetrofitClient {

    public static final String CBR_XML_DAILY_URL = "cbr-xml-daily.ru";

    private final ServerApi serverApi;
    private final OkHttpClient client;
    private volatile static RetrofitClient INSTANCE;

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY :
                HttpLoggingInterceptor.Level.NONE);


        ConnectionSpec modernTlsSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_0)
                .allEnabledCipherSuites()
                .build();
        List<ConnectionSpec> specs = Arrays.asList(ConnectionSpec.CLEARTEXT, modernTlsSpec);

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                // Preventing SSLHandshakeException on devices with Android 4
                .connectionSpecs(specs)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();

        //If use https SSLProtocolException occur on deices lower sdk 22
        String protocol = Build.VERSION.SDK_INT > 21 ? "https://" : "http://";
        //String protocol = "https://";

        StringBuffer baseUrlBuilder = new StringBuffer();
        baseUrlBuilder
                .append(protocol)
                .append(CBR_XML_DAILY_URL)
                .append("/");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlBuilder.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.serverApi = retrofit.create(ServerApi.class);
    }

    public static RetrofitClient getInstance() {
        if (INSTANCE == null) {
            synchronized (ServerApi.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitClient();
                }
            }
        }
        return INSTANCE;
    }

    public ServerApi getServerApi() {
        return this.serverApi;
    }

    public OkHttpClient getClient() {
        return this.client;
    }
}