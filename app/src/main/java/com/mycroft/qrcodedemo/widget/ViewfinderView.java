package com.mycroft.qrcodedemo.widget;

/**
 * Created by Mycroft on 2016/12/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.zxing.ResultPoint;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 */
public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 13L;
    private static final int OPAQUE = 0xFF;

    private int ScreenRate;

    private static final int CORNER_WIDTH = 10;
    private static final int MIDDLE_LINE_WIDTH = 6;

    private static final int MIDDLE_LINE_PADDING = 5;

    private static final int SPEEN_DISTANCE = 5;

    private static float density;
    private static final int TEXT_SIZE = 14;
    private static final int TEXT_PADDING_TOP = 30;

    private Paint paint;

    private int slideTop;

    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;

    private final int resultPointColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    boolean isFirst;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        density = context.getResources().getDisplayMetrics().density;
        //½«ÏñËØ×ª»»³Édp
        ScreenRate = (int) (20 * density);

        paint = new Paint();
        maskColor = Color.parseColor("#60000000");
        resultColor = Color.parseColor("#B0000000");

        resultPointColor = Color.parseColor("#C0FFFF00");
        possibleResultPoints = new HashSet<>(5);
    }

    final Rect frame = new Rect();

    @Override
    public void onDraw(Canvas canvas) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        final int squareWidth = metrics.widthPixels * 3 / 5;
        final int squareHeight = metrics.heightPixels * 3 / 5;
        final int squareSlide = Math.min(squareWidth, squareHeight);

        final int measuredWidth = getMeasuredWidth();
        final int measuredHeight = getMeasuredHeight();
        frame.set((measuredWidth - squareSlide) >> 1, (measuredHeight - squareSlide) >> 1, (measuredWidth + squareSlide) >> 1, (measuredHeight + squareSlide) >> 1);

        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setColor(resultBitmap != null ? resultColor : maskColor);

        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            //»­É¨Ãè¿ò±ßÉÏµÄ½Ç£¬×Ü¹²8¸ö²¿·Ö
            paint.setColor(Color.GREEN);
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
                    + ScreenRate, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - ScreenRate,
                    frame.left + CORNER_WIDTH, frame.bottom, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
                    frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
                    frame.right, frame.bottom, paint);

            slideTop += SPEEN_DISTANCE;
            if (slideTop >= frame.bottom) {
                slideTop = frame.top;
            }
            canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING, slideTop + MIDDLE_LINE_WIDTH / 2, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE * density);
            paint.setTypeface(Typeface.DEFAULT);

            final Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (frame.bottom + frame.top - fontMetrics.bottom - fontMetrics.top) / 2 + squareSlide / 2 + 64;

            paint.setTextAlign(Paint.Align.CENTER);
//            canvas.drawText("将二维码放入框内, 即可自动扫描", frame.left, (frame.bottom + TEXT_PADDING_TOP * density), paint);
            canvas.drawText("将二维码放入框内, 即可自动扫描", frame.centerX(), baseline, paint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }

            //Ö»Ë¢ÐÂÉ¨Ãè¿òµÄÄÚÈÝ£¬ÆäËûµØ·½²»Ë¢ÐÂ
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);

        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
