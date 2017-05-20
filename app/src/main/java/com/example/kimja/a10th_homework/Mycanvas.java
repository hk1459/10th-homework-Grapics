package com.example.kimja.a10th_homework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Mycanvas extends View {
    Bitmap mBitmap;
    Canvas mCanvas;
    Paint mPaint = new Paint();
    //menus
    String onblur = "OFF";
    String oncolor ="OFF";
    String pencolor = "BLACK";
    // options
    String drawmode = "Pen";
    int rotate = 0;
    int move = 0;
    String onscale = "OFF";
    String onskew = "onprogress2";


    int pensize = 3;

    public void setDrawmode (String mode){
        this.drawmode = mode;
    }
    public Mycanvas(Context context) {
        super(context);
        this.setLayerType(LAYER_TYPE_SOFTWARE,null);
        setPen(pencolor,pensize);
    }

    public Mycanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(LAYER_TYPE_SOFTWARE,null);
        setPen(pencolor,pensize);
    }
    private void setPen(String pencolor, int pensize){
        if(pencolor.equals("BLACK")) mPaint.setColor(Color.BLACK);
        else if(pencolor.equals("RED")) mPaint.setColor(Color.RED);
        else if(pencolor.equals("BLUE")) mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(pensize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.YELLOW);

    }

    private void drawStamp(int x , int y){
        Bitmap img = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Bitmap bigimg = Bitmap.createScaledBitmap(img, img.getWidth()*3/2,img.getHeight()*3/2,false);
        if(onskew.equals("ON")){
            mCanvas.skew(0.2f,0);
            onskew = "onprogress";
        } else if(onskew.equals("OFF")) {
            mCanvas.skew(-0.2f,0);
            onskew = "onprogress2";
        }

        if(onblur.equals("ON")){
            BlurMaskFilter blur = new BlurMaskFilter(150, BlurMaskFilter.Blur.NORMAL);
            mPaint.setMaskFilter(blur);
        } else if(onblur.equals("OFF")){
            mPaint.setMaskFilter(null);
        }
        if(oncolor.equals("ON")){
            float[] array = {
                    2,0,0,0,-25f,
                    0,2,0,0,-25f,
                    0,0,2,0,-25f,
                    0,0,0,2,0
            };

            ColorMatrix colorMatrix = new ColorMatrix(array);
            ColorMatrixColorFilter filter =
                    new ColorMatrixColorFilter(colorMatrix);
            mPaint.setColorFilter(filter);
        } else if (oncolor.equals("OFF")){
            mPaint.setColorFilter(null);
        }


        Matrix matrix = new Matrix();
        if(rotate == 12) rotate = 0;
        matrix.postRotate(30*rotate);
        if(onscale.equals("ON")){
            Bitmap bigimg2 = Bitmap.createBitmap(bigimg, 0, 0, bigimg.getWidth(), bigimg.getHeight(), matrix, true);
            mCanvas.drawBitmap(bigimg2,x - bigimg2.getWidth()/2 + 10*move,y - bigimg2.getHeight()/2+ 10*move,mPaint);
        } else if(onscale.equals("OFF")) {
            Bitmap img2 = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            mCanvas.drawBitmap(img2,x - img2.getWidth()/2+ 10*move,y - img2.getHeight()/2+ 10*move,mPaint);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(mBitmap != null){
            canvas.drawBitmap(mBitmap,0,0,null);
        }

    }

    int oldX = -1, oldY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setPen(pencolor,pensize);
        int X = (int)event.getX();
        int Y = (int)event.getY();
        if(drawmode.equals("Stamp")) {
            drawStamp(X,Y);
        } else if (drawmode.equals("Pen")) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                oldX = X ; oldY = Y;
            }
            else if( event.getAction() == MotionEvent.ACTION_MOVE){
                if(oldX != -1){
                    mCanvas.drawLine(oldX,oldY,X,Y,mPaint);
                    invalidate();
                    oldX = X; oldY = Y;
                }
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                if(oldX != -1){
                    mCanvas.drawLine(oldX,oldY,X,Y,mPaint);
                    invalidate();
                }
                oldX = -1; oldY = -1;
            }
        }

        return true;
    }
    public boolean Save(String file_name) {  //파일저장
        try {
            FileOutputStream out = new FileOutputStream(file_name);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
        return false;
    }
    public void Read(String file_name) { //파일 읽기
        mCanvas.drawBitmap(BitmapFactory.decodeFile(file_name),0,0,mPaint);

        invalidate();
    }
    public void clear() {
        mBitmap.eraseColor(Color.WHITE);
        invalidate();
    }
}
