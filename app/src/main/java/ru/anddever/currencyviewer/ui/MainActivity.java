package ru.anddever.currencyviewer.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

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
import ru.anddever.currencyviewer.utils.Utils;

import static ru.anddever.currencyviewer.utils.Constants.DATA_TIMESTAMP;

public class MainActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener, CurrencyAdapter.UpdateCurrenciesFiltered {

    static final String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding binding;
    ArrayList<CurrencyDetails> currenciesFiltered = new ArrayList<>();
    private ArrayList<CurrencyDetails> currencies;
    private CurrencyAdapter adapter;
    private CurrencyRepository repository;
    private SharedPreferences settingsPref;
    private SearchView searchView;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settingsPref = PreferenceManager.getDefaultSharedPreferences(this);

        currencies = new ArrayList<>();
        adapter = new CurrencyAdapter(this, currencies);
        RecyclerView currencyRecycler = binding.currencyRecycler;
        currencyRecycler.setAdapter(adapter);
        if (this.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            currencyRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            currencyRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        }
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

        showLoadingViews();

        new Thread(() -> {
            repository = new CurrencyRepository(getApplication());
            currencies.clear();
            currencies.addAll(repository.getAllCurrencies());
            Log.d(TAG, "onStart:  currencies.size() " + currencies.size());

            if (currencies.size() == 0) {
                loadCurrencyData(repository);
            } else {
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    binding.dateView.setText(String.format(getString(R.string.timestamp_holder),
                            settingsPref.getString(DATA_TIMESTAMP, "")));
                    hideLoadingViews();
                });
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
                if (response.body() != null) {
                    currencies.clear();
                    currencies.addAll(response.body().getValute().values());
                    repository.insertAll(currencies);
                    Log.d(TAG, "CONVERT DATE: " + Utils.dateConverter(response.body()
                            .getTimestamp()));
                    settingsPref.edit().putString(DATA_TIMESTAMP,
                            Utils.dateConverter(response.body().getTimestamp())).apply();
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        binding.dateView.setText(String.format(getString(R.string.timestamp_holder),
                                Utils.dateConverter(response.body().getTimestamp())));
                        hideLoadingViews();
                    });
                } else {
                    runOnUiThread(() -> showLoadErrorStatus());
                }
                binding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<CurrencyResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                binding.swipeRefresh.setRefreshing(false);
                if (currencies.size() == 0) {
                    if (isNetworkConnected()) {
                        runOnUiThread(() -> showLoadErrorStatus());
                    } else {
                        runOnUiThread(() -> showNetworkErrorStatus());
                    }
                } else {
                    String errorMsg = getString(R.string.error_network_msg);
                    if (isNetworkConnected()) {
                        errorMsg = getString(R.string.error_status_msg);
                    }
                    String finalErrorMsg = errorMsg;
                    runOnUiThread(() ->
                            Snackbar
                                    .make(binding.getRoot(),
                                            finalErrorMsg,
                                            Snackbar.LENGTH_LONG)
                                    .show());
                }
            }
        });
    }

    public void openConverter(View view) {
        startActivity(new Intent(this, ConverterActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null)
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchItem = menu.findItem(R.id.menu_search);
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

    private void showLoadingViews() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.statusView.setText(R.string.load_data_msg);
        binding.statusView.setVisibility(View.VISIBLE);
        binding.currencyRecycler.setVisibility(View.INVISIBLE);
        binding.currencyConverter.setVisibility(View.INVISIBLE);
        binding.dateView.setVisibility(View.INVISIBLE);
        if (searchItem != null) searchItem.setVisible(false);
    }

    private void hideLoadingViews() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.statusView.setVisibility(View.INVISIBLE);
        binding.currencyRecycler.setVisibility(View.VISIBLE);
        binding.currencyConverter.setVisibility(View.VISIBLE);
        if (!settingsPref.getString(DATA_TIMESTAMP, "").isEmpty()) {
            binding.dateView.setVisibility(View.VISIBLE);
        }
        if (searchItem != null) searchItem.setVisible(true);
    }

    private void showLoadErrorStatus() {
        binding.progressBar.setVisibility(View.GONE);
        binding.statusView.setText(R.string.error_status_msg);
        binding.statusView.setVisibility(View.VISIBLE);
        binding.currencyRecycler.setVisibility(View.INVISIBLE);
        binding.currencyConverter.setVisibility(View.INVISIBLE);
        binding.dateView.setVisibility(View.INVISIBLE);
        if (searchItem != null) searchItem.setVisible(false);
    }

    private void showNetworkErrorStatus() {
        binding.progressBar.setVisibility(View.GONE);
        binding.statusView.setText(R.string.error_network_msg);
        binding.statusView.setVisibility(View.VISIBLE);
        binding.currencyRecycler.setVisibility(View.INVISIBLE);
        binding.currencyConverter.setVisibility(View.INVISIBLE);
        binding.dateView.setVisibility(View.INVISIBLE);
        if (searchItem != null) searchItem.setVisible(false);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        adapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void updateCurrenciesFiltered(ArrayList<CurrencyDetails> currenciesFiltered) {
        this.currenciesFiltered = currenciesFiltered;
        if (currenciesFiltered.size() == 0) {
            binding.statusView.setText(R.string.not_found_msg);
            binding.statusView.setVisibility(View.VISIBLE);
            binding.dateView.setVisibility(View.INVISIBLE);
            binding.currencyConverter.setVisibility(View.INVISIBLE);
        } else {
            binding.statusView.setText("");
            binding.statusView.setVisibility(View.INVISIBLE);
            binding.dateView.setVisibility(View.VISIBLE);
            binding.currencyConverter.setVisibility(View.VISIBLE);
        }
    }
}