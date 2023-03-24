package ru.anddever.currencyviewer;

import android.app.Application;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import ru.anddever.currencyviewer.worker.UpdateCurrencyWorker;

public class CurrencyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*
          Defining work as unique and periodic for preventing multiple instances
         */
        PeriodicWorkRequest updateCurrencyRequest =
                new PeriodicWorkRequest.Builder(UpdateCurrencyWorker.class, 1,
                        TimeUnit.HOURS).build();
        WorkManager
                .getInstance(this)
                .enqueueUniquePeriodicWork(getString(R.string.upload_currency_data_work),
                        ExistingPeriodicWorkPolicy.KEEP,
                        updateCurrencyRequest);
    }
}