package ru.anddever.currencyviewer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.anddever.currencyviewer.R;
import ru.anddever.currencyviewer.model.CurrencyDetails;
import ru.anddever.currencyviewer.ui.ConverterActivity;

import static ru.anddever.currencyviewer.utils.Constants.SELECTED_CURRENCY;

/**
 * Adapter class for mapping currencies as list in recycler view
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private Context context;
    private final ArrayList<CurrencyDetails> currencies;

    public CurrencyAdapter(Context context, ArrayList<CurrencyDetails> currencies) {
        this.context = context;
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item,
                parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        CurrencyDetails currency = currencies.get(position);
        holder.currencyName.setText(currency.getName());
        holder.currencyCharCode.setText(currency.getCharCode());
        holder.currencyValue.setText(String.valueOf(currency.getValue()));
        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ConverterActivity.class)
                    .putExtra(SELECTED_CURRENCY, holder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private final TextView currencyName;
        private final TextView currencyCharCode;
        private final TextView currencyValue;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.currency_name);
            currencyCharCode = itemView.findViewById(R.id.currency_char_code);
            currencyValue = itemView.findViewById(R.id.currency_value);
        }
    }
}