import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

public class CurrencyConverterService {

package com.example.currency;

import com.example.currency.dto.RatesResponse;
import com.google.gson.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

    /** Orquesta obtención de tasas, filtrado y conversiones. */
    public class CurrencyConverterService {

        private final ExchangeRateApiClient client;
        private final Gson gson = new Gson();

        public CurrencyConverterService(ExchangeRateApiClient client) {
            this.client = client;
        }

        /** Lista códigos soportados para una base dada, usando el POJO (Map<String, BigDecimal>). */
        public Set<String> listCodes(String base) throws IOException, InterruptedException {
            RatesResponse rr = client.getLatestRates(base);
            validateSuccess(rr.result, rr.errorType);
            return rr.conversionRates.keySet();
        }

        /**
         * Filtra monedas usando el árbol JSON de Gson (JsonObject),
         * demostrando manipulación y *filtrado con Gson*.
         */
        public List<String> filterCodesWithGsonTree(String base, String contains)
                throws IOException, InterruptedException {

            String json = client.getLatestRatesJson(base);
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            // La clave principal es "conversion_rates" en ExchangeRate-API
            JsonObject rates = root.getAsJsonObject("conversion_rates");
            if (rates == null) rates = root.getAsJsonObject("rates"); // alternativo

            String needle = contains.trim().toUpperCase(Locale.ROOT);

            List<String> filtered = new ArrayList<>();
            for (Map.Entry<String, JsonElement> e : rates.entrySet()) {
                String code = e.getKey();
                if (code.contains(needle)) {
                    filtered.add(code);
                }
            }
            Collections.sort(filtered);
            return filtered;
        }

        /**
         * Convierte entre dos monedas. Si from==base usaremos la tasa directa de /latest,
         * en caso general usamos el endpoint /pair para máxima precisión y simplicidad.
         */
        public BigDecimal convert(String from, String to, BigDecimal amount)
                throws IOException, InterruptedException {

            // Caso simple: usar /pair directamente.
            return client.convertPair(from, to, amount).conversionResult;
        }

        private static void validateSuccess(String result, String error) {
            if (!"success".equalsIgnoreCase(result)) {
                String msg = (error != null) ? error : "Error desconocido";
                throw new IllegalStateException("ExchangeRate-API: " + msg);
            }
        }
    }
``

}
