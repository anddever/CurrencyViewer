package ru.anddever.currencyviewer.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.anddever.currencyviewer.databinding.ActivityMainBinding;
import ru.anddever.currencyviewer.model.CurrencyDetails;
import ru.anddever.currencyviewer.model.CurrencyResponse;
import ru.anddever.currencyviewer.network.RetrofitClient;
import ru.anddever.currencyviewer.ui.adapter.CurrencyAdapter;

public class MainActivity extends AppCompatActivity {

    static final String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding binding;
    private ArrayList<CurrencyDetails> currencies;
    private CurrencyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currencies = new ArrayList<>();
        adapter = new CurrencyAdapter(currencies);
        RecyclerView currencyRecycler = binding.currencyRecycler;
        currencyRecycler.setAdapter(adapter);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            currencyRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        else
            currencyRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        currencyRecycler.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        currencyRecycler.setItemAnimator(new DefaultItemAnimator());
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
                currencies.addAll(response.body().getValute().values());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<CurrencyResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void openConverter(View view) {
        startActivity(new Intent(this, ConverterActivity.class));
    }
}