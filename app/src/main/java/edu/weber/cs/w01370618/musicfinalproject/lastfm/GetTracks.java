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
 * AsyncTask class used to retrieve the track data for a specified album via the lastfm API.
 * @author Travis Viall
 */

public class GetTracks extends AsyncTask<String, Integer, String> {

    private String rawJSON;
    private final String artistName;
    private final String albumName;
    private List<Track> tracks;
    private OnTrackListComplete mCallback;

    public GetTracks(String artistName, String albumName){
        this.albumName = albumName;
        this.artistName = artistName;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public interface OnTrackListComplete{
        void processTrackList(List<Track> trackList);
    }

    public void setTrackListComplete(OnTrackListComplete mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    protected String doInBackground(String... strings) {

        try{
            URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=" + Authorization.AUTH_TOKEN +
                    "&artist=" + artistName + "&album=" + albumName + "&format=json");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();

            int statusCode = connection.getResponseCode();

            switch(statusCode) {
                case 200:
                case 201:
                    BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    rawJSON =bufferedReader.readLine();
                    //Log.d("rawJSON", rawJSON.substring(0,255));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawJSON;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try
        {
            List<Track> tracks = parseJSON(result);

            if(tracks != null){
                if(mCallback != null) {
                    mCallback.processTrackList(tracks);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private List<Track> parseJSON(String result) throws JSONException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type trackType = new TypeToken<ArrayList<Track>>(){}.getType();

        JSONObject results = new JSONObject(result);
        JSONObject album = results.getJSONObject("album");
        JSONObject tracksObj = album.getJSONObject("tracks");
        JSONArray track = tracksObj.getJSONArray("track");

//        for(int i = 0; i < track.length(); i++) {
//            Log.d("Tracks", track.getString(i));
//        }

        tracks = gson.fromJson(String.valueOf(track), trackType);

        for(Track t: tracks) {
            Log.d("track", t.getName() + " " + t.getDuration());
        }

        return tracks;

    }
}
