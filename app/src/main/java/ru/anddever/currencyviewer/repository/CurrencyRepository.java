package ru.anddever.currencyviewer.repository;

import android.content.Context;

import java.util.List;

import ru.anddever.currencyviewer.model.CurrencyDao;
import ru.anddever.currencyviewer.model.CurrencyDatabase;
import ru.anddever.currencyviewer.model.CurrencyDetails;

/**
 * Repository layer manages queries and allows to use multiple backends
 */
public class CurrencyRepository {

    private final CurrencyDao mCurrencyDao;
    private final List<CurrencyDetails> mCurrencies;

    public CurrencyRepository(Context application) {
        CurrencyDatabase db = CurrencyDatabase.getDatabase(application);
        mCurrencyDao = db.currencyDao();
        mCurrencyDao.getAll();
        mCurrencies = mCurrencyDao.getAll();
    }

    public List<CurrencyDetails> getAllCurrencies() {
        return mCurrencies;
    }

    public void insertAll(List<CurrencyDetails> currencyDetails) {
        CurrencyDatabase.databaseWriteExecutor.execute(() ->
                mCurrencyDao.insertAll(currencyDetails));
    }
}