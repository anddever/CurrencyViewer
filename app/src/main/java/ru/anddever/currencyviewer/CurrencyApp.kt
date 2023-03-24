package ru.anddever.currencyviewer

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import ru.anddever.currencyviewer.di.DaggerAppComponent
import ru.anddever.currencyviewer.worker.UpdateCurrencyWorker
import java.util.concurrent.TimeUnit

class CurrencyApp : BaseCurrencyApp() {

    override fun onCreate() {
        super.onCreate()
        val updateCurrencyRequest = PeriodicWorkRequest.Builder(
            UpdateCurrencyWorker::class.java, 1,
            TimeUnit.HOURS
        ).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            getString(R.string.upload_currency_data_work),
            ExistingPeriodicWorkPolicy.KEEP, updateCurrencyRequest
        )

        val appComponent = DaggerAppComponent.builder().context(applicationContext).build()
        appComponent.inject(this)
    }
}