package edu.weber.cs.w01370618.musicfinalproject;

import android.app.Dialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.weber.cs.w01370618.musicfinalproject.firestore.FavoriteArtist;

/**
 * DialogFragment that displays the artists the user has selected as their favorites. A Firestore
 * List of FavoriteArtist objects is collected and then a RecyclerView is used to display the
 * artists.
 * @author Travis Viall
 */
public class FavoriteArtistsFragment extends DialogFragment {

    private View root;
    private List<FavoriteArtist> favoriteArtistList;
    private FirebaseAuth auth;


    public FavoriteArtistsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_favorite_artists, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreen);
        //setHasOptionsMenu(true);

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

        Toolbar toolbar = Objects.requireNonNull(getView()).findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if(favoriteArtistList == null) {
            favoriteArtistList = new ArrayList<>();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(currentUser != null) {

            db.collection("users").document(currentUser.getUid()).collection("favorite_artists")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            FavoriteArtist favoriteArtist = d.toObject(FavoriteArtist.class);

                            if(favoriteArtist != null) {
                                Log.d("Fave Artists", favoriteArtist.getName());
                                favoriteArtistList.add(favoriteArtist);
                            }
                        }
                    } else {
                        Log.d("Favorite List", " is empty");
                    }

                    FavoriteArtistsRecyclerAdapter favoriteArtistsRecyclerAdapter = new FavoriteArtistsRecyclerAdapter(favoriteArtistList, getContext());
                    favoriteArtistsRecyclerAdapter.notifyDataSetChanged();
                    RecyclerView recyclerView = root.findViewById(R.id.favorite_artistsRV);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(favoriteArtistsRecyclerAdapter);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}