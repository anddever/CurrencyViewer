package ru.anddever.currencyviewer.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.anddever.currencyviewer.navigation.AppNavHostFragment
import ru.anddever.currencyviewer.ui.converter.ConverterFragment
import ru.anddever.currencyviewer.ui.converter.ConverterViewModel
import ru.anddever.currencyviewer.ui.viewer.ViewerFragment
import ru.anddever.currencyviewer.ui.viewer.ViewerViewModel

@Module
interface PresentationModule {

    @Binds
    @IntoMap
    @FragmentKey(AppNavHostFragment::class)
    fun bindAppNavHostFragment(fragment: AppNavHostFragment): Fragment

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FragmentKey(ViewerFragment::class)
    fun bindViewerFragment(fragment: ViewerFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(ViewerViewModel::class)
    fun bindViewerViewModel(viewModel: ViewerViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(ConverterFragment::class)
    fun bindConverterFragment(fragment: ConverterFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(ConverterViewModel::class)
    fun bindConverterViewModel(viewModel: ConverterViewModel): ViewModel
}