package ru.anddever.currencyviewer.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

/**
 * Data class for mapping json response to java object and using as database entity
 */
@Keep
@Entity(tableName = "currencies")
public class CurrencyDetails {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String ID = "";

    @NonNull
    @ColumnInfo(name = "num_code")
    private String NumCode = "";

    @NonNull
    @ColumnInfo(name = "char_code")
    private String CharCode = "";

    @ColumnInfo(name = "nominal")
    private int Nominal;

    @NonNull
    @ColumnInfo(name = "name")
    private String Name = "";

    @NonNull
    @ColumnInfo(name = "value")
    private BigDecimal Value = new BigDecimal(0);

    @NonNull
    @ColumnInfo(name = "previous")
    private BigDecimal Previous = new BigDecimal(0);

    @NonNull
    public String getID() {
        return ID;
    }

    public void setID(@NonNull String ID) {
        this.ID = ID;
    }

    @NonNull
    public String getNumCode() {
        return NumCode;
    }

    public void setNumCode(@NonNull String numCode) {
        NumCode = numCode;
    }

    @NonNull
    public String getCharCode() {
        return CharCode;
    }

    public void setCharCode(@NonNull String charCode) {
        CharCode = charCode;
    }

    public int getNominal() {
        return Nominal;
    }

    public void setNominal(int nominal) {
        Nominal = nominal;
    }

    @NonNull
    public String getName() {
        return Name;
    }

    public void setName(@NonNull String name) {
        Name = name;
    }

    @NonNull
    public BigDecimal getValue() {
        return Value;
    }

    public void setValue(@NonNull BigDecimal value) {
        Value = value;
    }

    @NonNull
    public BigDecimal getPrevious() {
        return Previous;
    }

    public void setPrevious(@NonNull BigDecimal previous) {
        Previous = previous;
    }

    @NonNull
    @Override
    public String toString() {
        return "CurrencyDetails{" +
                "ID='" + ID + '\'' +
                ", NumCode='" + NumCode + '\'' +
                ", CharCode='" + CharCode + '\'' +
                ", Nominal=" + Nominal +
                ", Name='" + Name + '\'' +
                ", Value=" + Value +
                ", Previous=" + Previous +
                '}';
    }
}