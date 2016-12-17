package com.mycroft.qrcodedemo.luminance;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;
import com.google.zxing.RGBLuminanceSource;

/**
 * Created by Mycroft on 2016/12/17.
 */

public final class BitmapLuminanceSource extends LuminanceSource {

    private RGBLuminanceSource mRGBLuminanceSource;

    public BitmapLuminanceSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());

        final int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        mRGBLuminanceSource = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
    }
    /*public BitmapLuminanceSource(byte[] yuvData, int dataWidth, int dataHeight, int left, int top, int width, int height, boolean reverseHorizontal) {
        super(yuvData, dataWidth, dataHeight, left, top, width, height, reverseHorizontal);
    }*/

    @Override
    public byte[] getRow(int y, byte[] row) {
        return mRGBLuminanceSource.getRow(y, row);
    }

    @Override
    public byte[] getMatrix() {
        return mRGBLuminanceSource.getMatrix();
    }
}
