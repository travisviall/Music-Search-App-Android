/*package edu.weber.cs.w01370618.musicfinalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.FavoriteArtist;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.SearchArtist;

public class ResultsFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private List<FavoriteArtist> artistList;
    private String searchValue;

    public ResultsFragment() {
        // Required empty public constructor
    }

    public void getArtists(List<FavoriteArtist> artistList) {
        this.artistList = artistList;
    }

    public void getSearchValue(String searchValue){
        this.searchValue = searchValue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(artistList == null ) {
            artistList =new ArrayList<>();
        }

        recyclerView = root.findViewById(R.id.artist_rv_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ArtistSearchRecyclerAdapter(artistList, getContext()));

    }

}*/