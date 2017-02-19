package com.streamdata.apps.cryptochat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class QrCodeActivity extends AppCompatActivity {

    public static final int QR_REQUEST_CODE = 0;

    private TextView qrCodeContentsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        Button readQrCodeButton = (Button) findViewById(R.id.readQrCodeButton);
        qrCodeContentsView = (TextView) findViewById(R.id.qrCodeContentsView);

        readQrCodeButton.setOnClickListener(new ReadQrCodeListener(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                String contents = data.getExtras().getString("la.droid.qr.result");
                qrCodeContentsView.setText(String.format("Scanned QR code contents: %s", contents));
            }

            else if (resultCode == RESULT_CANCELED) {
                qrCodeContentsView.setText("QR code scan operation canceled.");
            }
        }
    }

    private static class ReadQrCodeListener implements View.OnClickListener {
        /***
         * Listener for QR code scanning procedure
         * (Relies upon installed QRDroid app)
         */

        private final WeakReference<QrCodeActivity> parentActivityReference;

        public ReadQrCodeListener(QrCodeActivity parent) {
            parentActivityReference = new WeakReference<>(parent);
        }

        @Override
        public void onClick(View v) {
            QrCodeActivity parent = parentActivityReference.get();

            if (parent == null) {
                return;
            }

            // scan if QRDroid installed
            try {
                Intent intent = new Intent("la.droid.qr.scan");
                parent.startActivityForResult(intent, QR_REQUEST_CODE);

            // else try to open Play Market for QRDroid page
            } catch (ActivityNotFoundException ex) {

                parent.qrCodeContentsView.setText("QR code scanner is not installed. Please install QR code scanner first");

                Uri marketUri = Uri.parse("market://details?id=la.droid.qr");
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);

                try {
                    parent.startActivity(marketIntent);

                // if no Play Market installed, just write info to text view
                } catch (ActivityNotFoundException ex2) {
                    parent.qrCodeContentsView.setText("QR code scanner is not installed. Please install QR code scanner first");
                }
            }
        }
    }
}
