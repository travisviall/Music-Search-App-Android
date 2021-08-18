package edu.weber.cs.w01370618.musicfinalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * SignInFragment to authenticate the user through FirebaseAuth via email and password.
 * @author Travis Viall
 */
//image courtesy of Martin Engel (open source images unsplash.com)
public class SignInFragment extends Fragment {

    private View root;
    private FirebaseAuth mAuth;
    private EditText usernameField;
    private EditText passwordField;
    private Button submitBtn;
    private TextView errorField;
    private ProgressBar progressBar;

    public SignInFragment() {
        // Required empty public constructor
    }

    public interface SignInSuccess {
        void login(FirebaseUser user);
    }

    private final TextWatcher fieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        /**
         * onTextChanged watches user name and password field to ensure they are non-null.
         * Once they are non-null, the color of the Submit button text is changed green
         * and the Submit button is activated.
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!usernameField.getText().toString().isEmpty() && !passwordField.getText().toString().isEmpty()) {

                submitBtn.setTextColor(Color.GREEN);
                submitBtn.setClickable(true);
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressBar.setVisibility(View.VISIBLE);
                        String usernameValue = usernameField.getText().toString();
                        String passwordValue = passwordField.getText().toString();
                        signIn(usernameValue, passwordValue);
                        clearFields();
                    }
                });
            } else {
                submitBtn.setClickable(false);
                submitBtn.setTextColor(Color.WHITE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onResume() {
        super.onResume();

        usernameField = root.findViewById(R.id.email_field);
        usernameField.addTextChangedListener(fieldWatcher);
        passwordField = root.findViewById(R.id.password_field);
        passwordField.addTextChangedListener(fieldWatcher);
        errorField = root.findViewById(R.id.error_field);
        progressBar = root.findViewById(R.id.progress_bar);

        submitBtn = root.findViewById(R.id.signin_btn);

        Button createNewUserBtn = root.findViewById(R.id.create_acct_btn);

        createNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the CreateUserFragment
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_main, new CreateUserFragment(), "create_new")
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Method takes two String representing the user's credentials and uses FirebaseAuth method
     * signInWithEmailAndPassword to authenticate the user.  If the authentication is successful,
     * the user is granted access and the LandingPageFragment is shown.
     * @param email email address of the user
     * @param password password of the user
     */
    private void signIn(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {

                            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayout_main, new LandingPageFragment(), "landing_page")
                                    .addToBackStack(null)
                                    .commit();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                            String errorMessage;

                            switch(errorCode) {
                                case "ERROR_WRONG_PASSWORD":
                                    errorMessage = "Invalid password. Please try again.";
                                    errorField.setText(errorMessage);
                                    break;
                                case "ERROR_INVALID_EMAIL":
                                    errorMessage = "Invalid email. Please try again";
                                    errorField.setText(errorMessage);
                                    break;
                                case "ERROR_USER_NOT_FOUND":
                                    errorMessage = "Account not found.  Please try again";
                                    errorField.setText(errorMessage);
                                    break;

                            }

                        }
                    }
                });
    }

    /**
     * Method to clear the username and password fields and hide the keyboard when the Submit button is pressed.
     */
    private void clearFields() {

        usernameField.setText("");
        passwordField.setText("");

        //hides the keyboard onClick
        try {
            InputMethodManager im = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
        }catch(NullPointerException e) {
            e.printStackTrace();
        }
    }
}