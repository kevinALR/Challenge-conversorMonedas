/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package main.challengeconversor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Scanner;
/**
 *
 * @author Kevin
 */
public class ChallengeConversor {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/a16310535d26bb0f485fbbf9/latest/USD";

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");

                Scanner scanner = new Scanner(System.in);

                while (true) {
                    System.out.println("Bienvenido al conversor de monedas");
                    System.out.println("1. Convertir moneda");
                    System.out.println("2. Salir");
                    System.out.print("Seleccione una opción: ");
                    int option = scanner.nextInt();

                    if (option == 2) {
                        System.out.println("Gracias por usar el conversor de monedas.");
                        break;
                    } else if (option == 1) {
                        System.out.print("Ingrese la cantidad a convertir: ");
                        double amount = scanner.nextDouble();

                        System.out.print("Ingrese el código de la moneda de origen (ej. USD): ");
                        String fromCurrency = scanner.next().toUpperCase();

                        System.out.print("Ingrese el código de la moneda de destino (ej. EUR): ");
                        String toCurrency = scanner.next().toUpperCase();

                        if (rates.has(fromCurrency) && rates.has(toCurrency)) {
                            double fromRate = rates.get(fromCurrency).getAsDouble();
                            double toRate = rates.get(toCurrency).getAsDouble();

                            double convertedAmount = (amount / fromRate) * toRate;
                            System.out.printf("La cantidad de %.2f %s es igual a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
                        } else {
                            System.out.println("Código de moneda no válido. Inténtelo de nuevo.");
                        }
                    } else {
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                    }
                }
                scanner.close();
            } else {
                System.err.println("Error al obtener las tasas de cambio. Código de estado: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al realizar la solicitud: " + e.getMessage());
        }
    }
}
