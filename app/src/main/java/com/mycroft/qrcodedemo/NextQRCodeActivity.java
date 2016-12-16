package com.mycroft.qrcodedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.github.yoojia.qrcode.camera.CameraPreviewView;
import com.github.yoojia.qrcode.camera.CaptureCallback;
import com.github.yoojia.qrcode.camera.LiveCameraView;
import com.github.yoojia.qrcode.qrcode.QRCodeDecoder;

public class NextQRCodeActivity extends AppCompatActivity {
    public static final String TAG = "mycroft";

    private LiveCameraView mLiveCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_next_qrcode);

        mLiveCameraView = (LiveCameraView) findViewById(R.id.capture_image);
        mLiveCameraView.setPreviewReadyCallback(new CameraPreviewView.PreviewReadyCallback() {

            @Override
            public void onStarted(Camera camera) {
                Log.i(TAG, "-> Camera started, start to auto capture");
                mLiveCameraView.startAutoCapture(1500, mCaptureCallback);
            }

            @Override
            public void onStopped() {
                Log.i(TAG, "-> Camera stopped");
                mLiveCameraView.stopAutoCapture();
            }
        });
    }

    private final CaptureCallback mCaptureCallback = new CaptureCallback() {
        @Override
        public void onCaptured(Bitmap bitmap) {
            Log.e(TAG, Thread.currentThread().getName());
            final QRCodeDecoder decoder = new QRCodeDecoder.Builder().build();
            String text = decoder.decode(bitmap);

            if (!TextUtils.isEmpty(text)) {
                final Intent intent = new Intent();
                intent.putExtra(MainActivity.RESULT_CODE, text);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        }
    };

}
