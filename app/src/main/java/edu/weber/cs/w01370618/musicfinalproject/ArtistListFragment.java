package edu.weber.cs.w01370618.musicfinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.Artist;

/**
 * Fragment to display the search results for the value entered.  A RecyclerView ArtistSearchRecyclerAdapter
 * is used to display the results.
 * @author Travis Viall
 */
public class  ArtistListFragment extends Fragment {

    private View root;
    private List<Artist> artistList;

    public ArtistListFragment() {
        // Required empty public constructor
    }

    /**
     * Overridden constructor to access a List of Artist objects.
     * @param artistList a list of Artist objects
     */
    public void getArtists(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_artist_list, container, false);
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
    public void onResume() {

        super.onResume();
        if(artistList == null) {
            artistList = new ArrayList<>();
        }

        RecyclerView recyclerView = root.findViewById(R.id.artist_rv_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ArtistSearchRecyclerAdapter(artistList, getContext()));
    }
}