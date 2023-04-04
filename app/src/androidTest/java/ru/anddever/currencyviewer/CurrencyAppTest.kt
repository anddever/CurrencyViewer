package ru.anddever.currencyviewer

import ru.anddever.currencyviewer.di.DaggerTestAppComponent

class CurrencyAppTest : CurrencyApp() {

    override fun initializeAppComponent() = DaggerTestAppComponent.create()
}