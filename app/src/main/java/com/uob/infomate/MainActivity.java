package com.uob.infomate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uob.infomate.Avatar.AvatarView;
import com.uob.infomate.ChatBot.ChatBotActivity;
import com.uob.infomate.Locations.NABridgeActivity;
import com.uob.infomate.Locations.SigiriyaActivity;
import com.uob.infomate.Locations.TempleOfToothActivity;
import com.uob.infomate.Recommendation.RecommendationAdapter;
import com.uob.infomate.Recommendation.RecommendationModel;
import com.uob.infomate.UserAuthentication.ProfileActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writingTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    RecyclerView mRecyclerView;
    RecommendationAdapter recommendationAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    int SRating=0, NABRating=0, TOTRating;
    private AvatarView mAvatarView;
    private ImageView IQR;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://infomate-6a4b7-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference SigiriyaRating = database.getReference("Sigiriya");
        DatabaseReference NABridgeRating = database.getReference("NABridge");
        DatabaseReference TempleOfToothRating = database.getReference("TempleOfTooth");

        SigiriyaRating.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                String value = (String) task.getResult().getValue();
                SRating = Integer.parseInt(value);
                Log.d(TAG, "Sigiriya Rating is: " + SRating);
            }
        });

        NABridgeRating.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                String value = (String) task.getResult().getValue();
                NABRating = Integer.parseInt(value);
                Log.d(TAG, "Nine Arch Bridge Rating is: " + NABRating);
            }
        });

        TempleOfToothRating.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                String value = (String) task.getResult().getValue();
                TOTRating = Integer.parseInt(value);
                Log.d(TAG, "Temple Of Tooth Rating is: " + TOTRating);
            }
        });

        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //new TextToSpeechAsync(mAvatarView, getResources().getString(R.string.introduction)).execute();
                recommendationAdapter = new RecommendationAdapter(context, getRecommendationList(SRating, NABRating, TOTRating));
                mRecyclerView.setAdapter(recommendationAdapter);
            }
        }, 2000);

        mRecyclerView = findViewById(R.id.RecommendationView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        IQR = findViewById(R.id.imgQR);
        mAvatarView = findViewById(R.id.avatar_view);

        context = this;

        //Bottom Nav Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navHome);
        final Intent[] intent = new Intent[2];
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.navHome:
                        return true;

                    case R.id.navChat:
                        intent[0] = new Intent(getApplicationContext(), ChatBotActivity.class);
                        intent[0].addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent[0]);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navProfile:
                        intent[1] = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent[1].addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent[1]);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        IQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanActivity.class));
            }
        });

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this,0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[] {tagDetected};
    }

    private void readFromIntent (Intent intent)
    {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null)
            {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i=0; i<rawMsgs.length; i++)
                {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    private void buildTagViews (NdefMessage[] msgs)
    {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int LanguageCodeLength = payload[0] & 0063;

        try
        {
            text = new String(payload, LanguageCodeLength + 1, payload.length - LanguageCodeLength - 1, textEncoding);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e("UnsupportedEncoding", e.toString());
        }
        if (text.equals("Sigiriya"))
        {
            startActivity(new Intent(MainActivity.this, SigiriyaActivity.class));
        }
        if (text.equals("Nine Arch Bridge"))
        {
            startActivity(new Intent(MainActivity.this, NABridgeActivity.class));
        }
        if (text.equals("Temple of Tooth"))
        {
            startActivity(new Intent(MainActivity.this, TempleOfToothActivity.class));
        }

    }

    @Override
    protected void onNewIntent (Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))
        {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        WriteModeOff();
        mAvatarView.setVisibility(View.GONE);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        WriteModeOn();
        overridePendingTransition(0, 0);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navHome);
    }

    private void WriteModeOn()
    {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
    }

    private void WriteModeOff()
    {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    private ArrayList<RecommendationModel> getRecommendationList(int S, int N, int T)
    {
        ArrayList<RecommendationModel> models = new ArrayList<>();

        RecommendationModel recommendationModel = new RecommendationModel();

        if ((S>=N)&&(S>=T))
        {
            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Sigiriya Rock");
            recommendationModel.setImg(R.drawable.sigiriya);
            models.add(recommendationModel);

            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Nine Arches Bridge");
            recommendationModel.setImg(R.drawable.nabridge);
            models.add(recommendationModel);

            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Temple of Tooth");
            recommendationModel.setImg(R.drawable.templeoftooth);
            models.add(recommendationModel);
        }

        if ((N>=S)&&(N>=T))
        {
            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Nine Arches Bridge");
            recommendationModel.setImg(R.drawable.nabridge);
            models.add(recommendationModel);

            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Sigiriya Rock");
            recommendationModel.setImg(R.drawable.sigiriya);
            models.add(recommendationModel);

            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Temple of Tooth");
            recommendationModel.setImg(R.drawable.templeoftooth);
            models.add(recommendationModel);
        }

        if ((T>=S)&&(T>=N))
        {
            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Temple of Tooth");
            recommendationModel.setImg(R.drawable.templeoftooth);
            models.add(recommendationModel);

            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Nine Arches Bridge");
            recommendationModel.setImg(R.drawable.nabridge);
            models.add(recommendationModel);

            recommendationModel = new RecommendationModel();
            recommendationModel.setTitle("Sigiriya Rock");
            recommendationModel.setImg(R.drawable.sigiriya);
            models.add(recommendationModel);

        }

        return models;
    }
}