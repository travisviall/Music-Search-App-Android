package edu.weber.cs.w01370618.musicfinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.Album;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Image;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Track;

/**
 * DialogFragment used to display the track list for the selected Album.  A lastfm API call is made
 * and then a RecyclerAapter is used to show the track name and duration.  Picasso is used to display
 * the image using the API call's URL string.
 * @author Travis Viall
 */
public class TrackViewFragment extends DialogFragment {

    private View root;
    private Track track;
    private List<Track> trackList;
    private String albumURL;
    private Album album;
    private FirebaseAuth auth;

    public TrackViewFragment() {
        // Required empty public constructor
    }

    public TrackViewFragment(Album album) {
        this.album = album;

    }

    public void getTracks(List<Track> trackList) {
        this.trackList = trackList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_track_view, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreen);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        FragmentManager fm = getFragmentManager();

        switch(item.getItemId()) {
            case R.id.menu_playlists:
                PlaylistFragment playlistFragment = new PlaylistFragment();
                playlistFragment.show(fm, "playlist");
                Log.d("menu" , "Playlist");
                return true;
            case R.id.menu_favorite_artists:
                FavoriteArtistsFragment favoriteArtistsFragment = new FavoriteArtistsFragment();
                favoriteArtistsFragment.show(fm, "favorites");
                Log.d("menu" , "Favorite Artists");
                return true;
            case R.id.menu_logout:
                Log.d("menu" , "Logout");
                auth.signOut();

                fm.beginTransaction()
                        .replace(R.id.frameLayout_main, new SignInFragment(), "sign_in")
                        .commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = Objects.requireNonNull(getView()).findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        if(trackList == null) {
            trackList = new ArrayList<>();
        }

        Image image = album.getImages().get(2);
        String albumURL = image.getImageURL();
        ImageView albumImage = root.findViewById(R.id.album_image_track_view);

        if(!albumURL.equals(""))
            Picasso.get().load(albumURL).into(albumImage);

        String albumName = album.getName();
        TextView albumNameTV = root.findViewById(R.id.track_view_album_name);
        albumNameTV.setText(albumName);

        //verified
        Log.d("TrackView URL", albumURL);

        TrackViewRecyclerAdapter trackViewRecyclerAdapter = new TrackViewRecyclerAdapter(trackList, getContext(), albumURL);
        RecyclerView recyclerView = root.findViewById(R.id.trackRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(trackViewRecyclerAdapter);
    }
}