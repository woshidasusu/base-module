package com.dasu.basemodule;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.dasu.blur.DBlur;
import com.dasu.blur.OnBlurListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BlurActivity extends AppCompatActivity {

    @InjectView(R.id.iv_blur_bg)
    ImageView imageView;
    @InjectView(R.id.iv_blur1)
    ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);
        ButterKnife.inject(this);


    }

    int i = 0;
    int radius = 10;
    int sampling = 1;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            DBlur.source(this, R.drawable.timg).modeRs().radius(radius).sampling(sampling).build().doBlur();
//            testBlur();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

            DBlur.source(this, R.drawable.image).mode(i++).radius(radius).sampling(sampling).build()
                    .doBlur(new OnBlurListener() {
                        @Override
                        public void onBlurSuccess(Bitmap bitmap) {
                            Log.e("!!!!!", "onBlurSuccess");
                            imageView1.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBlurFailed() {
                            Log.e("!!!!!", "onBlurFailed");
                        }
                    });

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            DBlur.source(this).intoTarget(imageView1).animAlpha().mode(i).radius(radius++).sampling(sampling).build().doBlur();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            DBlur.source(imageView).intoTarget(imageView1).animAlpha().mode(i).radius(radius).sampling(sampling++).build().doBlur();
        }
        if (i > 3) {
            i = 0;
        }

        return super.onKeyUp(keyCode, event);
    }

    private void testBlur() {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = SystemClock.uptimeMillis();
            DBlur.source(this, R.drawable.timg).modeRs().radius(20).sampling(1).build().doBlurSync();
            long end = SystemClock.uptimeMillis();
            sum += (end - time);
        }
        Log.e("DBlur", "modeRs cast " + (sum/100) + "ms");

        sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = SystemClock.uptimeMillis();
            DBlur.source(this, R.drawable.timg).modeNative().radius(20).sampling(1).build().doBlurSync();
            long end = SystemClock.uptimeMillis();
            sum += (end - time);
        }
        Log.e("DBlur", "modeNative cast " + (sum/100) + "ms");

        sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = SystemClock.uptimeMillis();
            DBlur.source(this, R.drawable.timg).modeJava().radius(20).sampling(1).build().doBlurSync();
            long end = SystemClock.uptimeMillis();
            sum += (end - time);
        }
        Log.e("DBlur", "modeJava cast " + (sum/100) + "ms");

        sum = 0;
        for (int i = 0; i < 100; i++) {
            long time = SystemClock.uptimeMillis();
            DBlur.source(this, R.drawable.timg).modeStack().radius(20).sampling(1).build().doBlurSync();
            long end = SystemClock.uptimeMillis();
            sum += (end - time);
        }
        Log.e("DBlur", "modeStack cast " + (sum/100) + "ms");




















    }
}

