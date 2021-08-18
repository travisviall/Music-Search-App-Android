package edu.weber.cs.w01370618.musicfinalproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.weber.cs.w01370618.musicfinalproject.firestore.FavoriteArtist;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Album;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Artist;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.GetAlbums;

/**
 * Recycler adapter used to display the favorite artists the user has selected.
 * @author Travis Viall
 */
public class FavoriteArtistsRecyclerAdapter extends RecyclerView.Adapter<FavoriteArtistsRecyclerAdapter.ViewHolder>{

    private final List<FavoriteArtist> favoriteArtistList;
    private final FavoriteArtistClickListener mCallback;

    public interface FavoriteArtistClickListener {
        void onFavoriteClicked(Artist artist);
    }

    public FavoriteArtistsRecyclerAdapter(List<FavoriteArtist> favoriteArtists, Context context) {
        this.favoriteArtistList = favoriteArtists;
        try {
            mCallback = (FavoriteArtistClickListener) context;
        }catch(ClassCastException e) {
            throw new ClassCastException("Must implement FavoriteArtistsClickListener interface");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_artists_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FavoriteArtist favoriteArtist = favoriteArtistList.get(position);


        if(favoriteArtist != null) {
            holder.artistName.setText(favoriteArtist.getName());
            holder.artist.setName(favoriteArtist.getName());
        }

        holder.artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d("Artist Object", holder.artist.getName());
                mCallback.onFavoriteClicked(holder.artist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteArtistList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemRoot;
        public TextView artistName;
        public Artist artist;

        public ViewHolder(@NonNull View view) {
            super(view);

            itemRoot = view;
            artistName = itemRoot.findViewById(R.id.favorite_artist_name);
            artist = new Artist();
        }
    }
}
