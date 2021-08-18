package edu.weber.cs.w01370618.musicfinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import edu.weber.cs.w01370618.musicfinalproject.firestore.FavoriteArtist;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.SearchArtist;

/**
 * Fragment that holds the Favorites/Playlists buttons and the SearchFragment.
 * @author Travis Viall
 */
public class LandingPageFragment extends Fragment {

    private View root;
    private FirebaseAuth auth;

    public LandingPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_landing_page, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
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

                if(fm != null) {
                    fm.beginTransaction()
                            .replace(R.id.frameLayout_main, new SignInFragment(), "sign_in")
                            .commit();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = Objects.requireNonNull(getView()).findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();

        Button favoritesBtn = root.findViewById(R.id.favoritesBtn);
        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteArtistsFragment favoriteArtistsFragment = new FavoriteArtistsFragment();
                FragmentManager fm = getFragmentManager();

                if(fm != null)
                favoriteArtistsFragment.show(fm, "favorites");
            }
        });

        Button playlistsBtn = root.findViewById(R.id.playlistBtn);
        playlistsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistFragment playlistFragment = new PlaylistFragment();
                FragmentManager fm = getFragmentManager();

                if(fm != null)
                playlistFragment.show(fm, "playlist");
            }
        });
    }
}