package edu.weber.cs.w01370618.musicfinalproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * Fragment holds the TextInputLayout field that is used in SearchArtists class to make an API call
 * to retrieve search results.
 * @author Travis Viall
 */
public class SearchFragment extends Fragment {

    private View root;
    private OnSearchClicked mCallback;

    /**
     * Interface used to pass the search value to the MainActivity when search button is clicked.
     */
    public interface OnSearchClicked{
        void searchClicked(String value);
    }

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageButton searchBtn = root.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();

            }
        });

        TextInputEditText searchField = root.findViewById(R.id.search_text_input_field);
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /**
             *onEditorAction changes the Enter key on the keyboard to a search function.
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

        try {
            if(mCallback == null) {
                mCallback = (OnSearchClicked) activity;
            }

        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " Must implement OnSearchClicked interface");
        }
    }

    /**
     * Method to hide the keyboard once the user clicks search button or Enter key.
     */
    private void hideKeyboard() {

        //hides the keyboard onClick
        try {
            InputMethodManager im = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
        }catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to retrieve the value in the search field and pass it to the Main Activity's implemented
     * searchClicked method.
     */
    private void search() {

        TextInputLayout searchValue = root.findViewById(R.id.search_field);

        if(searchValue != null) {

            String value = Objects.requireNonNull(searchValue.getEditText()).getText().toString();
            mCallback.searchClicked(value);
            searchValue.getEditText().setText("");
            hideKeyboard();
        }
    }
}