package ru.anddever.currencyviewer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.anddever.currencyviewer.CurrencyApp
import ru.anddever.currencyviewer.ui.MainActivity
import javax.inject.Singleton

@Component(modules = [PresentationModule::class, NetworkModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(app: CurrencyApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}