package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    private static final String API_KEY = "282ab73e7f751422b27c8da2";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            mostrarMenu();
            System.out.print("Elija una opción válida: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 7) {
                System.out.println("Saliendo...");
                break;
            }

            String baseCurrency = "";
            String targetCurrency = "";

            switch (opcion) {
                case 1:
                    baseCurrency = "USD";
                    targetCurrency = "ARS";
                    break;
                case 2:
                    baseCurrency = "ARS";
                    targetCurrency = "USD";
                    break;
                case 3:
                    baseCurrency = "USD";
                    targetCurrency = "BRL";
                    break;
                case 4:
                    baseCurrency = "BRL";
                    targetCurrency = "USD";
                    break;
                case 5:
                    baseCurrency = "USD";
                    targetCurrency = "COP";
                    break;
                case 6:
                    baseCurrency = "COP";
                    targetCurrency = "USD";
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    continue;
            }

            System.out.print("Introduce la cantidad a convertir: ");
            double amount = scanner.nextDouble();

            try {
                double rate = obtenerTasaDeCambio(baseCurrency, targetCurrency);
                if (rate != -1) {
                    double result = rate * amount;
                    System.out.println("El valor de " + amount + " " + "[" + baseCurrency + "]" + " corresponde al valor final de  ==> "  + result + " " + "[" + targetCurrency + "]");
                } else {
                    System.out.println("Error obteniendo la tasa de cambio. Verifica las monedas.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    public static void mostrarMenu() {

        String Menu = """
                      ****************************************************
                      Sea bienvenido/a al Conversor de Moneda 

                      1) Dólar  ==> Peso argentino
                      2) Peso argentino  ==> Dólar
                      3) Dólar  ==> Real brasileño
                      4) Real brasileño  ==> Dólar
                      5) Dólar  ==> Peso colombiano
                      6) Peso colombiano  ==> Dólar
                      7) Salir
                """;

        System.out.println(Menu);
    }

    public static double obtenerTasaDeCambio(String base, String destino) throws Exception {
        String requestUrl = API_URL + base + "/" + destino;

        URL url = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        if (status != 200) {
            System.out.println("Error: No se pudo obtener la tasa de cambio.");
            return -1;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();
        if (jsonResponse.get("result").getAsString().equals("success")) {
            return jsonResponse.get("conversion_rate").getAsDouble();
        } else {
            return -1;
        }
    }
}