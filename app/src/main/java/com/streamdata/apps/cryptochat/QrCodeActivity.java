package com.streamdata.apps.cryptochat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QrCodeActivity extends AppCompatActivity {

    public static final int QR_REQUEST_CODE = 0;

    private TextView qrCodeContentsView;
    private boolean isQrAppInstalled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        Button readQrCodeButton = (Button) findViewById(R.id.readQrCodeButton);
        qrCodeContentsView = (TextView) findViewById(R.id.qrCodeContentsView);

        readQrCodeButton.setOnClickListener(new ReadQrCodeListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                String contents = data.getExtras().getString("la.droid.qr.result");
                qrCodeContentsView.setText(String.format("Scanned QR code contents: %s", contents));
            }

            else if ((resultCode == RESULT_CANCELED) && isQrAppInstalled) {
                qrCodeContentsView.setText("QR code scan operation canceled.");
            }
        }
    }

    private class ReadQrCodeListener implements View.OnClickListener {
        /***
         * Listener for QR code scanning procedure
         * (Relies upon installed QRDroid app)
         */

        @Override
        public void onClick(View v) {
            // scan if QRDroid installed
            try {
                isQrAppInstalled = true;
                Intent intent = new Intent("la.droid.qr.scan");
                startActivityForResult(intent, QR_REQUEST_CODE);

            // else try to open Play Market for QRDroid page
            } catch (ActivityNotFoundException ex) {

                isQrAppInstalled = false;
                qrCodeContentsView.setText("QR code scanner is not installed. Please install QR code scanner first");

                Uri marketUri = Uri.parse("market://details?id=la.droid.qr");
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);

                try {
                    startActivity(marketIntent);

                // if no Play Market installed, just write info to text view
                } catch (ActivityNotFoundException ex2) {
                    qrCodeContentsView.setText("QR code scanner is not installed. Please install QR code scanner first");
                }
            }
        }
    }
}
