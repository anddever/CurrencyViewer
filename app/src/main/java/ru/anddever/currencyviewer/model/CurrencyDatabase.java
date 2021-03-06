package ru.anddever.currencyviewer.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Creating Room database for storing currencies using singleton pattern
 * to prevent having multiple instances of the database opened at the same time
 */
@Database(entities = {CurrencyDetails.class}, version = 1)
public abstract class CurrencyDatabase extends RoomDatabase {

    public abstract CurrencyDao currencyDao();

    private static volatile CurrencyDatabase INSTANCE;

    /**
     * Creating an ExecutorService with a fixed thread pool that we will use to run
     * database operations asynchronously on a background thread
     */
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CurrencyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CurrencyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CurrencyDatabase.class, "currency_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}