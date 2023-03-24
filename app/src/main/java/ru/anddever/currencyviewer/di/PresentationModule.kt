package ru.anddever.currencyviewer.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
interface PresentationModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}