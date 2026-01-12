
package com.example.currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        String apiKey = System.getenv("EXCHANGERATE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Falta la variable de entorno EXCHANGERATE_API_KEY");
            System.err.println("Configúrala en Run/Debug Configuration de IntelliJ.");
            System.exit(1);
        }

        Rate_Api.ExchangeRateApiClient apiClient = new ExchangeRateApiClient(apiKey);
        CurrencyConverterService service = new CurrencyConverterService(apiClient);

        Scanner sc = new Scanner(System.in);
        String base = "USD"; // base por defecto para listar/filtrar
        boolean running = true;

        while (running) {
            System.out.println("\n=== Conversor de Monedas ===");
            System.out.println("Base actual para listado/filtrado: " + base);
            System.out.println("1) Listar códigos soportados");
            System.out.println("2) Filtrar códigos (por texto, p.ej. 'U', 'EU')");
            System.out.println("3) Convertir monto");
            System.out.println("4) Cambiar base para listado/filtrado");
            System.out.println("0) Salir");
            System.out.print("Selecciona opción: ");

            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1": {
                        Set<String> codes = service.listCodes(base);
                        System.out.println("Monedas (" + codes.size() + "): " + codes);
                        break;
                    }
                    case "2": {
                        System.out.print("Texto a buscar (contiene): ");
                        String q = sc.nextLine().trim();
                        List<String> filtered = service.filterCodesWithGsonTree(base, q);
                        System.out.println("Filtradas (" + filtered.size() + "): " + filtered);
                        break;
                    }
                    case "3": {
                        System.out.print("Moneda origen (ej. USD): ");
                        String from = sc.nextLine().trim().toUpperCase(Locale.ROOT);
                        System.out.print("Moneda destino (ej. EUR): ");
                        String to = sc.nextLine().trim().toUpperCase(Locale.ROOT);
                        System.out.print("Monto: ");
                        String amountStr = sc.nextLine().trim();
                        BigDecimal amount = new BigDecimal(amountStr);

                        BigDecimal result = service.convert(from, to, amount);
                        System.out.printf("%s %s -> %s %s%n", amount.toPlainString(), from,
                                result.toPlainString(), to);
                        break;
                    }
                    case "4": {
                        System.out.print("Nueva base (ej. USD, EUR, COP): ");
                        base = sc.nextLine().trim().toUpperCase(Locale.ROOT);
                        break;
                    }
                    case "0": running = false; break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }

        System.out.println("¡Hasta pronto!");
    }
}
``
