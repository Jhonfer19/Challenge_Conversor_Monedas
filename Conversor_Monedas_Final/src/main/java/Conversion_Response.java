import java.math.BigDecimal;

public class Conversion_Response {

package com.example.currency.dto;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

    public class PairConversionResponse {
        public String result;
        @SerializedName("base_code")
        public String baseCode;
        @SerializedName("target_code")
        public String targetCode;

        @SerializedName("conversion_rate")
        public BigDecimal conversionRate;

        @SerializedName("conversion_result")
        public BigDecimal conversionResult;

        @SerializedName("error-type")
        public String errorType;
    }

}
