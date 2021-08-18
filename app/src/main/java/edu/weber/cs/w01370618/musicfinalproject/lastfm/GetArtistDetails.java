package edu.weber.cs.w01370618.musicfinalproject.lastfm;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.Artist;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.ArtistDetail;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Authorization;

/**
 * AsyncTask class used to retrieve the artist info for a specific artist via the lastfm API.
 * @author Travis Viall
 */
public class GetArtistDetails extends AsyncTask<String, Integer, String> {

    private String rawJSON;
    private String artistName;
    private List<ArtistDetail> details;

    public GetArtistDetails(String artistName) {
        this.artistName = artistName;
    }

    public List<ArtistDetail> getDetails() {
        return details;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + artistName + "&api_key="
                    + Authorization.AUTH_TOKEN + "&format=json");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();

            int statusCode = connection.getResponseCode();

            switch (statusCode) {
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    rawJSON = bufferedReader.readLine();
                    Log.d("ArtistDetails", rawJSON.substring(0,255));
            }

        } catch (IOException  e) {
            e.printStackTrace();
        }
        return rawJSON;
    }

    @Override
    protected void onPostExecute(String results) {
        super.onPostExecute(results);

        try {
            List<ArtistDetail> details;
            details = parseJSON(results);

        }  catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private List<ArtistDetail> parseJSON(String result) throws JSONException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type detailsType = new TypeToken<ArrayList<ArtistDetail>>(){}.getType();
        JSONObject object = new JSONObject(result);
        JSONObject artistObj = object.getJSONObject("artist");

       for(Iterator<String> keys = artistObj.keys(); keys.hasNext();)
           Log.d("Keys" , keys.next());

       details = gson.fromJson(String.valueOf(artistObj), detailsType);

        return details;

    }

}
