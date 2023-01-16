package com.uob.infomate.Locations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uob.infomate.Avatar.AvatarView;
import com.uob.infomate.MainActivity;
import com.uob.infomate.R;
import com.uob.infomate.TextToSpeechAsync;

public class SigiriyaActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RatingBar ratingBarSigiriya;
    private Button ratingSubmit, bExit;
    private ImageView iNavigate, iSpeech;
    private AvatarView mAvatarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigiriya);

        ratingBarSigiriya = findViewById(R.id.RatingSigiriya);
        ratingSubmit = findViewById(R.id.RatingSubmit);
        bExit = findViewById(R.id.btnExit);
        iNavigate = findViewById(R.id.imgNavigate);
        iSpeech = findViewById(R.id.imgSpeech);
        mAvatarView = findViewById(R.id.avatar_view);

        ratingSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ratingS = String.valueOf(ratingBarSigiriya.getRating());
                Toast.makeText(getApplicationContext(), "Thanks for rating !", Toast.LENGTH_SHORT).show();

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://infomate-6a4b7-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference("Sigiriya");

                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String value = (String) task.getResult().getValue();
                        Log.d(TAG, "Value is: " + value);
                        int ExistingRating = Integer.parseInt(value);
                        int PresentRating = Integer.parseInt(String.valueOf(ratingS.charAt(0)));
                        Log.d(TAG, "Rating: " + PresentRating);
                        int NewRating = ExistingRating + PresentRating;
                        Log.d(TAG, "New Rating: " + NewRating);
                        myRef.setValue(String.valueOf(NewRating));
                    }
                });
            }
        });

        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri navUri = Uri.parse("google.navigation:q=Sigiriya");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, navUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                finish();
            }
        });

        iSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TextToSpeechAsync(mAvatarView, getResources().getString(R.string.Sigiriya)).execute();
            }
        });
    }
}