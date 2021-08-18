package edu.weber.cs.w01370618.musicfinalproject.lastfm;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
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
import java.util.List;

/**
 * AsyncTask class to retrieve the Album discography for a specified artist via the lastfm API.
 * @author Travis Viall
 */
public class GetAlbums extends AsyncTask<String, Integer, String> {

    private String rawJSON;
    private String artist;
    private OnAlbumListComplete mCallback;
    private List<Album> albums;


    public GetAlbums(String artist) {
        this.artist = artist;
    }

    public List<Album> getAlbums() {
       return albums;
    }

    public interface OnAlbumListComplete {
        void processAlbumList(List<Album> albums);
    }

    public void setOnAlbumListComplete(OnAlbumListComplete mCallback) {
        this.mCallback = mCallback;
    }


    @Override
    protected String doInBackground(String... strings) {

        try {

            URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&artist="
                    + artist + "&api_key=" + Authorization.AUTH_TOKEN +"&format=json&limit=10");

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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawJSON;
    }

    @Override
    protected void onPostExecute(String results) {
        super.onPostExecute(results);


        try {
            List<Album> albums = parseJSON(results);

            if(artist != null) {
                if(mCallback != null) {
                    mCallback.processAlbumList(albums);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Album> parseJSON(String result) throws JSONException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type albumType = new TypeToken<ArrayList<Album>>(){}.getType();

        JSONObject results = new JSONObject(result);
        JSONObject topAlbums = results.getJSONObject("topalbums");
        JSONArray albumsArray = topAlbums.getJSONArray("album");

        albums = gson.fromJson(String.valueOf(albumsArray), albumType);

        return albums;
    }
};
