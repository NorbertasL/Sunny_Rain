package com.redsparkdev.sunnyrain;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Red_Spark on 24/08/2016.
 */
public class MainForecastFragment extends Fragment {

    public MainForecastFragment(){}



    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setHasOptionsMenu(true);

    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecast_fragment, menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.acion_refresh) {
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            fetchWeatherTask.execute("210077");
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //Fake Data for testing
        String [] forecastArray = {
                    "Mon 08/22 - Sunny - 31/17",
                    "Tue 08/23 - Foggy - 21/8",
                    "Wed 08/24 - Cloudy - 22/17",
                    "Thurs 08/25 - Rainy - 18/11",
                    "Fri 08/26 - Foggy - 21/10",
                    "Sat 08/27 - Raining Meteors - 23/18",
                    "Sun 08/28 - Sunny - 20/7",

            };

        List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));

        ArrayAdapter<String> forecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast);

        View rootView = inflater.inflate(R.layout.forecast_fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forecastAdapter);

        return rootView;
    }
}
class FetchWeatherTask extends AsyncTask <String, Void, Void>{
    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();



    protected Void doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            String apyKey = "?apikey=mlc8g2kAcDSAmrPJWYXHbjyc9BXpKhwM";
            URL mainUrl = new URL(" http://dataservice.accuweather.com/");
            String locationParameter ="";
            String forecast5dayParameter = "forecasts/v1/daily/5day/";
            String locationID = params[0];
            String metricParameter = "&metric=true/";

            URL url = new URL(mainUrl.toString()+forecast5dayParameter+locationID+apyKey+metricParameter);

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

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Forecast JSON String:" +forecastJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
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
        return null;

}


}
