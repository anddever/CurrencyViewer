package ru.anddever.currencyviewer.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = [PresentationModule::class, NetworkModule::class])
@Singleton
interface TestAppComponent : AppComponent