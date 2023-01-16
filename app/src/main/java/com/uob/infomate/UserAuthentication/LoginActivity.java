package com.uob.infomate.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.uob.infomate.MainActivity;
import com.uob.infomate.R;

public class LoginActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    EditText txtEmail, txtPassword;
    Button btnSignIn, btnForgotPassword, btnSignUp;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finishAffinity();
        }

        setContentView(R.layout.activity_login);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();

                if (email.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter email address!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter password!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgressDialog.setMessage("Logging in...");
                mProgressDialog.show();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this,
                        new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        mProgressDialog.dismiss();
                        if (!task.isSuccessful())
                        {
                            if (password.length() < 8)
                            {
                                txtPassword.setError("Password too short, enter minimum 8 characters!");
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, PasswordResetActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }
}