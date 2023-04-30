package ru.anddever.currencyviewer.di

import android.os.Build
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.anddever.currencyviewer.BuildConfig
import java.util.concurrent.TimeUnit

private const val CBR_XML_DAILY_URL = "cbr-xml-daily.ru"

@Module
class NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(provideBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideClient())
        .build()

    @Provides
    fun provideBaseUrl(): String {
        val protocol = if (Build.VERSION.SDK_INT < 22) "http" else "https"
        return "$protocol://$CBR_XML_DAILY_URL/"
    }

    @Provides
    fun provideClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

        val modernTlsSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_0)
            .allEnabledCipherSuites()
            .build()

        val specs = listOf(ConnectionSpec.CLEARTEXT, modernTlsSpec)

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectionSpecs(specs)
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
    }
}