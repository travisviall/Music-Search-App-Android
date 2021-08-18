package edu.weber.cs.w01370618.musicfinalproject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.Track;

import static edu.weber.cs.w01370618.musicfinalproject.MainActivity.getArtistClickedName;

public class TrackViewRecyclerAdapter extends RecyclerView.Adapter<TrackViewRecyclerAdapter.ViewHolder> {

    private final List<Track> trackList;
    private String albumURL;
    private final String artistName = getArtistClickedName();

    public TrackViewRecyclerAdapter(List<Track> trackList, Context context, String albumURL) {
        this.trackList = trackList;
        this.albumURL = albumURL;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       Track track = trackList.get(position);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> favoriteTrack = new HashMap<>();

       if(track != null) {

           holder.trackName.setText(track.getName());
           holder.trackDuration.setText(convertDuration(Integer.parseInt(track.getDuration())));
           holder.trackName.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   favoriteTrack.put("artist", getArtistClickedName());
                   favoriteTrack.put("song", holder.trackName.getText().toString());
                   favoriteTrack.put("duration", holder.trackDuration.getText().toString());


                   db.collection("users").document(currentUser.getUid()).collection("playlist")
                           .document(holder.trackName.getText().toString())
                           .set(favoriteTrack, SetOptions.merge());

                   holder.trackName.setTextColor(Color.GREEN);

               }
           });
       }

    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemRoot;
        public TextView trackName;
        public TextView trackDuration;
        public ImageView albumImage;
        public Track track;

        public ViewHolder(@NonNull View view) {
            super(view);

            itemRoot = view;
            //trackNumber = itemRoot.findViewById(R.id.track_number);
            trackName = itemRoot.findViewById(R.id.track_name);
            trackDuration = itemRoot.findViewById(R.id.track_duration);
            albumImage = itemRoot.findViewById(R.id.album_image_track_view);
        }
    }

    private String convertDuration(int duration) {
        return String.format(Locale.US,"%02d:%02d", duration / 60, duration & 60);
    }
}
