package ru.anddever.currencyviewer

import androidx.test.core.app.ActivityScenario
import org.junit.Test
import ru.anddever.currencyviewer.ui.MainActivity

class ApplicationTest {

    @Test
    fun runApp() {
        ActivityScenario.launch(MainActivity::class.java)
    }
}