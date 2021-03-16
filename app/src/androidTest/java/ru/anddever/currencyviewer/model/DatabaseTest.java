package ru.anddever.currencyviewer.model;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Test write and read functions in database
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private CurrencyDao currencyDao;
    private CurrencyDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, CurrencyDatabase.class).build();
        currencyDao = db.currencyDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeCurrencyAndRead() {
        List<CurrencyDetails> details = new ArrayList<>();
        CurrencyDetails detail = new CurrencyDetails();
        details.add(detail);
        detail.setID("R01010");
        detail.setName("Австралийский доллар");
        detail.setValue(new BigDecimal("56.7033"));
        currencyDao.insertAll(details);
        CurrencyDetails byId = currencyDao.findById("R01010");
        Assert.assertThat(byId.getName(), equalTo("Австралийский доллар"));
    }
}