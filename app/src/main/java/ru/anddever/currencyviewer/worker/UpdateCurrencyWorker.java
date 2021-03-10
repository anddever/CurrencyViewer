package ru.anddever.currencyviewer.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.anddever.currencyviewer.model.CurrencyResponse;
import ru.anddever.currencyviewer.network.RetrofitClient;

public class UpdateCurrencyWorker extends Worker {


    public UpdateCurrencyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final Result[] result = {Result.failure()};
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<CurrencyResponse> currencyCall =
                RetrofitClient.getInstance().getServerApi().getCurrency();
        currencyCall.enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyResponse> call, @NonNull Response<CurrencyResponse> response) {

            }

            @Override
            public void onFailure(@NonNull Call<CurrencyResponse> call, @NonNull Throwable t) {

            }
        });
        return null;
    }
}