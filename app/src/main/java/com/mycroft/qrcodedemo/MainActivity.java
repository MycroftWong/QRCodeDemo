package com.mycroft.qrcodedemo;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class MainActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    public static final String RESULT_CODE = "code.result";
    private QRCodeReaderView mQrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qr_reader_view);

        mQrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        mQrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        mQrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        mQrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
//        mQrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        mQrCodeReaderView.setBackCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        final Intent intent = new Intent();
        intent.putExtra(RESULT_CODE, text);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQrCodeReaderView.stopCamera();
    }
}
