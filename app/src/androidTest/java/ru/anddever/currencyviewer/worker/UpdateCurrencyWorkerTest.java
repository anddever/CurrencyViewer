package ru.anddever.currencyviewer.worker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.work.ListenableWorker;
import androidx.work.testing.TestListenableWorkerBuilder;
import androidx.work.testing.TestWorkerBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test if UpdateCurrencyWorker execute successful
 */
public class UpdateCurrencyWorkerTest {
    private Context context;
    private Executor executor;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void testUploadCurrencyWorker() {
        UpdateCurrencyWorker worker =
                (UpdateCurrencyWorker) TestWorkerBuilder.from(context,
                        UpdateCurrencyWorker.class,
                        executor)
                        .build();

        worker.doWork();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ListenableWorker.Result result = worker.result[0];
        assertThat(result, is(ListenableWorker.Result.success()));
    }
}