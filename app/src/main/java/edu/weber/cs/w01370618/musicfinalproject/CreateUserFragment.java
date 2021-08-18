package edu.weber.cs.w01370618.musicfinalproject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * CreateUserFragment takes a user name, email, and password to create a FirebaseAuth authentication
 * and create an account for the user.
 * @author Travis Viall
 */
public class CreateUserFragment extends Fragment {

    private View root;
    private FirebaseAuth mAuth;
    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button submitBtn;
    private TextView errorField;

    public CreateUserFragment() {
        // Required empty public constructor
    }

    private final TextWatcher fieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        /**
         *onTextChanged watches the TextInput fields to ensure they are non-null. Once all fields are
         * non-null and the password and confirmPassword fields match, the Submit button is activated.
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!usernameField.getText().toString().isEmpty() && !passwordField.getText().toString().isEmpty()
                    && !confirmPasswordField.getText().toString().isEmpty() && !emailField.getText().toString().isEmpty()){

                if(passwordField.getText().toString().equals(confirmPasswordField.getText().toString())) {

                    submitBtn.setTextColor(Color.GREEN);
                    submitBtn.setClickable(true);
                    submitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String username = usernameField.getText().toString();
                            String email = emailField.getText().toString();
                            String password = passwordField.getText().toString();
                            clearFields();
                            createAccount(username, email, password);
                        }
                    });
                }
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
        return root = inflater.inflate(R.layout.fragment_create_user, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mAuth = FirebaseAuth.getInstance();
        Context context = getContext();

        submitBtn = root.findViewById(R.id.submit_btn);

        errorField = root.findViewById(R.id.error_field);

        usernameField = root.findViewById(R.id.displayName_field);
        usernameField.addTextChangedListener(fieldWatcher);

        emailField = root.findViewById(R.id.email_field);
        emailField.addTextChangedListener(fieldWatcher);

        passwordField = root.findViewById(R.id.password_field);
        passwordField.addTextChangedListener(fieldWatcher);

        confirmPasswordField = root.findViewById(R.id.confirm_password_field);
        confirmPasswordField.addTextChangedListener(fieldWatcher);

        Button cancelBtn = root.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStackImmediate();
            }
        });
    }

    /**
     * Method takes three Strings representing the user's credentials when creating a new account
     * and uses FirebaseAuth method createUserWithEmailAndPassword to authenticate the user.  If
     * successful, a new Firebase account is created and the LandingPage is shown.
     * @param username the username of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    private void createAccount(String username, String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user != null) {
                                user.updateEmail(email);
                                user.updatePassword(password);

                                UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                user.updateProfile(profileChange);
                            }

                            //launch the LandingPageFragment
                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction()
                                    .replace(R.id.frameLayout_main, new LandingPageFragment(), "landing_page")
                                    .addToBackStack(null)
                                    .commit();

                        } else {

                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            String errorMessage;

                            //Log.w("Create Account Failed", error + " " + errorCode);
                            //The email address is badly formatted. ERROR_INVALID_EMAIL
                            //The email address is already in use by another account. ERROR_EMAIL_ALREADY_IN_USE
                            //The given password is invalid. [ Password should be at least 6 characters ] ERROR_WEAK_PASSWORD

                            switch(errorCode) {
                                case "ERROR_INVALID_EMAIL":
                                    errorMessage = "Invalid email address. Please try again";
                                    errorField.setText(errorMessage);
                                    break;
                                case "ERROR_EMAIL_ALREADY_IN_USE" :
                                    errorMessage = "Email address already in use. Please try again";
                                    errorField.setText(errorMessage);
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    errorMessage = "Weak password.  Please enter at least 6 characters";
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
        emailField.setText("");
        confirmPasswordField.setText("");

        //hides the keyboard onClick
        try {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }catch(NullPointerException e) {
        }
    }
}