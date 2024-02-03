package com.cools01.aitaskmanager;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;


public class SignupActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference usersReference;
    private FirebaseAuth mAuth;

    private Button buttonSign , buttonContinue ;
    private TextView usernameTv ,errorTextView;
    private EditText usernameEt ;
    private ProgressBar progressBar;
    private LinearLayout fc ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        database =  FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        usersReference = database.getReference().child("users");
        progressBar = findViewById(R.id.progressBar11);
        fc = findViewById(R.id.fc);



        List<AuthUI.IdpConfig> providers = Collections.singletonList(

                new AuthUI.IdpConfig.GoogleBuilder().build()

        );

        buttonSign = findViewById(R.id.button2);
        usernameTv = findViewById(R.id.usernameTv);
        usernameEt = findViewById(R.id.usernameEt);
        buttonContinue = findViewById(R.id.button_continue);

        errorTextView = findViewById(R.id.errTv);


        updateUI();

        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build();
                signInLauncher.launch(signInIntent);
            }
        });

// Create and launch sign-in intent
//        Intent signInIntent = AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build();
//        signInLauncher.launch(signInIntent);


        // Add a TextWatcher to the EditText
        usernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not used in this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not used in this example
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the input text
                String inputText = editable.toString().trim();

                // Clear previous error messages
                errorTextView.setText("");

                // Check for spaces or special characters
                if (containsSpacesOrSpecialCharacters(inputText)) {
                    errorTextView.setText("Username cannot contain spaces or special characters.");
                    buttonContinue.setEnabled(false); // Disable the button
                } else {
                    // Enable the button if conditions are met
                    buttonContinue.setEnabled(true);
                }
            }
        });



        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text from the EditText
                buttonContinue.setEnabled(false);
                String inputText = usernameEt.getText().toString().trim();

                // Check if the EditText is empty
                if (inputText.isEmpty()) {
                    // Show a toast if the EditText is empty
                    Toast.makeText(SignupActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    String id = user.getUid();

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            //  usersReference.child(uid).child("name").setValue(username);
                            //      usersReference.child(uid).child("email").setValue(email);
                            //    usersReference.child(id).child("username").setValue(inputText);
                            checkUsernameExists(inputText);

//                            Intent i = new Intent(SignupActivity.this , MainActivity.class);
//                            startActivity(i);
//                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SignupActivity.this,""+ error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });









    }


    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            Toast.makeText(this, "ssssssssssss", Toast.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
            getUserData();
            buttonSign.setVisibility(View.GONE);
            fc.setVisibility(View.GONE);
            ImageView img = findViewById(R.id.gggl);

            img.setVisibility(View.GONE);

            buttonContinue.setVisibility(View.VISIBLE);
            usernameEt.setVisibility(View.VISIBLE);
            usernameTv.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "ffffffffffffffffff", Toast.LENGTH_SHORT).show();


            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url

            String email = user.getEmail();
            //  Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            //    boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            //   String username = user.getDisplayName();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //  usersReference.child(uid).child("name").setValue(username);
                    usersReference.child(uid).child("email").setValue(email);
                    usersReference.child(uid).child("uid").setValue(uid);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignupActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
    private void updateUI() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in.
            Intent i = new Intent(SignupActivity.this , MainActivity2.class);
            startActivity(i);
            finish();
        } else {
            // User is signed out.
            buttonSign.setVisibility(View.VISIBLE);
        }

    }
    private boolean containsSpacesOrSpecialCharacters(String text) {
        // Add your conditions here
        // For example, you can use a regular expression to check for spaces or special characters
        return text.contains(" ") || !text.matches("^[a-zA-Z0-9]+$");
    }

    // Function to check if the username already exists in Firebase
    private void checkUsernameExists(final String username) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String id = user.getUid();
        usersReference.orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            progressBar.setVisibility(View.GONE);
                            // Username already exists
                            errorTextView.setText("Username already exists. Please choose another.");
                        } else {

                            // Username is valid, proceed with saving to Firebase
                            usersReference.child(id).child("username").setValue(username);
                            // Clear any previous error message
                            errorTextView.setText("");
                            progressBar.setVisibility(View.GONE);
                            Intent i = new Intent(SignupActivity.this , MainActivity2.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
    }

}