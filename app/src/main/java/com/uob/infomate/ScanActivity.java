package com.uob.infomate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.zxing.Result;
import com.uob.infomate.Locations.NABridgeActivity;
import com.uob.infomate.Locations.SigiriyaActivity;
import com.uob.infomate.Locations.TempleOfToothActivity;

public class ScanActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;
    private static final int RC_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(ScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        String num = result.getText();
                                switch (num){
                                    case "Sigiriya":
                                        startActivity(new Intent(ScanActivity.this, SigiriyaActivity.class));
                                        break;
                                    case "Nine Arch Bridge":
                                        startActivity(new Intent(ScanActivity.this, NABridgeActivity.class));
                                        break;
                                    case "Temple of Tooth":
                                        startActivity(new Intent(ScanActivity.this, TempleOfToothActivity.class));
                                        break;
                                    default:
                                        Toast.makeText(ScanActivity.this, "Invalid QR Code", Toast.LENGTH_SHORT).show();

                                }
                    }
                });
            }
        });

        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                //Toast.makeText(this, getString(R.string.scanner_error, error), Toast.LENGTH_LONG).show()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        mPermissionGranted = false;
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, RC_PERMISSION);
                    } else {
                        mPermissionGranted = true;
                    }
                } else {
                    mPermissionGranted = true;
                }
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}