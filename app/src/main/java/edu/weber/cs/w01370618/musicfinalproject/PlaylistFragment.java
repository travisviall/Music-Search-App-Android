package edu.weber.cs.w01370618.musicfinalproject;

import android.app.DownloadManager;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.weber.cs.w01370618.musicfinalproject.firestore.Playlist;

/**
 * DialogFragment that displays the favorite artists the user has selected.  A Firestore List of
 * Playlist objects is collected and then a RecycleView is used to display the tracks.
 * @author Travis Viall
 */
public class PlaylistFragment extends DialogFragment {

    private View root;
    private List<Playlist> playlistList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
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
        FirebaseUser currentUser = auth.getCurrentUser();

        Toolbar toolbar = Objects.requireNonNull(getView()).findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        if(playlistList == null) {
            playlistList = new ArrayList<>();
        }

        db.collection("users").document(currentUser.getUid()).collection("playlist")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: list) {
                        Playlist playlist = d.toObject(Playlist.class);

                        Log.d("Playlist", playlist.getArtist());
                        playlistList.add(playlist);
                    }
                } else
                {
                    Log.d("Query is empty", "this");
                }

                PlaylistViewRecyclerAdapter playlistViewRecyclerAdapter = new PlaylistViewRecyclerAdapter(playlistList, getContext());
                playlistViewRecyclerAdapter.notifyDataSetChanged();
                RecyclerView recyclerView = root.findViewById(R.id.playlistRV);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(playlistViewRecyclerAdapter);
            }
        }). addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
            }
        });




    }
}