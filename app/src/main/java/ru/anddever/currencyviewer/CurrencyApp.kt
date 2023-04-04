package ru.anddever.currencyviewer

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import ru.anddever.currencyviewer.di.DaggerAppComponent
import ru.anddever.currencyviewer.worker.UpdateCurrencyWorker
import java.util.concurrent.TimeUnit

open class CurrencyApp : BaseCurrencyApp() {

    private val appComponent by lazy { initializeAppComponent() }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)

        val updateCurrencyRequest = PeriodicWorkRequest.Builder(
            UpdateCurrencyWorker::class.java, 1,
            TimeUnit.HOURS
        ).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            getString(R.string.upload_currency_data_work),
            ExistingPeriodicWorkPolicy.KEEP, updateCurrencyRequest
        )
    }

    internal open fun initializeAppComponent() =
        DaggerAppComponent.builder().context(applicationContext).build()
}