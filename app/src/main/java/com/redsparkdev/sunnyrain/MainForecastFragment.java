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
    private final String LOG_TAG = MainForecastFragment.class.getSimpleName();
    private ArrayAdapter<String> mForecastAdapter;

    private String [] forecastArray = {//Just an initial array
            "Loading forecast",
            "Loading forecast",
            "Loading forecast",
            "Loading forecast",
            "Loading forecast",
    };
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
            fetchWeatherTask.updateWeatherData("210077", this);//refreshes weather data(this is only for testing)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));


         mForecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_text_view,
                weekForecast);

        View rootView = inflater.inflate(R.layout.forecast_fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }
    //method to update the mForecastAdapter
    public void updateForecastAdapter(String [] s){
        mForecastAdapter.clear();
        mForecastAdapter.addAll(s);
    }

}



