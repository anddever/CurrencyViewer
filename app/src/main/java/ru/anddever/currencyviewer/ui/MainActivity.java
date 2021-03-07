package ru.anddever.currencyviewer.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.anddever.currencyviewer.R;
import ru.anddever.currencyviewer.databinding.ActivityMainBinding;
import ru.anddever.currencyviewer.model.CurrencyDetails;
import ru.anddever.currencyviewer.model.CurrencyResponse;
import ru.anddever.currencyviewer.network.RetrofitClient;
import ru.anddever.currencyviewer.repository.CurrencyRepository;
import ru.anddever.currencyviewer.ui.adapter.CurrencyAdapter;

public class MainActivity extends AppCompatActivity {

    static final String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding binding;
    private ArrayList<CurrencyDetails> currencies;
    private CurrencyAdapter adapter;
    private CurrencyRepository repository;

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

        binding.swipeRefresh.setOnRefreshListener(() -> {
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout");
            loadCurrencyData(repository);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(() -> {
            repository = new CurrencyRepository(getApplication());
            currencies.clear();
            currencies.addAll(repository.getAllCurrencies());
            Log.d(TAG, "onStart:  currencies.size() " + currencies.size());

            if (currencies.size() == 0) {
                loadCurrencyData(repository);
            }
        }).start();
    }

    private void loadCurrencyData(CurrencyRepository repository) {
        Call<CurrencyResponse> currencyCall =
                RetrofitClient.getInstance().getServerApi().getCurrency();
        currencyCall.enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyResponse> call,
                                   @NonNull Response<CurrencyResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
                currencies.clear();
                currencies.addAll(response.body().getValute().values());
                repository.insertAll(currencies);
                adapter.notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            binding.swipeRefresh.setRefreshing(true);
            loadCurrencyData(repository);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}