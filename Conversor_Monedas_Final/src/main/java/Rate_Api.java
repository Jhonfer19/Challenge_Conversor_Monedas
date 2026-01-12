import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Rate_Api {

package com.example.currency;

import com.example.currency.dto.PairConversionResponse;
import com.example.currency.dto.RatesResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

    public class ExchangeRateApiClient {

        private static final String STANDARD_URL =
                "https://v6.exchangerate-api.com/v6/%s/latest/%s"; // /latest/{BASE}
        private static final String PAIR_URL =
                "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%s"; // /pair/{FROM}/{TO}/{AMOUNT}

        private final HttpClient http;
        private final Gson gson;
        private final String apiKey;

        public ExchangeRateApiClient(String apiKey) {
            this.http = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            this.gson = new Gson();
            this.apiKey = apiKey;
        }

        /** Obtiene el JSON bruto de /latest/{base}. */
        public String getLatestRatesJson(String base) throws IOException, InterruptedException {
            String url = String.format(STANDARD_URL, apiKey, base.toUpperCase());
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                throw new IOException("HTTP " + res.statusCode() + " - " + res.body());
            }
            return res.body();
        }

        /** Deserializa /latest/{base} a POJO. */
        public RatesResponse getLatestRates(String base) throws IOException, InterruptedException {
            String json = getLatestRatesJson(base);
            return gson.fromJson(json, RatesResponse.class);
        }

        /** Convierte directamente entre dos monedas usando /pair/{from}/{to}/{amount}. */
        public PairConversionResponse convertPair(String from, String to, BigDecimal amount)
                throws IOException, InterruptedException {

            String url = String.format(PAIR_URL, apiKey,
                    from.toUpperCase(), to.toUpperCase(), amount.stripTrailingZeros().toPlainString());

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                throw new IOException("HTTP " + res.statusCode() + " - " + res.body());
            }
            return gson.fromJson(res.body(), PairConversionResponse.class);
        }
    }

}
