package ru.anddever.currencyviewer.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Data class for mapping json response to java object
 */
@Keep
public class CurrencyResponse {

    private String Date = "";
    private String PreviousDate = "";
    private String PreviousURL = "";
    private String Timestamp = "";
    private Map<String, CurrencyDetails> Valute = new HashMap<>();

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPreviousDate() {
        return PreviousDate;
    }

    public void setPreviousDate(String previousDate) {
        PreviousDate = previousDate;
    }

    public String getPreviousURL() {
        return PreviousURL;
    }

    public void setPreviousURL(String previousURL) {
        PreviousURL = previousURL;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public Map<String, CurrencyDetails> getValute() {
        return Valute;
    }

    public void setValute(Map<String, CurrencyDetails> valute) {
        Valute = valute;
    }

    @NonNull
    @Override
    public String toString() {
        return "CurrencyResponse{" +
                "Date='" + Date + '\'' +
                ", PreviousDate='" + PreviousDate + '\'' +
                ", PreviousURL='" + PreviousURL + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                ", Valute=" + Valute +
                '}';
    }
}
