package com.uob.infomate.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uob.infomate.ChatBot.ChatBotActivity;
import com.uob.infomate.MainActivity;
import com.uob.infomate.R;

public class ProfileActivity extends AppCompatActivity {

    private EditText NewName, NewEmail, NewPassword;
    private Button UpdateAccount, DeleteAccount, SignOut;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navProfile);
        final Intent[] intent = new Intent[2];
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.navHome:
                        intent[0] = new Intent(getApplicationContext(), MainActivity.class);
                        intent[0].addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent[0]);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navChat:
                        intent[1] = new Intent(getApplicationContext(), ChatBotActivity.class);
                        intent[1].addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent[0]);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navProfile:
                        return true;
                }
                return false;
            }
        });

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        NewName = findViewById(R.id.txtNewName);
        NewEmail = findViewById(R.id.txtNewEmail);
        NewPassword = findViewById(R.id.txtNewPassword);
        UpdateAccount = findViewById(R.id.btnUpdate);
        DeleteAccount = findViewById(R.id.btnDelete);
        SignOut = findViewById(R.id.btnSignOut);
        mProgressDialog = new ProgressDialog(this);

        UpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mProgressDialog.setMessage("Updating account...");
                mProgressDialog.show();
                if (user != null && !NewEmail.getText().toString().trim().equals(""))
                {
                    user.updateEmail(NewEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        signOut();
                                        mProgressDialog.dismiss();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();
                                    }
                                }
                            });
                }
                else if (NewEmail.getText().toString().trim().equals(""))
                {
                    NewEmail.setError("Enter email");
                    mProgressDialog.dismiss();
                }
                if (user != null && !NewPassword.getText().toString().trim().equals("")) {
                    if (NewPassword.getText().toString().trim().length() < 6) {
                        NewPassword.setError("Password too short, enter minimum 6 characters");
                        mProgressDialog.dismiss();
                    } else {
                        user.updatePassword(NewPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ProfileActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            signOut();
                                            mProgressDialog.dismiss();
                                        } else {
                                            Toast.makeText(ProfileActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            mProgressDialog.dismiss();
                                        }
                                    }
                                });
                    }
                } else if (NewPassword.getText().toString().trim().equals("")) {
                    NewPassword.setError("Enter password");
                    mProgressDialog.dismiss();
                }
            }
        });

        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("Deleting account...");
                mProgressDialog.show();
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                        finish();
                                        mProgressDialog.dismiss();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });

        SignOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signOut();
                finishAffinity();
            }
        });
    }

    //sign out method
    public void signOut()
    {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressDialog.dismiss();
        overridePendingTransition(0, 0);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navProfile);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}