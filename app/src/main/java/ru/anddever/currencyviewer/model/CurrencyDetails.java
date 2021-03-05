package ru.anddever.currencyviewer.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.math.BigDecimal;

@Keep
public class CurrencyDetails {

    private String ID = "";
    private String NumCode = "";
    private String CharCode = "";
    private int Nominal;
    private String Name = "";
    private BigDecimal Value = new BigDecimal(0);
    private BigDecimal Previous = new BigDecimal(0);

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNumCode() {
        return NumCode;
    }

    public void setNumCode(String numCode) {
        NumCode = numCode;
    }

    public String getCharCode() {
        return CharCode;
    }

    public void setCharCode(String charCode) {
        CharCode = charCode;
    }

    public int getNominal() {
        return Nominal;
    }

    public void setNominal(int nominal) {
        Nominal = nominal;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public BigDecimal getValue() {
        return Value;
    }

    public void setValue(BigDecimal value) {
        Value = value;
    }

    public BigDecimal getPrevious() {
        return Previous;
    }

    public void setPrevious(BigDecimal previous) {
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