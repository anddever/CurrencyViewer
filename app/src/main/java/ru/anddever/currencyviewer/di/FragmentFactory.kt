package ru.anddever.currencyviewer.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject

class FragmentFactory @Inject constructor(
    private val fragments: Map<Class<out Fragment>, @JvmSuppressWildcards Fragment>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
        fragments[Class.forName(className)] as Fragment
}