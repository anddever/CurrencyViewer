package ru.anddever.currencyviewer.worker;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.anddever.currencyviewer.model.CurrencyDetails;
import ru.anddever.currencyviewer.model.CurrencyResponse;
import ru.anddever.currencyviewer.network.RetrofitClient;
import ru.anddever.currencyviewer.repository.CurrencyRepository;
import ru.anddever.currencyviewer.utils.Utils;

import static ru.anddever.currencyviewer.utils.Constants.DATA_TIMESTAMP;

/**
 * Worker for periodical loading currency data in background
 */
public class UpdateCurrencyWorker extends Worker {

    public static final String TAG = UpdateCurrencyWorker.class.getSimpleName();
    public final Result[] result = new Result[]{Result.failure()};

    public UpdateCurrencyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Context context = getApplicationContext();

        Call<CurrencyResponse> currencyCall =
                RetrofitClient.getInstance().getServerApi().getCurrency();
        currencyCall.enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyResponse> call,
                                   @NonNull Response<CurrencyResponse> response) {
                if (response.body() != null) {
                    new Thread(() -> {
                        try {
                            ArrayList<CurrencyDetails> currencies =
                                    new ArrayList<>(response.body().getValute().values());
                            CurrencyRepository repository =
                                    new CurrencyRepository(getApplicationContext());
                            repository.insertAll(currencies);
                            PreferenceManager.getDefaultSharedPreferences(context)
                                    .edit()
                                    .putString(DATA_TIMESTAMP,
                                            Utils.dateConverter(response.body().getTimestamp()))
                                    .apply();
                            result[0] = Result.success();
                            countDownLatch.countDown();
                            Log.d(TAG, "onResponse: success");
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: ", e);
                            result[0] = Result.failure();
                            countDownLatch.countDown();
                        }
                    }).start();
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "onResponse: body is null");
                    result[0] = Result.failure();
                    countDownLatch.countDown();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CurrencyResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                result[0] = Result.retry();
                countDownLatch.countDown();
            }
        });
        return result[0];
    }
}