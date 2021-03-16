package ru.anddever.currencyviewer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import ru.anddever.currencyviewer.R;
import ru.anddever.currencyviewer.model.CurrencyDetails;
import ru.anddever.currencyviewer.ui.ConverterActivity;

import static ru.anddever.currencyviewer.utils.Constants.SELECTED_CURRENCY;

/**
 * Adapter class for mapping currencies as list in recycler view
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private final Context context;
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
        holder.currencyCharCode.setText(String.format(Locale.getDefault(), "%s %d",
                currency.getCharCode(), currency.getNominal()));
        holder.currencyValue.setText(String.format("%s ₽", currency.getValue()));
        BigDecimal diff = currency.getValue().subtract(currency.getPrevious());
        String sign = "+";
        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            sign = "";
        }
        holder.currencyPrev.setText(String.format("%s%s", sign, diff));
        if (currency.getValue().compareTo(currency.getPrevious()) < 0) {
            holder.growArrow.setImageResource(R.drawable.ic_baseline_arrow_downward);
        } else {
            holder.growArrow.setImageResource(R.drawable.ic_baseline_arrow_upward);
        }
        holder.itemView.setOnClickListener(v ->
                context.startActivity(new Intent(context, ConverterActivity.class)
                        .putExtra(SELECTED_CURRENCY, holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private final TextView currencyName;
        private final TextView currencyCharCode;
        private final TextView currencyValue;
        private final TextView currencyPrev;
        private final ImageView growArrow;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.currency_name);
            currencyCharCode = itemView.findViewById(R.id.currency_char_code);
            currencyValue = itemView.findViewById(R.id.currency_value);
            currencyPrev = itemView.findViewById(R.id.currency_prev);
            growArrow = itemView.findViewById(R.id.grow_arrow);
        }
    }
}