package com.mycroft.qrcodedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class ZxingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);
    }

    public void encode(View view) {
        final String contents = "Hello world!";
        try {
            BitMatrix bitMatrix = encode(contents);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void decode(View view) {
    }

    /**
     * 生成二维码
     *
     * @param contents 二维码内容
     * @return 二维码的描述对象 BitMatrix
     * @throws WriterException 编码时出错
     */
    private BitMatrix encode(String contents) throws WriterException {
        final Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        return new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, 320, 320, hints);
    }

}
