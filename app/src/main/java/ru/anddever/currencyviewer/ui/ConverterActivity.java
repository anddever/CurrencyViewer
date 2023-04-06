package ru.anddever.currencyviewer.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import ru.anddever.currencyviewer.databinding.ActivityConverterBinding;
import ru.anddever.currencyviewer.model.CurrencyDetails;
import ru.anddever.currencyviewer.repository.CurrencyRepository;

import static ru.anddever.currencyviewer.utils.ConstantsKt.SELECTED_CURRENCY;

public class ConverterActivity extends AppCompatActivity {

    static final String TAG = ConverterActivity.class.getSimpleName();
    private ActivityConverterBinding binding;
    CurrencyDetails selectedCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Thread(() -> {
            CurrencyRepository repository = new CurrencyRepository(getApplication());
            List<CurrencyDetails> currencies = new ArrayList<>(repository.getAllCurrencies());
            ArrayAdapter<CurrencyDetails> currencyNamesAdapter = new ArrayAdapter<>
                    (this, android.R.layout.simple_spinner_item, currencies);
            currencyNamesAdapter.
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.currencySpinner.setAdapter(currencyNamesAdapter);
            if (getIntent().getExtras() != null) {
                binding.currencySpinner.setSelection(getIntent()
                        .getIntExtra(SELECTED_CURRENCY, 0));
            }
        }).start();

        binding.currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrency = (CurrencyDetails) binding.currencySpinner.getSelectedItem();
                convertCurrency();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.amountInRublesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                convertCurrency();
            }
        });
    }

    /**
     * For converting currency - divide rubles by selected currency value and
     * multiply by nominal
     */
    void convertCurrency() {
        if (selectedCurrency != null &&
                !binding.amountInRublesEditText.getText().toString().isEmpty()) {
            try {
                BigDecimal rubs = new BigDecimal(binding.amountInRublesEditText
                        .getText().toString());
                BigDecimal result = rubs
                        // I doubt what rounding mode is more correct for this operation
                        .divide(selectedCurrency.getValue(), 4, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal.valueOf(selectedCurrency.getNominal()));
                binding.resultTv.setText(String.format("%s %s", result.toPlainString(),
                        selectedCurrency.getCharCode()));
            } catch (Exception e) {
                Log.e(TAG, "convertCurrency: ", e);
            }
        } else {
            binding.resultTv.setText("");
        }
    }
}