import java.math.BigDecimal;
import java.util.Map;

public class Response {

package com.example.currency.dto;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Map;

    public class RatesResponse {
        public String result;
        @SerializedName("base_code")
        public String baseCode;

        // Algunas respuestas antiguas o mirrors usan "rates"; la principal es "conversion_rates"
        @SerializedName(value = "conversion_rates", alternate = {"rates"})
        public Map<String, BigDecimal> conversionRates;

        @SerializedName("time_last_update_utc")
        public String timeLastUpdateUtc;

        @SerializedName("error-type")
        public String errorType;
    }

}
