package edu.weber.cs.w01370618.musicfinalproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.Album;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Artist;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.GetAlbums;

/**
 * Recycler Adapter used to display the search results of the value entered by the user.
 * @author Travis Viall
 */
public class ArtistSearchRecyclerAdapter extends RecyclerView.Adapter<ArtistSearchRecyclerAdapter.ViewHolder> {

    private final List<Artist> artistList;
    private final SearchResultsClickListener mCallback;


    public ArtistSearchRecyclerAdapter(List<Artist> artistList, Context context) {
        this.artistList = artistList;
        try {
                mCallback = (SearchResultsClickListener) context;
        }catch(ClassCastException e) {
            throw new ClassCastException("Must implement SearchResultsClickListener interface");
        }
    }

    /**
     * Interface that sends the Artist object to the Main Activity.
     */
    public interface SearchResultsClickListener {
        void onArtistClick(Artist artist);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Artist artist = artistList.get(position);


        if(artist != null) {
            holder.artist = artist;
            holder.tv1.setText(artist.getName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onArtistClick(artist);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemRoot;
        public TextView tv1;
        public Artist artist;
        //public ImageView imageView;
        public CardView cardView;


    public ViewHolder(@NonNull View view) {
        super(view);
        itemRoot = view;
        tv1 =itemRoot.findViewById(R.id.artist_field);
        //imageView = itemRoot.findViewById(R.id.imageView);
        cardView = itemRoot.findViewById(R.id.search_results_card);
    }
}
}
