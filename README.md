# Conversor de Monedas (Java 11 + HttpClient + Gson + ExchangeRate‑API)

Aplicación de consola que convierte importes entre monedas usando **Java 11 HttpClient**, 
**Gson** para parseo/manipulación de JSON y la **ExchangeRate‑API** como fuente de tasas.

> Pensada para ser importada en **IntelliJ IDEA** y ejecutarse desde consola. Incluye un 
> menú interactivo con `Scanner` para listar, filtrar y convertir monedas.

---

## Características
- ✅ Cliente HTTP moderno con `java.net.http.HttpClient`, `HttpRequest`, `HttpResponse` (Java 11)
- ✅ Consumo de endpoints `/latest/{BASE}` y `/pair/{FROM}/{TO}/{AMOUNT}` de ExchangeRate‑API
- ✅ Parseo de JSON a POJO y **filtrado de monedas** usando **Gson** (modo árbol `JsonObject`)
- ✅ Conversión precisa con `BigDecimal`
- ✅ Menú por consola (listar, filtrar, convertir, cambiar base)

---

## Requisitos
- **JDK 11+**
- **Maven 3.6+** (o Gradle si decides adaptarlo)
- **Cuenta y API Key** de [ExchangeRate‑API](https://www.exchangerate-api.com/)

> Define tu llave en la variable de entorno `EXCHANGERATE_API_KEY`.

---

## Estructura del proyecto
```
.
├─ pom.xml
├─ README.md
└─ src/main/java/com/example/currency/
   ├─ Main.java                        # interfaz de consola (Scanner + menú)
   ├─ ExchangeRateApiClient.java       # HttpClient + llamadas a la API
   ├─ CurrencyConverterService.java    # lógica de negocio
   └─ dto/
      ├─ RatesResponse.java            # mapea /latest (conversion_rates)
      └─ PairConversionResponse.java   # mapea /pair (conversion_rate/result)
```

---

## Configuración de la API Key
- **macOS/Linux**
  ```bash
  export EXCHANGERATE_API_KEY=TU_LLAVE
  ```
- **Windows PowerShell**
  ```powershell
  $env:EXCHANGERATE_API_KEY = 'TU_LLAVE'
  ```

> En IntelliJ: *Run → Edit Configurations → Environment variables* → agrega `EXCHANGERATE_API_KEY`.

---

## Compilar y ejecutar con Maven
```bash
mvn -q -DskipTests package
java -cp target/currency-converter-1.0.0.jar com.example.currency.Main
```

> Alternativa desde IntelliJ: clic derecho sobre `Main` → **Run 'Main.main()'**.

---

## Cómo usar (menú)
1. **Listar códigos soportados**: muestra todos los códigos ISO 4217 disponibles para la base actual.
2. **Filtrar códigos**: ingresa un texto (p. ej. `U` o `EU`) y se listan las monedas que contienen ese texto.
3. **Convertir monto**: ingresa *origen*, *destino* y *monto*; se consulta `/pair/{FROM}/{TO}/{AMOUNT}`.
4. **Cambiar base**: establece la base utilizada para el listado/filtrado (por defecto `USD`).

### Ejemplos
- Filtrar con base `USD` y texto `EU` → podría devolver `EUR`, `SEK` no coincide, etc.
- Convertir **100 USD → EUR**
- Convertir **250000 COP → USD** (útil en un ecommerce localizado)

---

## Endpoints utilizados
- **Standard (latest)**: `GET https://v6.exchangerate-api.com/v6/{API_KEY}/latest/{BASE}`  
  Respuesta con `base_code` y objeto `conversion_rates` (mapa de moneda→tasa).
- **Pair conversion**: `GET https://v6.exchangerate-api.com/v6/{API_KEY}/pair/{FROM}/{TO}/{AMOUNT}`  
  Respuesta con `conversion_rate` y `conversion_result`.

> Consulta la documentación oficial para formatos y errores (`invalid-key`, `quota-reached`, etc.):  
> https://www.exchangerate-api.com/docs/standard-requests  
> https://www.exchangerate-api.com/docs/pair-conversion-requests

---

## Notas técnicas
- **HttpClient/HttpRequest/HttpResponse**: API estándar incorporada desde Java 11 (sin librerías extra).  
  Documentación: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html
- **Gson**: utilizado en dos modos: (1) **POJO** (DTOs) y (2) **árbol JSON** (`JsonParser`, `JsonObject`) para filtrar claves.  
  Guía: https://google.github.io/gson/
- **Precisión**: se usa `BigDecimal` para montos/tasas; evita errores por punto flotante.

---

## Manejo de errores comunes
- `HTTP 401/403` o `result = error` → revisa `EXCHANGERATE_API_KEY` y tu plan/cuota.
- `unsupported-code` → código de moneda no soportado por la API.
- `NumberFormatException` → el monto no es numérico; ingresa valores válidos (p. ej. `1234.56`).
- Problemas de red → verifica conectividad o configura un timeout mayor en `HttpClient`.

---

## Personalización / próximos pasos
- Cachear la respuesta de `/latest/{BASE}` para reducir llamadas.
- Añadir validación de códigos ISO‑4217 (lista blanca).
- Empaquetar como **microservicio** (Spring Boot) con endpoint `/convert` y **caché**.
- Añadir **Test** con JUnit/Mockito y pruebas de integración con **Testcontainers** (si se integra BD).

---

## Licencia
Define la licencia que aplique a tu proyecto (por ejemplo, MIT, Apache‑2.0, etc.).

---

## Créditos y recursos
- Oracle Java 11 HttpClient: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html
- ExchangeRate‑API Docs: https://www.exchangerate-api.com/docs
- Gson User Guide: https://google.github.io/gson/

