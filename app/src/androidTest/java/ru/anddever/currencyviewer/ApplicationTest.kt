package ru.anddever.currencyviewer

import androidx.test.core.app.ActivityScenario
import org.junit.Test
import ru.anddever.currencyviewer.ui.OldActivity

class ApplicationTest {

    @Test
    fun runApp() {
        ActivityScenario.launch(OldActivity::class.java)
    }
}