package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WeatherControl implements Initializable{

    // Variables
    public ToggleButton fahrenClick;
    public ToggleButton celsClick;

    public Button forecastClick;

    //    Data in format as (City, Temperature, Humidity, Wind Speed, Conditions)
    @FXML
    public Button buttonExample;
    public TextField inputText;
    @FXML
    private TextArea welcomeText;

    //Data Analyze and Parsing to Action Buttons
    public String[] weatherControl(String location) throws IOException {
        String weatherData = weatherDataAPI(location);
        JSONObject json = new JSONObject(weatherData);
        JSONObject conditions = json.getJSONObject("current");
        String weatherConditions = conditions.getJSONObject("condition").getString("text");
        double temperature = json.getJSONObject("current").getDouble("temp_c");
        double temperature_f = json.getJSONObject("current").getDouble("temp_f");
        double humidity = json.getJSONObject("current").getDouble("humidity");
        double windSpeed = json.getJSONObject("current").getDouble("wind_mph");
        return new String[]{String.valueOf(temperature), String.valueOf(humidity), String.valueOf(windSpeed), String.valueOf(temperature_f), weatherConditions};
    }

    public String[] forecastControl(String location) throws IOException {
        String forecastValue = forecastDataAPI(location);
        JSONObject json = new JSONObject(forecastValue);
        JSONObject forecastJSON = json.getJSONObject("forecast");
        JSONArray foreCastDay = (JSONArray) forecastJSON.get("forecastday");
        //Data for Day1 Forecast
        JSONObject day1 = foreCastDay.getJSONObject(0);
        JSONObject conditions = day1.getJSONObject("day");
        String weatherCondition1 = conditions.getJSONObject("condition").getString("text");
        double temp1 = day1.getJSONObject("day").getDouble("maxtemp_c");
        String date1 = day1.getString("date");
        //Data for Day2 Forecast
        JSONObject day2 = foreCastDay.getJSONObject(1);
        JSONObject conditions2 = day2.getJSONObject("day");
        String weatherCondition2 = conditions.getJSONObject("condition").getString("text");
        double temp2 = day2.getJSONObject("day").getDouble("maxtemp_c");
        String date2 = day2.getString("date");
        //Data for Day3 Forecast
        JSONObject day3 = foreCastDay.getJSONObject(2);
        JSONObject conditions3 = day3.getJSONObject("day");
        String weatherCondition3 = conditions3.getJSONObject("condition").getString("text");
        double temp3 = day3.getJSONObject("day").getDouble("maxtemp_c");
        String date3 = day3.getString("date");
        //Format(Date, Temperature, Conditions)
        return new String[]{date1, String.valueOf(temp1), weatherCondition1, date2, String.valueOf(temp2), weatherCondition2, date3, String.valueOf(temp3), weatherCondition3};
    }

    //Button Actions onAction Methods
    @FXML
    protected void onHelloButtonClick(ActionEvent ae) throws IOException{
        if(Objects.equals(inputText.getText(),"")){
            welcomeText.setText("Enter the Valid Location");
        }
        else {
            String [] weatherData = weatherControl(inputText.getText());
            printDataCels(weatherData);
        }
    }
    @FXML
    protected void fahrenTemp(ActionEvent ae) throws IOException {
        String [] weatherData = weatherControl(inputText.getText());
        printDataFahren(weatherData);
    }
    @FXML
    protected void celsTemp(ActionEvent ae) throws IOException {
        onHelloButtonClick(ae);
    }

    @FXML
    protected void forecastData(ActionEvent ae) throws IOException {
        if(Objects.equals(inputText.getText(),"")){
            welcomeText.setText("Enter the Valid Location");
        }
        else {
            String[] forecastDataValue = forecastControl(inputText.getText());
            printForecastData(forecastDataValue);
        }
    }

    //Print on the Application Methods
    protected void printForecastData(String[] forecastDataValue) {
        welcomeText.setText(
                "Weather details for: " + inputText.getText() + "\n\n" +
                        "Forecast for Day 1 \n"+
                        "Date: " + forecastDataValue[0] + "\n" +
                        "Temperature: " + forecastDataValue[1] + "°C\n" +
                        "Weather Conditions: " + forecastDataValue[2] + "\n\n" +
                        "Forecast for Day 2 \n"+
                        "Date: " + forecastDataValue[3] + "\n" +
                        "Temperature: " + forecastDataValue[4] + "°C\n" +
                        "Weather Conditions: " + forecastDataValue[5] + "\n\n" +
                        "Forecast for Day 3 \n"+
                        "Date: " + forecastDataValue[6] + "\n" +
                        "Temperature: " + forecastDataValue[7] + "°C\n" +
                        "Weather Conditions: " + forecastDataValue[8] + "\n"
        );
    }
    protected void printDataCels(String[] cityWeather){
        welcomeText.setText(
                "Weather details for: " + inputText.getText() + "\n" +
                        "Temperature: " + cityWeather[0] + "°C\n" +
                        "Humidity: " + cityWeather[1] + "%\n" +
                        "Wind Speed: " + cityWeather[2] + " mph\n" +
                        "Weather Conditions: " + cityWeather[4] + "\n"
        );
    }

    protected void printDataFahren(String[] cityWeather){
        welcomeText.setText(
                "Weather details for: " + inputText.getText() + "\n" +
                        "Temperature: " + cityWeather[3] + "°F\n" +
                        "Humidity: " + cityWeather[1] + "%\n" +
                        "Wind Speed: " + cityWeather[2] + " mph\n"+
                        "Weather Conditions: " + cityWeather[4] + "\n"
        );
    }

    //Initialization of Action Buttons
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputText.setPromptText("Please enter city");
        buttonExample.setOnAction(ae -> {
            try {
                onHelloButtonClick(ae);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        fahrenClick.setOnAction(ae -> {
            try {
                fahrenTemp(ae);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        celsClick.setOnAction(ae -> {
            try {
                celsTemp(ae);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        forecastClick.setOnAction(ae -> {
            try {
                forecastData(ae);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //API Method for Weather Data and Forecast
    public String weatherDataAPI(String location) throws IOException {
        APIConnect weatherData = new APIConnect(location);
        return weatherData.getWeatherData();
    }
    public String forecastDataAPI(String location) throws IOException {
        APIConnect forecastDataStats = new APIConnect(location);
        return forecastDataStats.getForecastData();
    }
}