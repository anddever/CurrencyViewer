package ru.anddever.currencyviewer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.anddever.currencyviewer.R;
import ru.anddever.currencyviewer.databinding.ActivityConverterBinding;

public class ConverterActivity extends AppCompatActivity {

    private ActivityConverterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}