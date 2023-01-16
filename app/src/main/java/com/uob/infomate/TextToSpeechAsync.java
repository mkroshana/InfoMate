package com.uob.infomate;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyPresigningClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SpeechMarkType;
import com.amazonaws.services.polly.model.SynthesizeSpeechPresignRequest;
import com.uob.infomate.Avatar.AvatarView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import static android.support.constraint.Constraints.TAG;

public class TextToSpeechAsync extends AsyncTask<String, Void, String> {

    // Cognito pool ID. Pool needs to be unauthenticated pool with
    // Amazon Polly permissions.
    private static String COGNITO_POOL_ID = "us-west-2:b08e9293-c34f-44ba-900d-64fc1f9df0bc";

    // Region of Amazon Polly.
    private static Regions MY_REGION = Regions.US_WEST_2;

    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private String mText;
    private AvatarView mAvatarView;
    private int[] mVisemes;

    public TextToSpeechAsync(AvatarView avatarView, String text) {
        mAvatarView = avatarView;
        mContext = avatarView.getContext();
        mMediaPlayer = new MediaPlayer();
        mText = text;
    }

    @Override
    protected String doInBackground(String... strings) {
        // Initialize the Amazon Cognito credentials provider.
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext.getApplicationContext(),
                COGNITO_POOL_ID,
                MY_REGION
        );

        // Create a client that supports generation of presigned URLs.
        AmazonPollyPresigningClient client = new AmazonPollyPresigningClient(credentialsProvider);

        // Create speech synthesis request.
        SynthesizeSpeechPresignRequest synthesizeSpeechPresignRequest =
                new SynthesizeSpeechPresignRequest()
                        .withText(mText)
                        .withVoiceId("Joanna")
                        .withSpeechMarkTypes(SpeechMarkType.Viseme.toString())
                        .withOutputFormat(OutputFormat.Json);

        URL visemeUrl = client.getPresignedSynthesizeSpeechUrl(synthesizeSpeechPresignRequest);
        getVisemes(visemeUrl);

        // Create speech synthesis request.
        synthesizeSpeechPresignRequest =
                new SynthesizeSpeechPresignRequest()
                        .withText(mText)
                        .withVoiceId("Joanna")
                        .withOutputFormat(OutputFormat.Mp3);

        URL presignedSynthesizeSpeechUrl =
                client.getPresignedSynthesizeSpeechUrl(synthesizeSpeechPresignRequest);

        return presignedSynthesizeSpeechUrl.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        // Create a media player to play the synthesized audio stream.
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            // Set media player's data source to previously obtained URL.
            mMediaPlayer.setDataSource(result);
        } catch (IOException e) {
            Log.e(result, "Unable to set data source for the media player! " + e.getMessage());
        }

        // Prepare the MediaPlayer asynchronously (since the data source is a network stream).
        mMediaPlayer.prepareAsync();

        // Set the callback to start the MediaPlayer when it's prepared.
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mAvatarView.setMouthMorphs(mVisemes);
                Log.i("setMouthMorphs", String.valueOf(mVisemes));
                mp.start();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }

    private void getVisemes(URL url) {

        Log.i("getVisemes URL", String.valueOf(url));
        String[] jsonArray = getFromUrl(url).split("[}]");
        mVisemes = new int[jsonArray.length*2+2];
        mVisemes[0] = mVisemes[1] = 0;
        int index = 2;
        int last = 0;

        for (String json : jsonArray) {
            if (json == "") continue;

            try {
                JSONObject jsonObject = new JSONObject(json + "}");

                String type = jsonObject.getString("type");
                if (type.equals("viseme")) {
                    int time = jsonObject.getInt("time");
                    Log.i("TIME", Integer.toString(time));
                    String awsValue = jsonObject.getString("value");
                    Log.i("AWS VALUE", awsValue);

                    mVisemes[index++] = time - last;
                    mVisemes[index++] = convertAwsViseme(awsValue);
                    last = time;
                }

            } catch (Exception exception) {
                Log.i("ERROR: ", exception.getMessage());
            }
        }
    }

    private int convertAwsViseme(String val) {
        if(val.equals("@"))      return 1;
        else if(val.equals("a") || val.equals("o") || val.equals("O")) return 2;
        else if(val.equals("e")) return 3;
        else if(val.equals("i") || val.equals("E")) return 4;
        else if(val.equals("r")) return 5;
        else if(val.equals("u")) return 6;
        else if(val.equals("p")) return 7;
        else if(val.equals("t") || val.equals("T") || val.equals("k") || val.equals("s")) return 8;
        else if(val.equals("S")) return 9;
        else if(val.equals ("f")) return 10;
        else return 0;
    }

    private String getFromUrl(URL url) {
        try {
            StringBuffer response = new StringBuffer();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            int status = connection.getResponseCode();

            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                return response.toString();
            } else {
                return "";
            }

        } catch (Exception exception) {
            return "";
        }
    }
}
