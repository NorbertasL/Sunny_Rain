package com.redsparkdev.sunnyrain;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Red_Spark on 07/09/2016.
 */
class FetchWeather extends AsyncTask<String, Void, String[]> {
    private final String LOG_TAG = FetchWeather.class.getSimpleName();

    private static String formatHighLows(double high, double low) {//removed the decimal and formats
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    //formats the weather data
    public String[] getWeatherDataFromJson(String forecastJsonStr, int numDays) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        //We are using http://developer.accuweather.com/ for our api
        final String ACC_DAILY_FORECAST = "DailyForecasts";
        final String ABC_TEMPERATURE = "Temperature";
        final String ACC_MIN = "Minimum";
        final String ACC_MAX = "Maximum";
        final String ACC_VALUE = "Value";

        //string that stores all the formatted results from JSON

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray forecastArray = forecastJson.getJSONArray(ACC_DAILY_FORECAST);

        //create a Gregorian Calendar, which is in current date
        GregorianCalendar gc = new GregorianCalendar();

        String[] resultStrs = new String[numDays];
        for(int i = 0; i < forecastArray.length(); i++) {
            String day, highAndLow;
            JSONObject dayForecast = forecastArray.getJSONObject(i);

            //add i dates to current date of calendar
            gc.add(GregorianCalendar.DATE, i);
            //get that date, format it, and "save" it on variable day
            Date time = gc.getTime();
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            day = shortenedDateFormat.format(time);

            JSONObject temperatureObject = dayForecast.getJSONObject(ABC_TEMPERATURE);
            JSONObject highObject = temperatureObject.getJSONObject(ACC_MAX);
            JSONObject lowObject = temperatureObject.getJSONObject(ACC_MIN);
            double highValue = highObject.getDouble(ACC_VALUE);
            double lowValue = lowObject.getDouble(ACC_VALUE);

            highAndLow = formatHighLows(highValue, lowValue);
            resultStrs[i] = day +"-"+highAndLow;


        }
        for(String s : resultStrs){
            Log.v(LOG_TAG, "Forecast entry:"+s);
        }
        return resultStrs;

    }

    protected String[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;


        if(params.length == 0)
            return null;
        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            String apyKey = "?apikey=mlc8g2kAcDSAmrPJWYXHbjyc9BXpKhwM";
            URL mainUrl = new URL(" http://dataservice.accuweather.com/");
            String locationParameter = "";
            String forecast5dayParameter = "forecasts/v1/daily/5day/";
            String locationID = params[0];
            String metricParameter = "&metric=true/";

            URL url = new URL(mainUrl.toString() + forecast5dayParameter + locationID + apyKey + metricParameter);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            forecastJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Forecast JSON String:" + forecastJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getWeatherDataFromJson(forecastJsonStr, 5);
        }catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;

    }
}