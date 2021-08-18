package edu.weber.cs.w01370618.musicfinalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.weber.cs.w01370618.musicfinalproject.lastfm.Album;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.GetTracks;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Image;
import edu.weber.cs.w01370618.musicfinalproject.lastfm.Track;

import static edu.weber.cs.w01370618.musicfinalproject.MainActivity.getArtistClickedName;

/**
 * RecyclerView to display the artist's discography
 * @author Travis Viall
 */
public class AlbumViewRecyclerAdapter extends RecyclerView.Adapter<AlbumViewRecyclerAdapter.ViewHolder> {

    private final List<Album> albumList;
    private String artistName = getArtistClickedName();
    private final AlbumClickListener mCallback;
    private String albumURL;

    public AlbumViewRecyclerAdapter(List<Album> albumList, Context context) {
        this.albumList = albumList;
        try {
            mCallback = (AlbumClickListener) context;
        }catch(ClassCastException e) {
            throw new ClassCastException("Must implement AlbumClickListener interface");
        }
    }

    /**
     * Interface that passes an Album object to the Main Activity.
     */
    public interface AlbumClickListener {
        void onAlbumClick(Album album);
    }

    public String getAlbumURL() {
        return albumURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Album album = albumList.get(position);
        List<Image> images = album.getImages();

        holder.album = album;
        if(!album.getName().equals("(null)")) {
            holder.name.setText(album.getName());
            for(Image i: images) {
                albumURL = i.getImageURL();
                if(!albumURL.equals("")) {
                    Picasso.get().load(albumURL).into(holder.imageView);
                } else {
                    Picasso.get().load(R.drawable.no_image_found).into(holder.imageView);
                }
            }
        }

        holder.albumSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onAlbumClick(album);

            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemRoot;
        public ImageView imageView;
        public Album album;
        public TextView name;
        public TextView url;
        public LinearLayout albumSelector;

        public ViewHolder(@NonNull View view) {
            super(view);

            itemRoot = view;
            imageView = itemRoot.findViewById(R.id.image_view_album);
            name = itemRoot.findViewById(R.id.album_name_album_view);
            albumSelector = itemRoot.findViewById(R.id.album_selector);

        }
    }
}
