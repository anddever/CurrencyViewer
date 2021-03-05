package ru.anddever.currencyviewer;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.anddever.currencyviewer.databinding.ActivityMainBinding;
import ru.anddever.currencyviewer.model.CurrencyResponse;
import ru.anddever.currencyviewer.network.RetrofitClient;

public class MainActivity extends AppCompatActivity {

    static final String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Call<CurrencyResponse> currencyCall =
                RetrofitClient.getInstance().getServerApi().getCurrency();
        currencyCall.enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyResponse> call,
                                   @NonNull Response<CurrencyResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CurrencyResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}