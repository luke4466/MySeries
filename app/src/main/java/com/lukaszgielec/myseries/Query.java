package com.lukaszgielec.myseries;

import android.os.AsyncTask;
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
 * Created by Łukasz Gielec on 02.02.2017.
 */

public class Query {

    private int mCurrentPage = 0;
    private int mAllPage = 0;
    private int mAllResults = 0;

    final static String mURL = "http://www.omdbapi.com/?s=";

    private String mTitle;


    public Query(String mTitle) {
        this.mTitle = mTitle;
    }



    public class SearchInOmdb extends AsyncTask<String, Void, ArrayList<Series>> {


        private ArrayList<Series> mSeriesListFromServer = new ArrayList<>();

        @Override
        protected ArrayList<Series> doInBackground(String... params) {

            return getSeriesListFromServer(params[0], Integer.parseInt(params[1]));
        }

        private ArrayList<Series> getSeriesListFromServer(String query, int page) {
            URL url;
            HttpURLConnection httpURLConnection;
            String response;

            try {
                url = getUrl(query, page);
                httpURLConnection = makeConnection(url);
                response = getResponse(httpURLConnection);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("Response").equals("True")) {
                    mAllResults = jsonObject.getInt("totalResults");
                    mAllPage = (int) Math.floor((double)(mAllResults/10));
                    Log.i("Query","mAllPage: "+mAllPage);

                    JSONArray jsonArray = jsonObject.getJSONArray("Search");
                    JSONArrayToSeriesArray(jsonArray);
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mSeriesListFromServer;
        }

        private URL getUrl(String query, int page) throws IOException {
            String finalURL = mURL + query + "&page=" + page + "&type=series";
            finalURL = finalURL.replaceAll(" ", "%20");
            return new URL(finalURL);
        }

        private HttpURLConnection makeConnection(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
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

        private void JSONArrayToSeriesArray(JSONArray jsonArray) throws JSONException {

            for (int i = 0; i < jsonArray.length(); i++) {
                Series series = new Series();
                series.setTitle(jsonArray.getJSONObject(i).getString("Title"));
                series.setYear(jsonArray.getJSONObject(i).getString("Year"));


                mSeriesListFromServer.add(series);
            }

        }


        @Override
        protected void onPostExecute(ArrayList<Series> series) {

        }
    }


}
