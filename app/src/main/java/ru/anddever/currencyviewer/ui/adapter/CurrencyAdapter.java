package ru.anddever.currencyviewer.ui.adapter;

import static ru.anddever.currencyviewer.utils.ConstantsKt.SELECTED_CURRENCY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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


/**
 * Adapter class for mapping currencies as list in recycler view
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>
        implements Filterable {

    private final Context context;
    private final ArrayList<CurrencyDetails> currencies;
    private ArrayList<CurrencyDetails> currenciesFiltered;
    private final ArrayList<CurrencyDetails> filteredCurrencies = new ArrayList<>();

    public CurrencyAdapter(Context context, ArrayList<CurrencyDetails> currencies) {
        this.context = context;
        this.currencies = currencies;
        this.currenciesFiltered = currencies;
        ((UpdateCurrenciesFiltered) context).updateCurrenciesFiltered(currenciesFiltered);
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
        CurrencyDetails currency = currenciesFiltered.get(position);
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
        return currenciesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty())
                    currenciesFiltered = currencies;
                else {
                    filteredCurrencies.clear();
                    for (CurrencyDetails currency : currencies)
                        if (currency.getName().toLowerCase().contains(constraint.toString()
                                .toLowerCase()))
                            filteredCurrencies.add(currency);
                    currenciesFiltered = filteredCurrencies;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = currenciesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                currenciesFiltered = (ArrayList<CurrencyDetails>) results.values;
                ((UpdateCurrenciesFiltered) context).updateCurrenciesFiltered(currenciesFiltered);
                notifyDataSetChanged();
            }
        };
    }

    public interface UpdateCurrenciesFiltered {
        void updateCurrenciesFiltered(ArrayList<CurrencyDetails> currenciesFiltered);
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