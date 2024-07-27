package com.example.main_activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View {
    private Paint paint;
    private Path path;
    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Paint> paints = new ArrayList<>();
    private Bitmap bitmap;
    private Canvas canvas;
    private boolean isEraser = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10f);
        path = new Path();
        paths.add(path);
        paints.add(paint);
        bitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                this.canvas.drawPath(path, paint);
                path = new Path();
                paint = new Paint(paint);
                if (isEraser) {
                    paint.setColor(Color.WHITE);
                    paint.setStrokeWidth(20f);
                }
                paths.add(path);
                paints.add(paint);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setDrawingMode() {
        isEraser = false;
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10f);
    }

    public void setEraserMode() {
        isEraser = true;
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(20f);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void clear() {
        path.reset();
        paths.clear();
        paints.clear();
        path = new Path();
        paint = new Paint(paint);
        paths.add(path);
        paints.add(paint);
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        invalidate();
    }
}
