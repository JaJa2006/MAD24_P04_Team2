package com.example.main_activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CenteredImageView extends AppCompatImageView {
    private Bitmap mBitmap;
    private Paint paint;

    public CenteredImageView(Context context) {
        super(context);
        init();
    }

    public CenteredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CenteredImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setImageBitmapCentered(Bitmap bm) {
        mBitmap = bm;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            int bitmapWidth = mBitmap.getWidth();
            int bitmapHeight = mBitmap.getHeight();

            float scale = Math.min((float) canvasWidth / bitmapWidth, (float) canvasHeight / bitmapHeight);

            float dx = (canvasWidth - bitmapWidth * scale) / 2;
            float dy = (canvasHeight - bitmapHeight * scale) / 2;

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            matrix.postTranslate(dx, dy);

            canvas.drawBitmap(mBitmap, matrix, paint);
        }
    }
}
