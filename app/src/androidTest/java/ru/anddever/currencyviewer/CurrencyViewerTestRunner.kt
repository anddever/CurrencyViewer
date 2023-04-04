package ru.anddever.currencyviewer

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class CurrencyViewerTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application = super.newApplication(cl, CurrencyAppTest::class.java.name, context)
}