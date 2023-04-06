package ru.anddever.currencyviewer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import ru.anddever.currencyviewer.databinding.ActivityMainBinding
import ru.anddever.currencyviewer.di.FragmentFactory
import ru.anddever.currencyviewer.utils.TAG
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val navController: NavController
        get() = findNavController(R.id.nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as CurrencyApp).appComponent.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory

        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        Log.d(TAG, "MainActivity.onCreate: ")
    }

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration) ||
            super.onSupportNavigateUp()
}