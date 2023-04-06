package ru.anddever.currencyviewer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.anddever.currencyviewer.CurrencyApp
import ru.anddever.currencyviewer.MainActivity
import ru.anddever.currencyviewer.ui.OldActivity
import javax.inject.Singleton

@Component(modules = [PresentationModule::class, NetworkModule::class])
@Singleton
interface AppComponent {

    fun inject(app: CurrencyApp)

    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}