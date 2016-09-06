package com.redsparkdev.sunnyrain;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
            FetchWeather fetchWeatherTask = new FetchWeather();
            fetchWeatherTask.execute("210077");
        }

        return super.onOptionsItemSelected(item);
    }

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


        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_text_view,
                weekForecast);

        View rootView = inflater.inflate(R.layout.forecast_fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

}


