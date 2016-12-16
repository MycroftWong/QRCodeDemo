package com.mycroft.qrcodedemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class ZxingActivity extends AppCompatActivity {

    private ImageView mQrcodeImageView;
    private EditText mContentsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);

        mContentsEditText = (EditText) findViewById(R.id.contents_edit_text);

        mQrcodeImageView = (ImageView) findViewById(R.id.qrcode_image_view);
    }

    public void encode(View view) {
        final String contents = mContentsEditText.getText().toString();
        try {
            BitMatrix bitMatrix = encode(contents);

            final int width = bitMatrix.getWidth();
            final int height = bitMatrix.getHeight();
            Log.e("mycroft", "width = " + width + ", height = " + height);

            final long start = System.currentTimeMillis();

            final int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixels[y * width + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//            Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, width, 0, 0, 200, 200);

            final long end = System.currentTimeMillis();

            Log.e("mycroft", "" + (end - start));
            mQrcodeImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
            Log.e("mycroft", "encode error", e);
        }
    }

    public void decode(View view) {
    }

    private Bitmap bitMatrixToBitmap(BitMatrix bitMatrix) {

        final int width = bitMatrix.getWidth();
        final int height = bitMatrix.getHeight();

        final int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y * width + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
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
