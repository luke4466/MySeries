package com.lukaszgielec.myseries;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Łukasz Gielec on 05.03.2017.
 */

public class CloudDatabase {



    public ArrayList<Series> search(String title, int page){
        ArrayList<Series> mSeriesList = new ArrayList<>();
        String mURL = "http://www.omdbapi.com/?s=";
        URL url;
        HttpURLConnection httpURLConnection;
        String response;

        try {
            String finalURL = mURL + title + "&page=" + page + "&type=series";
            finalURL = finalURL.replaceAll(" ", "%20");
            url = new URL(finalURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            response = getResponse(httpURLConnection);
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("Response").equals("True")) {
                JSONArray jsonArray = jsonObject.getJSONArray("Search");
                mSeriesList.addAll(JSONArrayToSeriesArray(jsonArray));
                return mSeriesList;

            }else return mSeriesList;


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return mSeriesList;
    }

    private String getResponse(HttpURLConnection httpURLConnection) throws IOException {
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }
            input.close();
            httpURLConnection.disconnect();
            return response.toString();
        }

        return "null";

    }

    private ArrayList<Series> JSONArrayToSeriesArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Series> seriesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Series series = new Series();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            series.setTitle(jsonObject.getString("Title"));
            series.setYear(jsonObject.getString("Year"));
            series.setImdbID(jsonObject.getString("imdbID"));
            series.setPosterURL(jsonObject.getString("Poster"));

            jsonObject = searchByID(series.getImdbID());
            series.setReleasedDate(jsonObject.getString("Released"));
            series.setRuntime(jsonObject.getString("Runtime"));
            series.setGenre(jsonObject.getString("Genre"));
            series.setDirector(jsonObject.getString("Director"));
            series.setWriter(jsonObject.getString("Writer"));
            series.setActors(jsonObject.getString("Actors"));
            series.setFullPlot(jsonObject.getString("Plot"));
            series.setLanguage(jsonObject.getString("Language"));
            series.setCountry(jsonObject.getString("Country"));
            series.setAwards(jsonObject.getString("Awards"));
            series.setImdbRating(jsonObject.getString("imdbRating"));
            series.setImdbVotes(jsonObject.getString("imdbVotes"));
            series.setTotalSeasons(jsonObject.getString("totalSeasons"));

            seriesList.add(series);
        }

        return seriesList;

    }

    private JSONObject searchByID(String mID){
        String mURL = "http://www.omdbapi.com/?i=";
        URL url;
        HttpURLConnection httpURLConnection;
        String response;

        try {
            String finalURL = mURL + mID + "&plot=full";
            url = new URL(finalURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            response = getResponse(httpURLConnection);
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("Response").equals("True")) {

                return jsonObject;

            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
