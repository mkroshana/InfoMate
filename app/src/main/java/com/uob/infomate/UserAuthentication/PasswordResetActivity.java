package com.uob.infomate.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.uob.infomate.R;

public class PasswordResetActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private EditText txtResetEmail;
    private Button btnReset, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        btnReset = findViewById(R.id.btnResetLink);
        txtResetEmail = findViewById(R.id.txtResetEmail);

        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ResetEmail = txtResetEmail.getText().toString().trim();

                if (ResetEmail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter the email address associated with your account",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(ResetEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),
                                    "We have sent you instructions to reset your password!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}