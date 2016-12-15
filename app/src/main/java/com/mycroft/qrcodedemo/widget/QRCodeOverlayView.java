package com.mycroft.qrcodedemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.mycroft.qrcodedemo.R;

/**
 * Created by Mycroft on 2016/12/15.
 */
public class QRCodeOverlayView extends View {

    public QRCodeOverlayView(Context context) {
        super(context);
        init(context);
    }

    public QRCodeOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QRCodeOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QRCodeOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private Bitmap mLineBitmap;
    private NinePatch mSquareNinePatch;

    private void init(Context context) {
        mPaint.setAntiAlias(true);

        Bitmap squareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.square_qrcode);
        mSquareNinePatch = new NinePatch(squareBitmap, squareBitmap.getNinePatchChunk(), null);

        mLineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.line_qrcode);
    }

    private final Rect mSquareRect = new Rect();

    private final Rect mLineRect = new Rect();

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mLineDistance = 0;

    private static final int BORDER_WIDTH = 12;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int measuredWidth = getMeasuredWidth();
        final int measuredHeight = getMeasuredHeight();

        final int slideWidth = Math.min(measuredWidth, measuredHeight) * 3 / 5;

        // 绘制二维码扫描框
        final int left = (measuredWidth - slideWidth) >> 1;
        final int top = (measuredHeight - slideWidth) >> 1;

        final int right = (measuredWidth + slideWidth) >> 1;
        final int bottom = (measuredHeight + slideWidth) >> 1;

        mSquareRect.set(left, top, right, bottom);

        mSquareNinePatch.draw(canvas, mSquareRect, mPaint);

        // 绘制扫描条
        mLineRect.set(left + BORDER_WIDTH, top + mLineDistance + BORDER_WIDTH, left + slideWidth - BORDER_WIDTH, top + mLineDistance + BORDER_WIDTH + mLineBitmap.getHeight());
        canvas.drawBitmap(mLineBitmap, null, mLineRect, mPaint);

        mLineDistance += 3;
        if (mLineDistance + BORDER_WIDTH * 2 + mLineBitmap.getHeight() >= slideWidth) {
            mLineDistance = 0;
        }

        postInvalidateDelayed(DRAW_DELAY);
    }

    private static final int DRAW_DELAY = 14;

}
































