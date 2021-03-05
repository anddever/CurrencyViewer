package ru.anddever.currencyviewer.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerApi {

    String DAILY_JSON = "daily_json.js";

    @GET(DAILY_JSON)
    Call<Object> getCurrency();
}