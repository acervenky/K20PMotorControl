package cn.jayneo.gameprophet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cn.jayneo.gameprophet.R;
import cn.jayneo.gameprophet.utils.FloatingManager;

public class FloatingView extends FrameLayout {

    private WindowManager.LayoutParams mParams;
    private View displayView;
    private ImageView imageView;
    private TextView life;
    private Context mContext;
    private FloatingManager mWindowManager;
    private int mTouchStartX, mTouchStartY;//手指按下时坐标

    public FloatingView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        displayView = mLayoutInflater.inflate(R.layout.image_display, null);
        imageView = displayView.findViewById(R.id.image_display_imageview);
        life = displayView.findViewById(R.id.life);
        imageView.setImageResource(R.drawable.image_01);
        displayView.setOnTouchListener(mOnTouchListener);
        mWindowManager = FloatingManager.getInstance(mContext);
    }

    public void show() {
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP | Gravity.LEFT;
        mParams.x = 0;
        mParams.y = 0;
        //总是出现在应用程序窗口之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        /*mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;*/
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mParams.width = LayoutParams.WRAP_CONTENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(displayView, mParams);
        //逐帧动画
        /*AnimationDrawable animationDrawable=(AnimationDrawable)imageView.getDrawable();
        animationDrawable.start();*/
    }

    public void hide() {
        mWindowManager.removeView(displayView);
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - mTouchStartX;
                    int movedY = nowY - mTouchStartY;
                    mTouchStartX = nowX;
                    mTouchStartY = nowY;
                    mParams.x += movedX;
                    mParams.y += movedY;
                    mWindowManager.updateView(displayView, mParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    };

    public void setXY(int x, int y) {
        mParams.x = x;
        mParams.y = y;
        mWindowManager.updateView(displayView, mParams);
    }

    public void setLife(String lif){
        life = displayView.findViewById(R.id.life);
        life.setText(lif);
        mWindowManager.updateView(displayView, mParams);
    }

    public void setId(String id, Context context) {
        FileInputStream fs = null;
        try {
            fs = new FileInputStream("/sdcard/Prophet/imgs/" + id + ".jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap  = BitmapFactory.decodeStream(fs);
        imageView.setImageBitmap(bitmap);
        mWindowManager.updateView(displayView, mParams);
    }

}
