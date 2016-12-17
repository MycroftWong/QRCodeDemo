package com.mycroft.qrcodedemo;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mycroft.qrcodedemo.luminance.BitmapLuminanceSource;

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
            mBitmap = bitmap;

            Log.e("mycroft", "" + (end - start));
            mQrcodeImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
            Log.e("mycroft", "encode error", e);
        }
    }

    public void decode(View view) {
        if (mBitmap == null || mBitmap.isRecycled()) {
            return;
        }

        Bitmap bitmap = rotateBitmap(mBitmap);
        mQrcodeImageView.setImageBitmap(bitmap);
        final BitmapLuminanceSource luminanceSource = new BitmapLuminanceSource(bitmap);
        final Binarizer binarizer = new HybridBinarizer(luminanceSource);
        final BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        try {
            final Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            Result result = new QRCodeReader().decode(binaryBitmap, hints);

            Log.e("mycroft", "" + result);
            Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("mycroft", e.getMessage(), e);
        }
    }

    /**
     * 解析Bitmap中的二维码
     *
     * @param bitmap
     * @return 解析结果，null表示解析失败
     */
    private String decode(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource luminanceSource = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

        try {
            final Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            Result result = new QRCodeReader().decode(binaryBitmap, hints);

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap rotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.setRotate(180, source.getWidth() >> 1, source.getHeight() >> 1);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private Bitmap mBitmap;

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

        mBitmap = bitmap;
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        mBitmap = null;
        super.onDestroy();
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
