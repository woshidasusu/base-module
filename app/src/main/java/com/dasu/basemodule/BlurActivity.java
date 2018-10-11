package com.dasu.basemodule;

import android.graphics.Bitmap;
import android.os.Bundle;
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
    int radius = 1;
    int sampling = 1;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            Log.e("!!!!!", "begin");
            DBlur.source(imageView).intoTarget(imageView1).animAlpha().mode(i).build().doBlurSync();
            Log.e("!!!!!", "end");
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            Log.e("!!!!!", "begin");
            DBlur.source(this, R.drawable.timg).intoTarget(imageView1).animAlpha().mode(i++).build()
                    .doBlur(new OnBlurListener() {
                        @Override
                        public void onBlurSuccess(Bitmap bitmap) {
                            Log.e("!!!!!", "onBlurSuccess");
                        }

                        @Override
                        public void onBlurFailed() {
                            Log.e("!!!!!", "onBlurFailed");
                        }
                    });
            Log.e("!!!!!", "end");
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            DBlur.source(imageView).intoTarget(imageView1).animAlpha().mode(i).radius(radius++).sampling(sampling).build().doBlur();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            DBlur.source(imageView).intoTarget(imageView1).animAlpha().mode(i).radius(radius).sampling(sampling++).build().doBlur();
        }
        if (i > 3) {
            i = 0;
        }
        return super.onKeyUp(keyCode, event);
    }
}
