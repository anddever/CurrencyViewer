package ru.anddever.currencyviewer.model;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

/**
 * Class for converting BigDecimal to String and vice versa for storing currency in database
 */
public class Converters {

    @TypeConverter
    public BigDecimal fromString(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @TypeConverter
    public String toString(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        } else {
            return bigDecimal.toPlainString();
        }
    }
}