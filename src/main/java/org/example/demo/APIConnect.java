package org.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIConnect {
    private final String location;
    private static final String API_KEY = "7a3d8212074948fd91c43852241003";
    private static final String API_URL = "https://api.weatherapi.com/v1/";
    public APIConnect(String location){
        this.location = location;
    }
    protected String getWeatherData() throws IOException {
        String finallocation = location.replaceAll("\\s+","%20");
        String apiUrl = API_URL + "current.json?key=" + API_KEY +  "&q=" + finallocation + "&aqi=no";
        return Data(apiUrl);
    }

    protected String Data(String apiURL) throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    protected String getForecastData() throws IOException {
        String finallocation = location.replaceAll("\\s+","%20");
        String apiUrl = API_URL + "forecast.json?key=" + API_KEY +  "&q=" + finallocation + "&days=3&aqi=no&alerts=no";
        return Data(apiUrl);
    }

}