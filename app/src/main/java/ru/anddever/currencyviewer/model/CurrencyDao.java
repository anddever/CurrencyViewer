package ru.anddever.currencyviewer.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data access object class for SQL validation at compile-time and associates it with a method
 */
@Dao
public interface CurrencyDao {

    @Query("SELECT * FROM currencies")
    List<CurrencyDetails> getAll();

    @Query("SELECT * FROM currencies WHERE id = :id")
    CurrencyDetails findById(String id);

    @Insert
    void insertAll(CurrencyDetails... currencyDetails);

    @Query("DELETE FROM currencies")
    void clearCurrencies();
}