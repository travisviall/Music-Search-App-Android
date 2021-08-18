package edu.weber.cs.w01370618.musicfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.Album;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Artist;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.ArtistDetail;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.GetAlbums;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.GetArtistDetails;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.GetTracks;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Image;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.SearchArtist;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Track;

/**
 * Main activity that implements the interfaces used by other fragments to pass data
 * @author Travis Viall
 */
public class MainActivity extends AppCompatActivity implements SignInFragment.SignInSuccess, SearchFragment.OnSearchClicked,
ArtistSearchRecyclerAdapter.SearchResultsClickListener, AlbumViewRecyclerAdapter.AlbumClickListener,
        FavoriteArtistsRecyclerAdapter.FavoriteArtistClickListener {

    //image courtesy of baby abbas (open source images unsplash.com)
    private static String artistClickedName;
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();

        FragmentManager fm = getSupportFragmentManager();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(currentUser != null) {

            Map<String, String> user = new HashMap<>();
            user.put("email", currentUser.getEmail());

            db.collection("users").document(currentUser.getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "addUser to FireStore");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Failure", "addUser to FireStore");
                        }
                    });

            fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.frameLayout_main, new LandingPageFragment(), "landing_page")
                    .commit();
        } else {
            fm.beginTransaction()
                    .replace(R.id.frameLayout_main, new SignInFragment(), "sign_in")
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void login(FirebaseUser user) {
        //load the LandingPageFragment customized with the user info
    }

    /**
     * Method used to retrieve the search value from the SearchFragment that is used to
     * make an API call to get the artists that match the value.  The search value is
     * passed to the SearchArtist class that implements the setOnArtistListComplete interface
     * to process the results. An ArtistListFragment is then used to replace the results_layout
     * with the artist's names.
     * @param value the value the user enters in the search field to search for an artist.
     */
    @Override
    public void searchClicked(String value) {

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        SearchArtist getArtist = new SearchArtist(value);
        getArtist.setOnArtistListComplete(new SearchArtist.OnArtistListComplete() {
            @Override
            public void processResultsList(List<Artist> artists) {

                ArtistListFragment artistListFragment = new ArtistListFragment();
                artistListFragment.getArtists(artists);

                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.results_layout, artistListFragment, "results")
                        .addToBackStack(null)
                        .commit();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        getArtist.execute("");
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Method used to retrieve the discography of the selected artist.  The Artist's name is passed
     * to the GetAlbums class that makes an API call to get the albums associated with the artist name.
     * An ArtistViewFragment is then shown with the associated albums.
     * @param artist the Artist object the user clicks to view.
     */
    @Override
    public void onArtistClick(Artist artist) {

        artistClickedName = artist.getName();

        Log.d("ClickedArtist", artistClickedName);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        GetAlbums getAlbums = new GetAlbums(artist.getName());
        getAlbums.setOnAlbumListComplete(new GetAlbums.OnAlbumListComplete() {
            @Override
            public void processAlbumList(List<Album> albums) {

                ArtistViewFragment artistViewFragment = new ArtistViewFragment(artist);
                artistViewFragment.getAlbums(albums);
                artistViewFragment.show(getSupportFragmentManager(), "albums_view");
                progressBar.setVisibility(View.INVISIBLE);
            }

        });
        getAlbums.execute("");

    }

    /**
     * Method used to retrieve the tracks of the album clicked. A TrackViewFragment is shown with the
     * associated tracks.
     * @param album the Album object the user has clicked.
     */
    @Override
    public void onAlbumClick(Album album) {

       Log.d("FavoriteArtist", artistClickedName + " " + album.getName());
        GetTracks getTracks = new GetTracks(artistClickedName, album.getName());
        getTracks.setTrackListComplete(new GetTracks.OnTrackListComplete() {
            @Override
            public void processTrackList(List<Track> trackList) {

                TrackViewFragment trackViewFragment = new TrackViewFragment(album);
                trackViewFragment.getTracks(trackList);
                trackViewFragment.show(getSupportFragmentManager(), "tracks_view");
            }
        });
        getTracks.execute("");
    }

    /**
     *
     * @return the name of the artist clicked.
     */
    public static String getArtistClickedName() {
        return artistClickedName;
    }

    /**
     * Method retrieves the album data for the artist the user has selected from the favorites list.
     * The artist name is passed to the GetAlbums class that makes an API call to retrieve the
     * associated albums
     * @param artist the Artist object clicked from the favorites list.
     */
    @Override
    public void onFavoriteClicked(Artist artist) {

        artistClickedName = artist.getName();

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        GetAlbums getAlbums = new GetAlbums(artistClickedName);
        getAlbums.setOnAlbumListComplete(new GetAlbums.OnAlbumListComplete() {
            @Override
            public void processAlbumList(List<Album> albums) {

                ArtistViewFragment artistViewFragment = new ArtistViewFragment(artist);
                artistViewFragment.getAlbums(albums);
                artistViewFragment.show(getSupportFragmentManager(), "albums_view");
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        getAlbums.execute("");
    }

}