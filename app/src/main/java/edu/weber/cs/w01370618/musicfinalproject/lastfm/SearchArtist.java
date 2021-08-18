package edu.weber.cs.w01370618.musicfinalproject.lastfm;

import android.app.Activity;
import android.os.AsyncTask;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * AsyncTask class used to retrieve the search results of the specified search value via the lastfm API.
 * @author Travis Viall
 */
public class SearchArtist extends AsyncTask<String, Integer, String> {

    private String rawJSON;
    private final String searchValue;
    private OnArtistListComplete mCallback;

    public interface OnArtistListComplete {
        void processResultsList(List<Artist> artists);
    }

    public void setOnArtistListComplete(OnArtistListComplete mCallback) {
        this.mCallback = mCallback;
    }

    //overridden constructor
    public SearchArtist(String searchValue) {
        this.searchValue = searchValue;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url =new URL("http://ws.audioscrobbler.com/2.0/?method=artist.search&artist=" + searchValue + "&api_key="
                    + Authorization.AUTH_TOKEN +"&format=json&limit=5");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();

            int statusCode = connection.getResponseCode();

            switch(statusCode) {
                case 200:
                case 201:
                    BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    rawJSON =bufferedReader.readLine();
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
        return rawJSON;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        List<Artist> artists;

        try {
            artists = parseJSON(result);

            if(artists != null) {
                if(mCallback != null) {
                    mCallback.processResultsList(artists);
                }
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    private List<Artist> parseJSON(String result) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List<Artist> artists =null;
        Type artistListType =new TypeToken<ArrayList<Artist>>(){}.getType();

        try {
            JSONObject searchObj = new JSONObject(result);
            JSONObject resultObj = searchObj.getJSONObject("results");
            JSONObject matchesOBj = resultObj.getJSONObject("artistmatches");
            JSONArray artistArray =matchesOBj.getJSONArray("artist");
            artists =gson.fromJson(String.valueOf(artistArray), artistListType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artists;

    }


}
