package ru.anddever.currencyviewer.network;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.anddever.currencyviewer.model.CurrencyResponse;

public interface ServerApi {

    String DAILY_JSON = "daily_json.js";

    @GET(DAILY_JSON)
    Call<CurrencyResponse> getCurrency();
}