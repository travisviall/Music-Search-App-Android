package edu.weber.cs.w01370618.musicfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.weber.cs.w01370618.musicfinalproject.firestore.Playlist;

/**
 * Recycler adapter used to display the tracks user has selected for the playlist.
 * @author Travis Viall
 */
public class PlaylistViewRecyclerAdapter extends RecyclerView.Adapter<PlaylistViewRecyclerAdapter.ViewHolder> {

    private final List<Playlist> playlistsList;

    public PlaylistViewRecyclerAdapter(List<Playlist> playlistList, Context context) {
        this.playlistsList = playlistList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_track_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Playlist playlist = playlistsList.get(position);

        if(playlist != null) {
            holder.artistName.setText(playlist.getArtist());
            holder.songName.setText(playlist.getSong());
        }

    }

    @Override
    public int getItemCount() {
        return playlistsList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemRoot;
        public TextView artistName;
        public TextView songName;

        public ViewHolder(@NonNull View view) {
            super(view);

            itemRoot = view;
            artistName = itemRoot.findViewById(R.id.playlist_artist_value);
            songName = itemRoot.findViewById(R.id.playlist_song_value);
        }
    }
}
