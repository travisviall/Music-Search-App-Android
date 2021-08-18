package edu.weber.cs.w01370618.musicfinalproject;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.Album;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Artist;

/**
 * DialogFragment used to show the Artist information for the selected artist.  A RecyclerView is
 * used to populate the albums of the artist.
 * @author Travis Viall
 */
public class ArtistViewFragment extends DialogFragment {

    private View root;
    private Artist artist;
    private TextView artistName;
    private List<Album> albumList;
    private FirebaseAuth auth;

    public ArtistViewFragment() {
        // Required empty public constructor
    }

    /**
     * Overridden constructor that accepts an Artist object.
     * @param artist an object that represents the artist selected.
     */
    public ArtistViewFragment(Artist artist) {
        this.artist = artist;
    }

    /**
     * Get method that retrieves a List of Album objects.
     * @param albumList a list of Album objects.
     */
    public void getAlbums(List<Album> albumList) {
        this.albumList = albumList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_artist_view, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreen);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        artistName = root.findViewById(R.id.results_artist_name_field);
        artistName.setText(artist.getName());

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if(albumList == null) {
            albumList = new ArrayList<>();
        }
        RecyclerView recyclerView = root.findViewById(R.id.albumRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AlbumViewRecyclerAdapter(albumList, getContext()));
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

        Toolbar toolbar = Objects.requireNonNull(getView()).findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();

        ImageButton favoriteBtn = root.findViewById(R.id.favorites_icon);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteBtn.setImageResource(R.drawable.ic_baseline_star_24_filled);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, String> favoriteArtist = new HashMap<>();
                favoriteArtist.put("name", artistName.getText().toString());

                if(currentUser != null) {
                    db.collection("users").document(currentUser.getUid()).collection("favorite_artists")
                            .document(artistName.getText().toString())
                            .set(favoriteArtist, SetOptions.merge());
                    Toast.makeText(getContext(), "FavoriteArtist added to Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No User Exists.", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}